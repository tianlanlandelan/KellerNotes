package com.keller.common.entity;

import com.keller.common.mybatis.BaseEntity;
import com.keller.common.mybatis.annotation.FieldAttribute;
import com.keller.common.mybatis.annotation.KeyAttribute;
import com.keller.common.mybatis.annotation.SortAttribute;
import com.keller.common.mybatis.annotation.TableAttribute;

import java.util.Date;

@TableAttribute(name = "login_log",comment = "登录日志表")
public class LoginLog extends BaseEntity {

    @KeyAttribute(autoIncr = true)
    @FieldAttribute
    private int id;

    @FieldAttribute(isIndex = true)
    private Integer userId;

    @FieldAttribute(value = "登录IP",isCondition = true)
    private String ip;

    @FieldAttribute
    @SortAttribute
    private Date createTime = new Date();


    public LoginLog() {
    }

    public LoginLog(Integer userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "LoginLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", ip='" + ip + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
