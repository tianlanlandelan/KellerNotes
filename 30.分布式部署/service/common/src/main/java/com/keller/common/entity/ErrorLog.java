package com.keller.common.entity;

import com.keller.common.mybatis.BaseEntity;
import com.keller.common.mybatis.annotation.FieldAttribute;
import com.keller.common.mybatis.annotation.KeyAttribute;
import com.keller.common.mybatis.annotation.TableAttribute;

@TableAttribute(name = "error_log",comment = "系统错误日志")
public class ErrorLog extends BaseEntity {

    @KeyAttribute(autoIncr = true)
    @FieldAttribute
    private Integer id;

    @FieldAttribute
    private String ip;

    @FieldAttribute
    private String method;

    @FieldAttribute(length = 500)
    private String url;

    @FieldAttribute(length = 2000)
    private String params;

    @FieldAttribute(length = 1000)
    private String type;

    @FieldAttribute(length = 2000)
    private String message;


    public ErrorLog() {
    }

    public ErrorLog(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorLog{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", params='" + params + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
