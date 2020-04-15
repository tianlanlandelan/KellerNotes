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
import java.util.Date;


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

    /**
     * 修改笔记，可修改：标题、排序、所属笔记本
     * @param kellerUserId
     * @param noteId
     * @param notesId
     * @param title
     * @return
     */
    @PostMapping("/update")
    public ResponseEntity update(Integer kellerUserId,Integer noteId,Integer notesId,String title){
        if(StringUtils.isEmpty(kellerUserId,noteId) || StringUtils.noValue(notesId,title)){
            return Response.badRequest();
        }

        NoteInfo noteInfo = new NoteInfo(kellerUserId,notesId,title);
        noteInfo.setId(noteId);
        return Response.ok(service.update(noteInfo));
    }

    /**
     * 保存笔记内容
     * @param kellerUserId  必填
     * @param noteId        必填
     * @param text          纯文本内容
     * @param html          Html 内容
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity save(Integer kellerUserId,Integer noteId,String text,String html){
        if(StringUtils.isEmpty(kellerUserId,noteId) ){
            return Response.badRequest();
        }
        if(text == null){
            text = "";
        }
        if(html == null){
            html = "";
        }
        NoteInfo noteInfo = new NoteInfo(noteId);
        noteInfo.setUserId(kellerUserId);
        noteInfo.setText(text);
        noteInfo.setHtml(html);
        return Response.ok(service.save(noteInfo));
    }


    /**
     * 获取笔记详情
     * @param kellerUserId  从 JWT 中解析到的用户 ID
     * @param noteId    用户的笔记 ID
     * @return
     */
    @GetMapping
    public ResponseEntity get(Integer kellerUserId,Integer noteId){
        if(StringUtils.isEmpty(kellerUserId,noteId)){
            return Response.badRequest();
        }
        return Response.ok(service.get(kellerUserId,noteId));
    }

    @PostMapping("/del")
    public ResponseEntity del(Integer kellerUserId,Integer noteId){
        if(StringUtils.isEmpty(kellerUserId,noteId)){
            return Response.badRequest();
        }
        return Response.ok(service.del(kellerUserId,noteId));
    }
}
