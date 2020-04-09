package com.justdoit.keller.service;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.entity.NotesInfo;
import com.justdoit.keller.mapper.NotesMapper;
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
        mapper.baseDeleteById(notesInfo);
        return ResultData.success();
    }


    /**
     * 获取用户的笔记本列表
     * @param userId
     * @return
     */
    public ResultData getListByUser(int userId){
        NotesInfo notesInfo = new NotesInfo();
        notesInfo.setUserId(userId);
        notesInfo.setBaseKyleUseASC(true);
        List<NotesInfo> list = mapper.baseSelectByCondition(notesInfo);
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
        NotesInfo notesInfo = new NotesInfo(userId,PublicConstant.DEFAULT_NOTES_NAME);
        notesInfo.setStatus(PublicConstant.DEFAULT_NOTES_STATUS);
        mapper.baseInsertAndReturnKey(notesInfo);
        return notesInfo;
    }
}
