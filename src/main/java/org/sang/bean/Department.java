package org.sang.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 2018/1/7.
 */
@Data
public class Department
{
    private Long id;
    private String name;
    private Long parentId;
    private String depPath;
    private boolean enabled;
    private boolean isParent;
    //存储过程执行结果
    private Integer result;
    private List<Department> children = new ArrayList<>();

    public Department() {}

    public Department(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Department that = (Department) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }


    @JsonIgnore
    public Integer getResult()
    {
        return result;
    }

    @JsonIgnore
    public String getDepPath()
    {
        return depPath;
    }

}
