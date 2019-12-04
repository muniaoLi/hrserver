package org.sang.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.sang.HrserverApplication;
import org.sang.bean.Hr;
import org.sang.bean.Menu;
import org.sang.bean.Role;
import org.sang.mapper.HrMapper;
import org.sang.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HrserverApplication.class)
public class DaoTest
{
    @Autowired
    HrMapper hrMapper;

    @Test
    public void test1()
    {
        Hr hr = hrMapper.loadUserByUsername("admin");
        System.out.println(hr);
        List<Role> roles = hr.getRoles();
        System.out.println(roles);
    }

    @Test
    public void test2()
    {
        Hr hr = hrMapper.getHrById(3L);
        System.out.println(hr);
        List<Role> roles = hr.getRoles();
        System.out.println(roles);
    }

    @Autowired
    MenuMapper menuMapper;
    @Test
    public void test3(){
        List<Menu> menus = menuMapper.getAllMenu();
        System.out.println(menus);
    }
    @Test
    public void test4(){
        List<Menu> menus = menuMapper.getMenusByHrId(3L);
        System.out.println(menus);
    }

    @Test
    public void test5(){
        List<Menu> menus = menuMapper.menuTree();
        System.out.println(menus);
    }

    @Test
    public void test6()
    {
        Menu menu = menuMapper.getMenuTree();
        System.out.println(menu);
    }
}
