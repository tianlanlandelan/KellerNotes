package com.justdoit.keller.controller;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.response.Response;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.NoteInfo;
import com.justdoit.keller.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 *
 * @author yangkaile
 * @date 2020-04-05 10:45:51
 */
@RestController
@RequestMapping("/note")
public class NoteController {

    @Resource
    private NoteService service;

    /**
     * 获取笔记本中的笔记列表
     * @param kellerUserId
     * @param notesId
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getList(Integer kellerUserId,Integer notesId){
        if(StringUtils.isEmpty(kellerUserId,notesId)){
            return Response.badRequest();
        }
        return Response.ok(service.getListByUserIdAndNotesId(kellerUserId,notesId));
    }

    /**
     * 创建笔记
     * @param kellerUserId
     * @param notesId
     * @param title
     * @param type
     * @return
     */
    @PostMapping
    public ResponseEntity create(Integer kellerUserId,Integer notesId,String title,Integer type){
        if(StringUtils.isEmpty(kellerUserId,notesId,title)){
            return Response.badRequest();
        }
        if(type == null){
            type = PublicConstant.NOTE_TYPE_RICH_TEXT;
        }else{
            if(type != PublicConstant.NOTE_TYPE_RICH_TEXT && type != PublicConstant.NOTE_TYPE_MARK_DOWN){
                return Response.badRequest();
            }
        }
        NoteInfo noteInfo = new NoteInfo(kellerUserId,notesId,title);
        noteInfo.setType(type);
        return Response.ok(service.createNote(noteInfo));
    }

    @PostMapping("/reSort")
    public ResponseEntity reSort(Integer kellerUserId,Integer noteId,Integer sort){
        if(StringUtils.isEmpty(kellerUserId,noteId,sort)){
            return Response.badRequest();
        }
        return Response.ok(service.reSort(kellerUserId,noteId,sort));
    }
}
