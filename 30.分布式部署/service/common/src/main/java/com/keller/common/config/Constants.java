package com.keller.common.config;

/**
 * 公用常量，项目中要到的一些常量
 * @author yangkaile
 * @date 2019-09-16 14:41:23
 */
public class Constants {

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
     * 应用启动的端口号
     */
    public static String port ;

    /**
     * 本地IP
     */
    public static final String localIp = "127.0.0.1";


    /**
     * 应用名
     */
    public static final String appName = "KellerNotes";


    /**
     * 不需要身份验证就可以访问的接口
     */
    public static final String baseMapping = "/base";

    /**
     * 需要管理员身份才能访问的接口
     */
    public static final String adminMapping = "/admin";


    public static String nginxPath = "/Users/yangkaile/Projects/nginx/keller/";

    public static String nginxUrl = "http://localhost:8081/keller/";

    /**
     * 图片保存路径
     */
    public static String imgPath = "image/";

    /**
     * 头像保存路径
     */
    public static String portraitPath = "portrait/";

    /**
     * 原图保存路径
     */
    public static String originImgPath = "img/";

    /**
     * 缩略图保存路径
     */
    public static String thumbnailPath = "thum/";
    /**
     * web 访问地址
     */
    public static String webUrl;

    public static final String DEFAULT_NOTES_NAME = "默认笔记本";

    public static final int DEFAULT_NOTES_STATUS = 1;

    /**
     * 头像的前缀名
     */
    public static final String PORTRAIT_PREFIX = "kellerNotes";


    /**
     *  头像格式
     */
    public static final String PORTRAIT_TYPE = "png";
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

    /**
     * 用户id参数固定名称，不接收外部传入的用户id
     */
    public static final String USER_ID_KEY = "kellerUserId";

    /**
     * 管理员id参数固定名称，不接收外部传入的管理员id
     */
    public static final String ADMIN_ID_KEY = "kellerAdminId";

    public static final String packageName = "com.keller";
}
