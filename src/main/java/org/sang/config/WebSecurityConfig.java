package org.sang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sang.bean.RespBean;
import org.sang.common.HrUtils;
import org.sang.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.io.PrintWriter;

/**
 * Created by sang on 2017/12/28.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启基于注解的安全配置
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    HrService hrService;//获取登录用户，并且比对密码
    @Autowired
    CustomMetadataSource metadataSource;//访问url所需要的角色信息
    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;//判断当前访问是否有权限
    @Autowired
    AuthenticationAccessDeniedHandler deniedHandler;//权限不足时的处理

    //认证管理
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(hrService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //需要忽略的路径
    @Override
    public void configure(WebSecurity web)
    {
        web.ignoring().antMatchers("/index.html", "/static/**", "/login_p", "/favicon.ico");
    }

    //拦截规则，表单登录、登录成功失败的响应
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>()
                {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o)
                    {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                })
                .and()
                .formLogin().loginPage("/login_p").loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password")
                .failureHandler((req, resp, e) ->
                {
                    resp.setContentType("application/json;charset=utf-8");
                    RespBean respBean;
                    if (e instanceof BadCredentialsException ||
                            e instanceof UsernameNotFoundException)
                    {
                        respBean = RespBean.error("账户名或者密码输入错误!");
                    }
                    else if (e instanceof LockedException)
                    {
                        respBean = RespBean.error("账户被锁定，请联系管理员!");
                    }
                    else if (e instanceof CredentialsExpiredException)
                    {
                        respBean = RespBean.error("密码过期，请联系管理员!");
                    }
                    else if (e instanceof AccountExpiredException)
                    {
                        respBean = RespBean.error("账户过期，请联系管理员!");
                    }
                    else if (e instanceof DisabledException)
                    {
                        respBean = RespBean.error("账户被禁用，请联系管理员!");
                    }
                    else
                    {
                        respBean = RespBean.error("登录失败!");
                    }
                    resp.setStatus(401);
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(respBean));
                    out.flush();
                    out.close();
                })
                .successHandler((req, resp, auth) ->
                {
                    resp.setContentType("application/json;charset=utf-8");
                    RespBean respBean = RespBean.ok("登录成功!", HrUtils.getCurrentHr());
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(respBean));
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, resp, authentication) ->
                {
                    resp.setContentType("application/json;charset=utf-8");
                    RespBean respBean = RespBean.ok("注销成功!");
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(respBean));
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(deniedHandler);//配置权限不足处理
    }
}