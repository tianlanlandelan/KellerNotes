package com.justdoit.keller.controller;

import com.justdoit.keller.common.response.Response;
import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.common.util.*;
import com.justdoit.keller.service.UserCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 上传
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class UploadController {

    @Resource
    private UserCardService userCardService;

    /**
     * 上传头像
     * @param file
     * @param token
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity upload(MultipartFile file,String token, HttpServletRequest request){
        if(file == null){
            return Response.badRequest();
        }
        Integer userId = JwtUtils.getUserIdFromLogin(token);
        if(userId == null){
            return Response.unauthorized();
        }
        ResultData resultData = userCardService.setPortrait(file,userId);
        return Response.ok(resultData);
    }

    @PostMapping("/img")
    public ResponseEntity uploadImg(MultipartFile file,Integer noteId,String token){
        Console.println("token",token);
        if(file == null || noteId == null){
            return Response.badRequest();
        }
        Integer userId = JwtUtils.getUserIdFromLogin(token);
        if(userId == null){
            return Response.unauthorized();
        }
        String fileName = null;
        try {
            fileName = ImageUtils.saveImg(file,noteId,userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.ok(ResultData.success(FileUtils.getImgUrl(fileName,userId)));
    }
}
