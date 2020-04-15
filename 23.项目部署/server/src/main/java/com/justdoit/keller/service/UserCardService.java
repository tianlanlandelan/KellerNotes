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
     * 设置用户名片，只设置有值的字段
     * 用户名片时在注册时创建的，不能再次创建
     * @param userId
     * @param nickName
     * @param email
     * @param label
     * @return
     */
    public ResultData setUserCard(int userId,String nickName,String email,String label){
        UserCard userCard = new UserCard(userId);
        //如果没查到用户名片，新建一个用户名片
        userCard.setNickName(nickName);
        userCard.setEmail(email);
        userCard.setLabel(label);
        int result = mapper.baseUpdateByKey(userCard);
        if(result < 1){
            return ResultData.error("用户名片不存在");
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
        if(StringUtils.noEmpty(imgName)){
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
        //在注册时，会出现先上传用户头像后设置用户昵称的情况
        int result = mapper.baseUpdateByKey(userCard);
        if(result == 0){
            mapper.baseInsert(userCard);
        }
        return ResultData.success();
    }


}
