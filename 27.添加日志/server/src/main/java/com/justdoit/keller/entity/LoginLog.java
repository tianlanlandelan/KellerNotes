package com.justdoit.keller.entity;

import com.justdoit.keller.common.mybatis.BaseEntity;
import com.justdoit.keller.common.mybatis.annotation.FieldAttribute;
import com.justdoit.keller.common.mybatis.annotation.KeyAttribute;
import com.justdoit.keller.common.mybatis.annotation.TableAttribute;

import java.util.Date;

@TableAttribute(name = "login_log",comment = "登录日志表")
public class LoginLog extends BaseEntity {

    @KeyAttribute(autoIncr = true)
    @FieldAttribute
    private int id;

    @FieldAttribute(isIndex = true)
    private Integer userId;

    @FieldAttribute(isCondition = true)
    private String ip;

    @FieldAttribute
    private Date createTime = new Date();

}
