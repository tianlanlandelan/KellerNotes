package com.keller.note.service;

import com.keller.common.config.Constants;
import com.keller.common.http.ResultData;
import com.keller.note.entity.NotesInfo;
import com.keller.note.mapper.NotesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * 笔记本逻辑处理
 *
 * @author yangkaile
 * @date 2020-03-31 15:18:30
 *
 */

@Service
public class NotesService {
    @Resource
    private NotesMapper mapper;

    /**
     * 保存笔记本
     * @param notesInfo
     * @return
     */
    public ResultData save(NotesInfo notesInfo){
        if (notesInfo.getId() == null){
            mapper.baseInsertAndReturnKey(notesInfo);
            return ResultData.success(notesInfo);
        }

        // 校验该笔记本是否是该用户的
        NotesInfo result = getByIdAndUserId(notesInfo.getId(),notesInfo.getUserId());
        if(result == null){
            return ResultData.error("笔记本不存在");
        }

        mapper.baseUpdateById(notesInfo);
        return ResultData.success();
    }

    public ResultData deleteById(int userId,int id){
        NotesInfo notesInfo =getByIdAndUserId(id,userId);
        if(notesInfo == null){
            return ResultData.error("笔记本不存在");
        }
        if(notesInfo.getStatus() == Constants.DEFAULT_NOTES_STATUS){
            return ResultData.error("用户默认笔记本不可删除");
        }
        mapper.baseDeleteById(notesInfo);
        return ResultData.success();
    }


    /**
     * 获取用户的笔记本列表,如果用户没有笔记本，创建默认笔记本
     * @param userId
     * @return
     */
    public ResultData getListByUser(int userId){
        NotesInfo notesInfo = new NotesInfo();
        notesInfo.setUserId(userId);
        notesInfo.setBaseKyleUseASC(true);
        List<NotesInfo> list = mapper.baseSelectList(notesInfo);

        if(list == null || list.size() < 1){
            notesInfo.setStatus(Constants.DEFAULT_NOTES_STATUS);
            notesInfo.setTitle(Constants.DEFAULT_NOTES_NAME);
            mapper.baseInsertAndReturnKey(notesInfo);
            list.add(notesInfo);
        }
        return ResultData.success(list);
    }

    public NotesInfo getByIdAndUserId(int id,int userId){
        NotesInfo notesInfo = new NotesInfo(id);
        notesInfo = mapper.baseSelectById(notesInfo);
        if(notesInfo == null){
            return null;
        }
        if(userId != notesInfo.getUserId()){
            return null;
        }
        return notesInfo;
    }

    public void addNoteCount(NotesInfo notesInfo){
        int count = notesInfo.getNoteCount();
        notesInfo.setNoteCount(++count < 0 ? 0 :count);
        mapper.baseUpdateById(notesInfo);
    }

    public void decrNoteCount(NotesInfo notesInfo){
        int count = notesInfo.getNoteCount();
        notesInfo.setNoteCount(--count < 0 ? 0 :count);
        mapper.baseUpdateById(notesInfo);
    }

    public NotesInfo insertDefaultNotes(int userId){
        NotesInfo notesInfo = new NotesInfo(userId,Constants.DEFAULT_NOTES_NAME);
        notesInfo.setStatus(Constants.DEFAULT_NOTES_STATUS);
        mapper.baseInsertAndReturnKey(notesInfo);
        return notesInfo;
    }
}
