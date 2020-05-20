package com.keller.log.controller;

import com.keller.common.config.Constants;
import com.keller.common.http.Response;
import com.keller.common.util.ObjectUtils;
import com.keller.log.service.LoginLogService;
import com.keller.log.service.RegisterLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 仅管理员可访问的接口
 * @author yangkaile
 * @date 2020-05-20 09:24:07
 */
@RestController
@RequestMapping(Constants.adminMapping)
public class AdminController {
    @Resource
    private LoginLogService loginLogService;

    @Resource
    private RegisterLogService registerLogService;

    @GetMapping("/loginLogByDay")
    public ResponseEntity getLoginLogByDay(String startDate, String endDate){
        if(ObjectUtils.isEmpty(startDate)){
            return Response.badRequest();
        }
        return Response.ok(loginLogService.getCountByDay(startDate,endDate));
    }

    @GetMapping("/loginLogByMonth")
    public ResponseEntity getLoginLogByMonth(String startDate,String endDate){
        if(ObjectUtils.isEmpty(startDate)){
            return Response.badRequest();
        }
        return Response.ok(loginLogService.getCountByMonth(startDate,endDate));
    }



    @GetMapping("/registerLogByDay")
    public ResponseEntity getRegisterLogByDay(String startDate, String endDate){
        if(ObjectUtils.isEmpty(startDate)){
            return Response.badRequest();
        }
        return Response.ok(registerLogService.getCountByDay(startDate,endDate));
    }

    @GetMapping("/registerLogByMonth")
    public ResponseEntity getRegisterLogByMonth(String startDate,String endDate){
        if(ObjectUtils.isEmpty(startDate)){
            return Response.badRequest();
        }
        return Response.ok(registerLogService.getCountByMonth(startDate,endDate));
    }
}
