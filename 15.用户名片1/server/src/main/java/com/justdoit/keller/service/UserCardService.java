package com.justdoit.keller.service;

import com.justdoit.keller.common.response.ResultData;
import com.justdoit.keller.common.util.DateUtils;
import com.justdoit.keller.common.util.FileUtils;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.UserCard;
import com.justdoit.keller.mapper.UserCardMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class UserCardService {
    @Resource
    private UserCardMapper mapper;

    /**
     * 设置用户名片，只设置有值的字段，当用户名片不存在时创建名片
     * @param userId
     * @param nickName
     * @param email
     * @param label
     * @return
     */
    public ResultData setUserCard(int userId,String nickName,String email,String label){
        UserCard userCard = new UserCard(userId);
        UserCard result = mapper.baseSelectByKey(userCard);
        //标示是否是更新操作
        boolean update = true;
        //如果没查到用户名片，新建一个用户名片
        if(result == null){
            result = userCard;
            update = false;
        }
        result.setNickName(nickName);
        result.setEmail(email);
        result.setLabel(label);

        if(update){
            mapper.baseUpdateByKey(result);
        }else {
            mapper.baseInsert(result);
        }
        return ResultData.success();
    }

    /**
     * 获取用户名片
     * @param userId
     * @return
     */
    public ResultData getUserCard(int userId){
        UserCard userCard = new UserCard(userId);
        userCard = mapper.baseSelectByKey(userCard);
        if(userCard == null){
            return ResultData.error("用户名片不存在");
        }
        String imgName = userCard.getPortraitName();
        if(StringUtils.isNotEmpty(imgName)){
            userCard.setProtraitUrl(FileUtils.getImgUrl(imgName));
            userCard.setProtraitThumUrl(FileUtils.getThumUrl(imgName));
        }

        return ResultData.success(userCard);
    }

    /**
     * 设置用户头像
     * @param file
     * @param userId
     * @return
     */
    public ResultData setPortrait(MultipartFile file,int userId){
        String timeMask = DateUtils.getTimeMask();
        String originalFilename = file.getOriginalFilename();
        String fileName;
        try {
            fileName = FileUtils.saveImgAndThum(file,timeMask);
        }catch (Exception e){
            e.printStackTrace();
            return ResultData.error("头像保存失败");
        }
        UserCard userCard = new UserCard(userId);
        userCard.setPortraitOriginName(originalFilename);
        userCard.setPortraitName(fileName);
        int result = mapper.baseUpdateByKey(userCard);
        if(result > 0){
            return ResultData.success();
        }
        return ResultData.error("用户名片不存在");
    }


}
