package com.justdoit.keller.common.config;

/**
 * 公用常量，项目中要到的一些常量
 * @author yangkaile
 * @date 2019-09-16 14:41:23
 */
public class PublicConstant {

    /**
     * 业务成功标示
     */
    public static final int SUCCESS  = 0;
    /**
     * 业务失败标示
     */
    public static final int FAILED = 1;

    /**
     * 注册类型
     */
    public static final int REGISTER_TYPE = 0;

    /**
     * 登录类型
     */
    public static final int LOGIN_TYPE = 1;

    /**
     * 重置密码类型
     */
    public static final int RESET_PASSWORD_TYPE = 2;

    /**
     * 邮件验证码有效期
     */
    public static final int EMAIL_CODE_TIME = 5;

    /**
     * 邮件验证码长度
     */
    public static final int EMAIL_CODE_LENGTH = 6;


    /**
     * 应用启动的端口号
     */
    public static String port ;

    /**
     * 应用运行的 IP 地址
     */
    public static String address ;


    /**
     * 应用名
     */
    public static String appName ;

    /**
     * 服务器访问地址
     */
    public static String serviceUrl = "http://" + address + ":" + port;

    /**
     * 邮件服务器地址
     */
    public static String mailServerHost;


    /**
     * 邮件服务器登录用户名
     */
    public static String mailServerUser;


    /**
     * 邮件服务器登录密码
     */
    public static String mailServerPassword;

    /**
     * 通用，不做访问权限设置
     */
    public static final int AUTHORITY_COMMON = 1 << 0;

    /**
     * 用户登录后可以访问
     */
    public static final int AUTHORITY_LOGON  = 1 << 1;

    /**
     * 管理员可以访问
     */
    public static final int AUTHORITY_ADMIN   = 1 << 2;

    /**
     * 超级管理员可以访问
     */
    public static final int AUTHORITY_SUPPER_ADMIN = 1 << 3;


}
