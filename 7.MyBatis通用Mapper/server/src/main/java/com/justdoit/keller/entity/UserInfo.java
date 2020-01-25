package com.justdoit.keller.entity;

import com.justdoit.keller.common.mybatis.BaseEntity;
import com.justdoit.keller.common.mybatis.annotation.*;

import java.util.Date;

/**
 * @author yangkaile
 * @date 2019-10-01 13:34:14
 */
@TableAttribute(name = "user_info")
public class UserInfo   extends BaseEntity {
    @KeyAttribute
    @FieldAttribute
    private int id;

    @FieldAttribute
    private Integer type;

    @FieldAttribute
    @IndexAttribute
    private String email;

    @FieldAttribute
    private String password;

    @FieldAttribute
    @SortAttribute
    private Date createTime = new Date();

    @FieldAttribute
    private Integer status ;

    @FieldAttribute
    private Integer isDelete;

    @FieldAttribute
    private Date updateTime = new Date();

    @FieldAttribute
    private Integer updateUserId;

    public UserInfo() {
    }

    public UserInfo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", type=" + type +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
