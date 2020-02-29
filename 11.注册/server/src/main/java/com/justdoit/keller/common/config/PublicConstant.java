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
     * 注册
     */
    public static final int REGISTER_TYPE = 0;

    /**
     * 登录
     */
    public static final int LOGIN_TYPE = 1;

    /**
     * 重置密码
     */
    public static final int RESET_PASSWORD_TYPE = 2;


    public static String port ;
    public static String address ;
    public static String appName ;
    public static String serviceUrl = "http://" + address + ":" + port;

    public static String mailServerHost;

    public static String mailServerUser;

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
