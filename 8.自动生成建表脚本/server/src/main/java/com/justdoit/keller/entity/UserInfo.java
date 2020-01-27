package com.justdoit.keller.entity;

import com.justdoit.keller.common.mybatis.BaseEntity;
import com.justdoit.keller.common.mybatis.annotation.*;

import java.util.Date;

/**
 * @author yangkaile
 * @date 2019-10-01 13:34:14
 */
@TableAttribute(name = "user_info",comment = "用户信息表")
public class UserInfo   extends BaseEntity {
    @KeyAttribute(autoIncr = true)
    @FieldAttribute("用户Id")
    private int id;

    @FieldAttribute(value = "用户类型",notNull = true)
    @IndexAttribute
    private Integer type;


    @FieldAttribute(value = "密码",length = 200)
    private String password;

    @FieldAttribute(value = "邮箱",notNull = true,length = 200)
    @IndexAttribute
    private String email;

    @FieldAttribute
    private Date createTime = new Date();

    @FieldAttribute("用户账号状态")
    @IndexAttribute
    private Integer status ;

    @FieldAttribute("是否删除，1 表示删除")
    @IndexAttribute
    private Integer isDelete;

    @FieldAttribute("最后一次修改时间")
    private Date updateTime = new Date();

    @FieldAttribute("修改人")
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
                ", isDelete=" + isDelete +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                '}';
    }
}
