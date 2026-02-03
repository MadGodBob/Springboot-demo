package com.wms.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String no;
    private String name;
    private String password;
    private Integer sex;
    private Integer roleId;
    private String phone;
    @TableField("isValid")
    private String isValid;
}
