package com.justdoit.keller.service;

import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.entity.NoteInfo;
import com.justdoit.keller.entity.NotesInfo;
import com.justdoit.keller.mapper.NoteMapper;
import com.justdoit.keller.mapper.NotesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public NoteInfo getByUserIdAndNoteId(int userId,int noteId){
        NoteInfo noteInfo = new NoteInfo(noteId);
        noteInfo = mapper.baseSelectById(noteInfo);
        if(noteInfo == null || userId != noteInfo.getUserId()){
            return null;
        }
        return noteInfo;
    }


}
