package com.keller.user;

import com.alibaba.fastjson.JSON;
import com.keller.common.config.Constants;
import com.keller.common.config.MqConstants;
import com.keller.common.entity.EmailLog;
import com.keller.common.util.Console;
import com.keller.common.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

public class EmailSender {

    /**
     * 邮件验证码标题
     */
    public static final String TITLE ="【From】" + Constants.appName;

    /**
     * 邮件验证码正文内容
     */
    public static final String RegisterBody =
            "验证码:%1$s，用于账号： %2$s 注册，泄露有风险。"
                    + Constants.EMAIL_CODE_TIME + "分钟内使用有效。";

    public static final String LoginBody =
            "验证码:%1$s，用于账号： %2$s 登录，泄露有风险。"
                    + Constants.EMAIL_CODE_TIME + "分钟内使用有效。";

    public static final String ResetPasswordBody =
            "验证码:%1$s，用于账号： %2$s 找回密码，泄露有风险。"
                    + Constants.EMAIL_CODE_TIME + "分钟内使用有效。";

    public static final String ResetPasswordLinkBody =
            "您好！我们已收到您的账号： %1$s 重置密码的申请，" +
                    "请点击链接重置密码： " + Constants.webUrl +
                    "/ResetPassword/%2$s ，该链接使用一次后失效";


    @Resource
    private MailProperties mailProperties;
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private RabbitTemplate rabbitTemplate;



    /**
     * 发送邮件验证码
     * 1.随机生成指定位数的验证码
     * 2.创建邮件实体类，设置收件人地址、邮件标题、邮件内容、验证码
     * 3.发送邮件并记录发送结果
     * 4.返回包含发送结果的邮件实体类
     * @param email 收件人邮箱
     * @return 邮件实体类EmailEntity
     */
    public boolean sendCode(int type, String email){
        String code = StringUtils.getAllCharString(Constants.EMAIL_CODE_LENGTH);
        EmailLog entity = new EmailLog();
        entity.setEmail(email);
        entity.setType(type);
        entity.setTitle(TITLE);
        String body;
        switch (type){
            case Constants.REGISTER_TYPE :
                body = String.format(RegisterBody,code,email);
                break;
            case Constants.LOGIN_TYPE :
                body = String.format(LoginBody,code,email);
                break;
            case Constants.RESET_PASSWORD_TYPE :
                body = String.format(ResetPasswordBody,code,email);
                break;
            default:return false;
        }
        entity.setContent(body);
        entity.setCode(code);
        boolean result = false;
        try {
            result = sendSimpleMail(entity.getEmail(),entity.getTitle(),entity.getContent());
        }catch (Exception e){
            Console.error("send sendVerificationCode error :",e.getMessage());
            entity.setResult(e.getMessage());
            entity.setStatusCode(Constants.FAILED);
        }
        rabbitTemplate.convertAndSend(MqConstants.EMAIL_LOG, JSON.toJSONString(entity));
        return result;
    }

    /**
     * 发送重置验证码邮件，该邮件中会包含一个重置密码的链接
     * @param email
     * @return
     */
    public boolean sendResetPasswordEmail(String email,String code,String token){
        EmailLog entity = new EmailLog();
        entity.setEmail(email);
        entity.setType(Constants.RESET_PASSWORD_TYPE );
        entity.setTitle(TITLE);
        String body = String.format(ResetPasswordLinkBody,email,token);

        entity.setContent(body);
        entity.setCode(code);
        boolean result = false;
        try {
            result = sendSimpleMail(entity.getEmail(),entity.getTitle(),entity.getContent());
        }catch (Exception e){
            Console.error("send sendResetPasswordEmail error :",e.getMessage());
            entity.setResult(e.getMessage());
            entity.setStatusCode(Constants.FAILED);
        }
        rabbitTemplate.convertAndSend(MqConstants.EMAIL_LOG, JSON.toJSONString(entity));
        return result;
    }



    public  boolean sendSimpleMail(String to,String title,String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 邮件发送来源
        simpleMailMessage.setFrom(mailProperties.getUsername());
        // 邮件发送目标
        simpleMailMessage.setTo(to);
        // 设置标题
        simpleMailMessage.setSubject(title);
        // 设置内容
        simpleMailMessage.setText(content);

        try {
            // 发送
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
