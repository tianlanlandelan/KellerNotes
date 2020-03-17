package com.justdoit.keller.controller;

import com.justdoit.keller.common.config.RequestConfig;
import com.justdoit.keller.common.response.Response;
import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.common.util.Console;
import com.justdoit.keller.common.util.DateUtils;
import com.justdoit.keller.common.util.JwtUtils;
import com.justdoit.keller.entity.UserInfo;
import com.justdoit.keller.service.UserCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 上传
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class UploadController {

    @Resource
    private UserCardService userCardService;

    @PostMapping
    public ResponseEntity upload(MultipartFile file, HttpServletRequest request){
        if(file == null){
            return Response.badRequest();
        }
        String token = request.getHeader(RequestConfig.TOKEN);
        Integer userId = JwtUtils.getUserId(token);
        if(userId == null){
            return Response.unauthorized();
        }
        ResultData resultData = userCardService.setPortrait(file,userId);
        return Response.ok(resultData);
    }
}
