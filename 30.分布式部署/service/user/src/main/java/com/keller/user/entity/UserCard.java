package com.keller.user.entity;
import com.keller.common.mybatis.BaseEntity;
import com.keller.common.mybatis.annotation.FieldAttribute;
import com.keller.common.mybatis.annotation.KeyAttribute;
import com.keller.common.mybatis.annotation.TableAttribute;

import java.util.Date;

@TableAttribute(name = "user_card",comment = "用户名片表")
public class UserCard extends BaseEntity {

    @FieldAttribute("用户ID")
    @KeyAttribute
    private int userId;

    @FieldAttribute(value = "昵称",length = 50)
    private String nickName;

    @FieldAttribute(value = "名片上要展示的邮箱地址",length = 100)
    private String email;

    @FieldAttribute(value = "用户头像最初的文件名",length = 100)
    private String portraitOriginName;

    @FieldAttribute(value = "用户头像名称",length = 100)
    private String portraitName;

    @FieldAttribute(value = "个人签名",length = 100)
    private String label;

    @FieldAttribute
    private Date createTime = new Date();

    @FieldAttribute
    private Date updateTime;

    @FieldAttribute
    private Integer updateUserId;

    private String protraitUrl;

    private String protraitThumUrl;

    public UserCard() {
    }

    public UserCard(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPortraitOriginName() {
        return portraitOriginName;
    }

    public void setPortraitOriginName(String portraitOriginName) {
        this.portraitOriginName = portraitOriginName;
    }

    public String getPortraitName() {
        return portraitName;
    }

    public void setPortraitName(String portraitName) {
        this.portraitName = portraitName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getProtraitUrl() {
        return protraitUrl;
    }

    public void setProtraitUrl(String protraitUrl) {
        this.protraitUrl = protraitUrl;
    }

    public String getProtraitThumUrl() {
        return protraitThumUrl;
    }

    public void setProtraitThumUrl(String protraitThumUrl) {
        this.protraitThumUrl = protraitThumUrl;
    }

    @Override
    public String toString() {
        return "UserCard{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", email='" + email + '\'' +
                ", portraitOriginName='" + portraitOriginName + '\'' +
                ", portraitName='" + portraitName + '\'' +
                ", label='" + label + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                '}';
    }
}
