package com.justdoit.keller.common.util;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.entity.EmailLog;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮件发送工具类
 * SendEmailUtils.getSender(
 *  myConfig.mailServerHost,
 *  myConfig.mailServerUser,
 *  myConfig.mailServerPassword)
 * .sendSimpleMail("atomiclong@aliyun.com",
 *                         "你好",
 *                         "邮件内容");
 * @author yangkaile
 * @date 2018-09-11 14:34:03
 */
public class SendEmailUtils {
    /**
     * 邮件验证码标题
     */
    public static final String TITLE ="【From】" + PublicConstant.appName;

    /**
     * 邮件验证码正文内容
     */
    public static final String RegisterBody =
            "验证码:%1$s，用于账号： %2$s 注册，泄露有风险。"
                    + PublicConstant.EMAIL_CODE_TIME + "分钟内使用有效。";

    public static final String LoginBody =
            "验证码:%1$s，用于账号： %2$s 登录，泄露有风险。"
                    + PublicConstant.EMAIL_CODE_TIME + "分钟内使用有效。";

    public static final String ResetPasswordBody =
            "验证码:%1$s，用于账号： %2$s 找回密码，泄露有风险。"
                    + PublicConstant.EMAIL_CODE_TIME + "分钟内使用有效。";

    public static final String ResetPasswordLinkBody =
            "您好！我们已收到您的账号： %1$s 重置密码的申请，" +
                    "请点击链接重置密码： " + PublicConstant.webUrl +
                    "/ResetPassword/%2$s ，该链接使用一次后失效";


    public static Session session= null;

    /**
     * 初始化Session
     * @return
     */
    private static  Session initSession(){
        if(session == null) {
            try {
                Properties prop = new Properties();
                //邮件服务器地址
                prop.setProperty("mail.host", PublicConstant.mailServerHost);
                //邮件发送协议
                prop.setProperty("mail.transport.protocol", "smtp");
                //是否需要身份验证
                prop.setProperty("mail.smtp.auth", "true");

                prop.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                prop.setProperty("mail.smtp.socketFactory.port", "465");
                prop.setProperty("mail.smtp.port", "465");
                //创建session
                session = Session.getInstance(prop);
                //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
                session.setDebug(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return session;
    }

    /**
     * 发送简单邮件
     * @param toEmailAddress 收件人地址
     * @param title 邮件标题
     * @param content 邮件内容
     * @throws Exception
     */
    private static void sendSimpleMail(String toEmailAddress,String title,String content)
            throws Exception {
        Session session = initSession();
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(PublicConstant.mailServerUser));
        //指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
        //邮件的标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        Transport transport = session.getTransport();
        //使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器,用户名和密码都通过验证之后才能够正常发送邮件给收件人
        transport.connect(PublicConstant.mailServerHost,PublicConstant.mailServerUser,PublicConstant.mailServerPassword);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     * 发送邮件验证码
     * 1.随机生成指定位数的验证码
     * 2.创建邮件实体类，设置收件人地址、邮件标题、邮件内容、验证码
     * 3.发送邮件并记录发送结果
     * 4.返回包含发送结果的邮件实体类
     * @param email 收件人邮箱
     * @return 邮件实体类EmailEntity
     */
    public static EmailLog sendVCode(int type, String email,String code){
        EmailLog entity = new EmailLog();
        entity.setEmail(email);
        entity.setType(type);
        entity.setTitle(TITLE);
        String body;
        switch (type){
            case PublicConstant.REGISTER_TYPE :
                body = String.format(RegisterBody,code,email);
                break;
            case PublicConstant.LOGIN_TYPE :
                body = String.format(LoginBody,code,email);
                break;
            case PublicConstant.RESET_PASSWORD_TYPE :
                body = String.format(ResetPasswordBody,code,email);
                break;
            default:return null;
        }
        entity.setContent(body);
        entity.setCode(code);
        try {
            sendSimpleMail(entity.getEmail(),entity.getTitle(),entity.getContent());
        }catch (Exception e){
            Console.error("send sendVerificationCode error :",e.getMessage());
            entity.setResult(e.getMessage());
            entity.setStatusCode(PublicConstant.FAILED);
        }
        return entity;
    }

    /**
     * 发送重置验证码邮件，该邮件中会包含一个重置密码的链接
     * @param email
     * @return
     */
    public static EmailLog sendResetPasswordEmail(String email,String code,String token){
        EmailLog entity = new EmailLog();
        entity.setEmail(email);
        entity.setType(PublicConstant.RESET_PASSWORD_TYPE );
        entity.setTitle(TITLE);
        String body = String.format(ResetPasswordLinkBody,email,token);

        entity.setContent(body);
        entity.setCode(code);
        try {
            sendSimpleMail(entity.getEmail(),entity.getTitle(),entity.getContent());
        }catch (Exception e){
            Console.error("send sendResetPasswordEmail error :",e.getMessage());
            entity.setResult(e.getMessage());
            entity.setStatusCode(PublicConstant.FAILED);
        }
        return entity;
    }


}
