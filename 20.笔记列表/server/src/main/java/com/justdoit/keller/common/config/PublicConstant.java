package com.justdoit.keller.common.config;

/**
 * 公用常量，项目中要到的一些常量
 * @author yangkaile
 * @date 2019-09-16 14:41:23
 */
public class PublicConstant {

    /**
     * 默认的用户类型，即普通用户类型
     */
    public static final int DEFAULT_USER_TYPE = 0;

    /**
     * 管理员的用户类型
     */
    public static final int ADMIN_USER_TYPE = 100;

    public static boolean notUserType(Integer userType){
        return !isUserType(userType);
    }

    public static boolean isUserType(Integer userType){
        if(userType == null){
            return false;
        }
        return userType == DEFAULT_USER_TYPE || userType == ADMIN_USER_TYPE;
    }

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
     * JWT 有效时间 12 小时
     */
    public static final long JWT_EXP_TIME = 12 * 60 * 60 * 1000;

    /**
     * JWT 签名
     */
    public static final String JWT_SIGN_KEY = "kellerNotes20241002";

    /**
     * 应用启动的端口号
     */
    public static String port ;


    /**
     * 应用名
     */
    public static String appName ;

    /**
     * 服务器访问地址
     */
    public static String serviceUrl;

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

    public static String nginxPath;

    public static String nginxUrl;

    public static String imgPath;

    public static String thumPath;

    public static String webUrl;

    public static final String DEFAULT_NOTES_NAME = "默认笔记本";
    public static final int DEFAULT_NOTES_STATUS = 1;

    /**
     * 缩略图前缀名
     */
    public static final String THUM_PREFIX = "thum";

    /**
     * 缩略图最大宽度
     */
    public static final int THUM_MAX_WIDTH = 120;

    /**
     * 缩略图最大高度
     */
    public static final int THUM_MAX_HEIGHT = 120;

    /**
     * 富文本笔记类型
     */
    public static final int NOTE_TYPE_RICH_TEXT = 0;


    /**
     * MarkDown 笔记类型
     */
    public static final int NOTE_TYPE_MARK_DOWN = 1;

    public static final int NOTE_CONTENT_TEXT = 0;

    public static final int NOTE_CONTENT_HTML = 1;

    /**
     * 用户id参数固定名称，不接收外部传入的用户id
     */
    public static final String USER_ID_KEY = "kellerUserId";

    /**
     * 管理员id参数固定名称，不接收外部传入的管理员id
     */
    public static final String ADMIN_ID_KEY = "kellerAdminId";

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
