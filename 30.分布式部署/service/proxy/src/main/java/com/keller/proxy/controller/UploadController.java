package com.keller.proxy.controller;

import com.keller.common.http.Response;
import com.keller.common.http.ResultData;
import com.keller.common.util.FileUtils;
import com.keller.common.util.ImageUtils;
import com.keller.common.util.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @author yangkaile
 * @date 2020-05-19 08:15:16
 * 上传接口
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*",allowedHeaders="*", maxAge = 3600)
public class UploadController {

    /**
     * 上传头像
     * 用户头像的名称和格式是固定的，每个用户的头像只会保存一个，上传新头像后会覆盖原有头像
     * @param file
     * @param token
     * @return
     */
    @PostMapping("/portrait")
    public ResponseEntity upload(MultipartFile file,String token) throws Exception{
        if(file == null){
            return Response.badRequest();
        }
        HashMap<String,String>  map = JwtUtils.readJwt(token);
        if(map == null){
            return Response.unauthorized();
        }
        int userId = Integer.parseInt(map.get(JwtUtils.ID));
        String originalFilename = file.getOriginalFilename();
        ImageUtils.savePortrait(file,userId);
        return Response.ok(ResultData.success(originalFilename));
    }

    /**
     * 上传图片
     * @param file
     * @param noteId
     * @param token
     * @return
     */
    @PostMapping("/img")
    public ResponseEntity uploadImg(MultipartFile file,Integer noteId,String token){
        if(file == null || noteId == null){
            return Response.badRequest();
        }
        HashMap<String,String>  map = JwtUtils.readJwt(token);
        if(map == null){
            return Response.unauthorized();
        }
        String fileName = null;
        int userId = Integer.parseInt(map.get(JwtUtils.ID));
        try {
            fileName = ImageUtils.saveImg(file,noteId,userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.ok(ResultData.success(FileUtils.getImgUrl(fileName,userId)));
    }
}
