package com.keller.common.config;

/**
 * RabbitMQ 队列名及队列消息格式
 * @author yangkaile
 * @date 2020-05-20 13:41:39
 */
public class MqConstants {
    /**
     * 错误日志队列名
     */
    public static final String ERROR_LOG = "errorLog";
    /**
     * 登录日志队列名
     */
    public static final String LOGON_LOG = "logonLog";
    /**
     * 注册日志队列名
     */
    public static final String REGISTER_LOG = "registerLog";
    /**
     * 邮件发送日志队列名
     */
    public static final String EMAIL_LOG = "emailLog";


}
