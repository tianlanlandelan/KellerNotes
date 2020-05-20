package com.keller.log.service;

import com.keller.common.util.ObjectUtils;
import com.keller.log.mapper.RegisterLogMapper;
import com.keller.common.http.ResultData;
import com.keller.common.util.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 注册日志
 * @author yangkaile
 * @date 2020-04-29 08:31:01
 */
@Service
public class RegisterLogService {
    @Resource
    private RegisterLogMapper mapper;

    /**
     * 含前含后
     * @param startDate 开始日期，格式 YYYY-MM-DD
     * @param endDate   截止日期，格式 YYYY-MM-DD
     * @return
     */
    public ResultData getCountByDay(String startDate, String endDate){
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
