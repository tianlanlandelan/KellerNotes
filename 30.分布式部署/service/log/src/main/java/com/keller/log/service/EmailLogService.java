package com.keller.log.service;

import com.keller.common.entity.EmailLog;
import com.keller.common.http.ResultData;
import com.keller.log.mapper.EmailLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yangkaile
 * @date 2019-11-22 15:54:45
 */
@Service
public class EmailLogService {
    @Resource
    private EmailLogMapper mapper;

    /**
     * 校验验证码
     * @param email
     * @param code
     * @param type
     * @return
     */
    public ResultData checkCode(String email, String code, Integer type){

        EmailLog result = new EmailLog();
        result.setType(type);
        result.setEmail(email);
        result.setBaseKyleUseASC(false);
        result.setBaseKyleUseAnd(true);
        List<EmailLog> list = mapper.baseSelectList(result);
        if(list == null || list.size() <= 0){
            return ResultData.error("没有符合条件的数据");
        }
        result = list.get(0);
        if(result.isEfficientVerificationCode() &&
                result.getCode().equals(code)){
            setCodeUsed(result);
            return ResultData.success();
        }
        return ResultData.error("验证失败");
    }

    private void setCodeUsed(EmailLog emailLog){
        emailLog.setIsUsed(1);
        mapper.baseUpdateById(emailLog);
    }
}
