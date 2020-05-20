package com.keller.note.controller;

import com.keller.common.util.ObjectUtils;
import com.keller.note.entity.NotesInfo;
import com.keller.note.service.NotesService;
import com.keller.common.http.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yangkaile
 * @date 2020-04-03 15:09:09
 */
@RestController
    @RequestMapping("/notes")
    public class NotesController {
        @Resource
        private NotesService service;


        @PostMapping
        public ResponseEntity add(Integer kellerUserId,String title,String subTitle,Integer sort){
            if(ObjectUtils.isEmpty(kellerUserId,title)){
                return Response.badRequest();
            }
            if(sort == null){
                sort = 0;
            }
            NotesInfo notesInfo = new NotesInfo(kellerUserId,title);
            notesInfo.setSubTitle(subTitle);
            notesInfo.setSort(sort);
            return Response.ok(service.save(notesInfo));
        }

    @PostMapping("/update")
    public ResponseEntity update(Integer kellerUserId,Integer id,String title,String subTitle,Integer sort){
        //用户ID和笔记本ID是必填参数
        if(kellerUserId == null || id == null){
            return Response.badRequest();
        }
        // 至少要修改一个数据
        if(!ObjectUtils.hasValue(title,subTitle,sort)){
            return Response.badRequest();
        }

        NotesInfo notesInfo = new NotesInfo(id);
        notesInfo.setTitle(title);
        notesInfo.setUserId(kellerUserId);
        notesInfo.setSubTitle(subTitle);
        notesInfo.setSort(sort);
        return Response.ok(service.save(notesInfo));
    }

    @PostMapping("/delete")
    public ResponseEntity delete(Integer kellerUserId,Integer id){
        //用户ID和笔记本ID是必填参数
        if(ObjectUtils.isEmpty(kellerUserId,id)){
            return Response.badRequest();
        }
        return Response.ok(service.deleteById(kellerUserId,id));
    }

    @GetMapping
    public ResponseEntity get(Integer kellerUserId){
        if(kellerUserId == null){
            return Response.badRequest();
        }
        return Response.ok(service.getListByUser(kellerUserId));
    }
}
