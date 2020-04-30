package com.justdoit.keller.service;

import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.common.util.DateUtils;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.LoginLog;
import com.justdoit.keller.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录日志相关操作
 * @author yangkaile
 * @date 2020-04-29 08:31:42
 */
@Service
public class LoginLogService {
    @Resource
    private LoginLogMapper mapper;

    public void insert(int userId,String ip){
        LoginLog log = new LoginLog(userId);
        log.setIp(ip);
        mapper.baseInsertAndReturnKey(log);
    }

    /**
     * 含前含后
     * @param startDate 开始日期，格式 YYYY-MM-DD
     * @param endDate   截止日期，格式 YYYY-MM-DD
     * @return
     */
    public ResultData getCountByDay(String startDate,String endDate){
        if(StringUtils.isEmpty(endDate)){
            endDate = DateUtils.getNextDay(startDate);
        }else{
            endDate = DateUtils.getNextDay(endDate);
        }
        return ResultData.success(mapper.getCountByDay(startDate,endDate));
    }

    /**
     * 含前含后
     * @param startDate 开始日期，格式 YYYY-MM-DD
     * @param endDate   截止日期，格式 YYYY-MM-DD
     * @return
     */
    public ResultData getCountByMonth(String startDate,String endDate){
        if(StringUtils.isEmpty(endDate)){
            endDate = DateUtils.getNextMonth(startDate);
        }else{
            endDate = DateUtils.getNextMonth(endDate);
        }
        return ResultData.success(mapper.getCountByMonth(startDate,endDate));
    }

}
