package org.sang.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Data
public class Menu implements Serializable
{
    private Long id;
    private String url;
    private String path;
    private Object component;
    private String name;
    private String iconCls;
    private Long parentId;
    private List<Role> roles;
    private List<Menu> children;
    private MenuMeta meta;

    @JsonIgnore
    public String getUrl()
    {
        return url;
    }
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public Object getComponent()
    {
        return component;
    }
    @JsonIgnore
    public Long getParentId()
    {
        return parentId;
    }
    @JsonIgnore
    public List<Role> getRoles()
    {
        return roles;
    }

}
