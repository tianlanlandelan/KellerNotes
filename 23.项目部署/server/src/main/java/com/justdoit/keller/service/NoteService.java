package com.justdoit.keller.service;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.entity.NoteInfo;
import com.justdoit.keller.entity.NotesInfo;
import com.justdoit.keller.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author yangkaile
 * @date 2020-04-05 10:35:26
 */
@Service
public class NoteService {

    @Resource
    private NoteMapper mapper;

    @Resource
    private NotesService notesService;

    /**
     * 获取用户笔记本中的笔记列表
     * 不返回每个笔记的明细，只返回标题、类型等
     * @param userId
     * @param notesId
     * @return
     */
    public ResultData getListByUserIdAndNotesId(int userId, int notesId){
        NotesInfo notesInfo = notesService.getByIdAndUserId(notesId,userId);
        if(notesInfo == null){
            return ResultData.error("笔记本不存在");
        }
        NoteInfo noteInfo = new NoteInfo(userId,notesId);
        noteInfo.setBaseKyleUseAnd(true);
        List<NoteInfo> list = mapper.baseSelectByCondition(noteInfo);
        return ResultData.success(list);
    }

    /**
     * 创建笔记
     * @param noteInfo
     * @return
     */
    public ResultData createNote(NoteInfo noteInfo){
        NotesInfo notesInfo = notesService.getByIdAndUserId(noteInfo.getNotesId(),noteInfo.getUserId());
        if(notesInfo == null){
            return ResultData.error("笔记本不存在");
        }

        Integer result = mapper.baseInsertAndReturnKey(noteInfo);
        if(result == 1){
            notesService.addNoteCount(notesInfo);
            return ResultData.success(noteInfo);
        }else {
            return ResultData.error("创建笔记本失败");
        }
    }

    public ResultData reSort(int userId,int noteId,int sort){
        NoteInfo noteInfo = getByUserIdAndNoteId(userId,noteId);
        if(noteInfo == null){
            return ResultData.error("笔记不存在");
        }
        noteInfo.setSort(sort);
        mapper.baseUpdateById(noteInfo);
        return ResultData.success();
    }

    /**
     * 修改笔记，可修改：标题、排序、所属笔记本
     * @param noteInfo
     * @return
     */
    public ResultData update(NoteInfo noteInfo){

        NoteInfo result = getByUserIdAndNoteId(noteInfo.getUserId(),noteInfo.getId());
        if(result == null){
            return ResultData.error("笔记不存在");
        }

        /*
         * 如果设置了笔记本 ID ，且和原笔记本 ID 不同，表示要移动笔记本
         * 此时，校验要移动到的笔记本是不是用户自己的笔记本
         * 校验成功后移动笔记本，并修改原笔记本和新笔记本的笔记数量
         */
        if(noteInfo.getNotesId() != null && !noteInfo.getNotesId().equals(result.getNotesId())){
            NotesInfo newNotes = notesService.getByIdAndUserId(noteInfo.getNotesId(),noteInfo.getUserId());
            NotesInfo orderNotes= notesService.getByIdAndUserId(result.getNotesId(),result.getUserId());
            if(newNotes == null || orderNotes == null){
                return ResultData.error("笔记本不存在");
            }
            result.setNotesId(noteInfo.getNotesId());
            if(mapper.baseUpdateById(result) == 1){
                notesService.decrNoteCount(orderNotes);
                notesService.addNoteCount(newNotes);
            }
        }
        result.setSort(noteInfo.getSort());
        result.setTitle(noteInfo.getTitle());
        mapper.baseUpdateById(result);

        return ResultData.success();
    }

    public ResultData save(NoteInfo noteInfo){
        NoteInfo result = getByUserIdAndNoteId(noteInfo.getUserId(),noteInfo.getId());
        if(result == null){
            return ResultData.error("笔记本不存在");
        }
        result.setText(noteInfo.getText());
        result.setHtml(noteInfo.getHtml());
        result.setUpdateUserID(noteInfo.getUserId());
        result.setUpdateTime(new Date());
        mapper.baseUpdateById(result);

        return  ResultData.success();
    }


    public ResultData get(int userId,int noteId){
        NoteInfo noteInfo = getByUserIdAndNoteId(userId, noteId);
        if(noteInfo == null){
            return ResultData.error("笔记本不存在");
        }
        return ResultData.success(noteInfo);
    }

    public ResultData del(int userId,int noteId){
        NoteInfo noteInfo = getByUserIdAndNoteId(userId, noteId);
        if(noteInfo == null){
            return ResultData.error("笔记本不存在");
        }
        mapper.baseDeleteById(noteInfo);
        return ResultData.success();
    }

    public NoteInfo getByUserIdAndNoteId(int userId,int noteId){
        NoteInfo noteInfo = new NoteInfo(noteId);
        noteInfo = mapper.baseSelectById(noteInfo);
        if(noteInfo == null || userId != noteInfo.getUserId()){
            return null;
        }
        return noteInfo;
    }


}
