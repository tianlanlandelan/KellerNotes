package com.justdoit.keller.util;

import com.justdoit.keller.config.StaticConfig;
import com.justdoit.keller.entity.EmailLog;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
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
    public static final String TITLE ="【From】" + StaticConfig.AppName;

    public static final int TIME = 5;

    /**
     * 邮件验证码正文内容
     */
    public static final String RegisterBody =
            "验证码:%1$s，用于账号： %2$s 注册，泄露有风险。"
                    + TIME + "分钟内使用有效。";

    public static final String LoginBody =
            "验证码:%1$s，用于账号： %2$s 登录，泄露有风险。"
                    + TIME + "分钟内使用有效。";

    public static final String ResetPasswordBody =
            "验证码:%1$s，用于账号： %2$s 找回密码，泄露有风险。"
                    + TIME + "分钟内使用有效。";

    /**
     * 邮件验证码长度
     */
    public static final int LENGTH = 6;


    private  String host;
    private  String user;
    private  String password;

    private static SendEmailUtils sender = new SendEmailUtils();

    private SendEmailUtils() {
    }

    public static SendEmailUtils getSender(String host, String user, String password){
        sender.host = host;
        sender.user = user;
        sender.password = password;
        return sender;
    }

    /**
     * 初始化Session
     * @return
     */
    private static  Session initSession(){
        Session session = null;
        try{
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.qq.com");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");
            //创建session
            session = Session.getInstance(prop);
            //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
            session.setDebug(true);
        }catch (Exception e){
            e.printStackTrace();
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
    public void sendSimpleMail(String toEmailAddress,String title,String content)
            throws Exception {
        Session session = initSession();
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(user));
        //指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmailAddress));
        //邮件的标题
        message.setSubject(title);
        //邮件的文本内容
        message.setContent(content, "text/html;charset=UTF-8");
        //返回创建好的邮件对象
        Transport transport = session.getTransport();
        //使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器,用户名和密码都通过验证之后才能够正常发送邮件给收件人
        transport.connect(host,user,password);
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
    public  EmailLog sendVCode(int type, String email){
        String code = StringUtils.getNumbserString(LENGTH);
        EmailLog entity = new EmailLog();
        entity.setEmail(email);
        entity.setType(type);
        entity.setTitle(TITLE);
        String body;
        switch (type){
            case StaticConfig.RegisterType :
                body = String.format(RegisterBody,code,email);
                break;
            case StaticConfig.LoginType :
                body = String.format(LoginBody,code,email);
                break;
            case StaticConfig.ResetPasswordType :
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
            entity.setStatusCode(StaticConfig.FAILED);
        }
        return entity;
    }


}
