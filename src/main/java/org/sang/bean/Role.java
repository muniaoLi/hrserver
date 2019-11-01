package org.sang.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by sang on 2017/12/28.
 */
@Data
public class Role implements Serializable {
    private Long id;
    private String name;
    private String nameZh;
}
