package com.justdoit.keller.service;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.common.util.SendEmailUtils;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.EmailLog;
import com.justdoit.keller.mapper.EmailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangkaile
 * @date 2019-11-22 15:54:45
 */
@Service
public class EmailService {
    @Resource
    private EmailMapper mapper;

    /**
     * 发送邮件验证码
     * @param type
     * @param email
     * @return
     */
    public ResultData sendCode(int type, String email){
        EmailLog emailLog = SendEmailUtils.sendVCode(type,email,StringUtils.getAllCharString(PublicConstant.EMAIL_CODE_LENGTH));
        if(emailLog == null){
            return ResultData.error("邮件发送失败");
        }
        mapper.baseInsertAndReturnKey(emailLog);
        return ResultData.success();
    }


    /**
     * 发送重置密码的邮件
     * @param email
     * @param code
     * @param token
     * @return
     */
    public ResultData sendResetPasswordEmail(String email,String code,String token){
        EmailLog emailLog = SendEmailUtils.sendResetPasswordEmail(email,code,token);
        if(emailLog == null){
            return ResultData.error("邮件发送失败");
        }
        mapper.baseInsertAndReturnKey(emailLog);
        return ResultData.success();
    }

    /**
     * 校验验证码
     * @param email
     * @param code
     * @param type
     * @return
     */
    public boolean checkCode(String email,String code,Integer type){

        EmailLog result = new EmailLog();
        result.setType(type);
        result.setEmail(email);
        result.setBaseKyleUseASC(false);
        result.setBaseKyleUseAnd(true);
        List<EmailLog> list = mapper.baseSelectList(result);
        if(list == null || list.size() <= 0){
            return false;
        }
        result = list.get(0);
        if(result.isEfficientVerificationCode() &&
                result.getCode().equals(code)){
            setCodeUsed(result);
            return true;
        }
        return false;
    }

    private void setCodeUsed(EmailLog emailLog){
        emailLog.setIsUsed(1);
        mapper.baseUpdateById(emailLog);
    }
}
