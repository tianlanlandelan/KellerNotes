package com.keller.log.service;

import com.keller.common.util.ObjectUtils;
import com.keller.log.mapper.LoginLogMapper;
import com.keller.common.http.ResultData;
import com.keller.common.util.DateUtils;
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

    /**
     * 含前含后
     * @param startDate 开始日期，格式 YYYY-MM-DD
     * @param endDate   截止日期，格式 YYYY-MM-DD
     * @return
     */
    public ResultData getCountByDay(String startDate,String endDate){
        if(ObjectUtils.isEmpty(endDate)){
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
        if(ObjectUtils.isEmpty(endDate)){
            endDate = DateUtils.getNextMonth(startDate);
        }else{
            endDate = DateUtils.getNextMonth(endDate);
        }
        return ResultData.success(mapper.getCountByMonth(startDate,endDate));
    }

}
