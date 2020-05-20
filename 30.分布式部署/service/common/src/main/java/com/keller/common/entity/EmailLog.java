package com.keller.common.entity;


import com.keller.common.mybatis.BaseEntity;
import com.keller.common.mybatis.annotation.FieldAttribute;
import com.keller.common.mybatis.annotation.KeyAttribute;
import com.keller.common.mybatis.annotation.SortAttribute;
import com.keller.common.mybatis.annotation.TableAttribute;

import java.util.Date;

/**
 * @author yangkaile
 * @date 2019-09-29 10:58:52
 */
@TableAttribute(name = "email_log")
public class EmailLog extends BaseEntity {
    @FieldAttribute
    @KeyAttribute(autoIncr = true)
    private int id;
    /**
    * 邮件发送类型
    */
    @FieldAttribute(value = "邮件发送类型,不能为空",notNull = true,isCondition = true)
     private Integer type;
    /**
    * 收件人
    */
    @FieldAttribute(value = "收件人，不能为空",notNull = true,isIndex = true)
     private String email;
    /**
    * 标题
    */
    @FieldAttribute(value = "邮件标题，不能为空",notNull = true,length = 200)
     private String title;
    /**
    * 内容
    */
    @FieldAttribute(value = "邮件内容，不能为空",notNull = true,length = 500)
     private String content;
    /**
     * 验证码
     * 包含验证码的邮件，需要将验证码单独填写，方便查询
    */
    @FieldAttribute("验证码")
     private String code;

    @FieldAttribute(value = "发送结果描述，如：发送失败的原因等",length = 500)
     private String result;
    /**
     * 状态码
     * 0 成功
     * 1 失败
     */
    @FieldAttribute(value = "发送状态",notNull = true)
     private int statusCode;

    /**
     * 发送时间，验证码邮件的有效时间为5分钟
     */
    @FieldAttribute(value = "发送时间",notNull = true)
    @SortAttribute
    private Date createTime = new Date();

    /**
     * 验证码是否已使用 0:未使用 1:已使用
     */
    @FieldAttribute("验证码是否已使用")
    private int isUsed = 0;

    public EmailLog(){
        super();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", result='" + result + '\'' +
                ", statusCode=" + statusCode +
                ", createTime=" + createTime +
                ", isUsed=" + isUsed +
                '}';
    }


    /**
     * 是否是有效的验证码
     * 要求成功发送，发送时间在5分钟内，且未使用过
     * @return
     */
    public boolean isEfficientVerificationCode(){
        return (isUsed == 0
                && statusCode == 0
        );
    }
}
