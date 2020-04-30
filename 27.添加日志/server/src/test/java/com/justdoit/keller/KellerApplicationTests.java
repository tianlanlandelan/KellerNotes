package com.justdoit.keller;

import com.justdoit.keller.common.config.PublicConstant;
import com.justdoit.keller.common.mybatis.annotation.TableAttribute;
import com.justdoit.keller.common.util.StringUtils;
import com.justdoit.keller.entity.*;
import com.justdoit.keller.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class KellerApplicationTests {


    @Resource
    private UserMapper userMapper;

    @Test
    public void createUserInfoTable(){
        userMapper.baseCreate(new UserInfo());
    }

    @Resource
    private EmailMapper emailMapper;

    @Test
    public void createEmailTable(){
        emailMapper.baseCreate(new EmailLog());
    }

    @Resource
    private UserCardMapper userCardMapper;

    @Test
    public void createUserCardTable(){
        userCardMapper.baseCreate(new UserCard());
    }

    @Resource
    private NotesMapper notesMapper;
    @Test
    public void createNotesTable(){
        notesMapper.baseCreate(new NotesInfo());
    }


    @Resource
    private NoteMapper noteMapper;
    @Test
    public void createNoteTable(){
        noteMapper.baseCreate(new NoteInfo());
    }

    @Resource
    private RegisterLogMapper registerLogMapper;

    @Test
    public void createRegisterLogTable(){
        registerLogMapper.baseCreate(new RegisterLog());
    }


    @Test
    public void insetUserInfo(){
        UserInfo userInfo = new UserInfo();
        for(int i = 0 ; i < 900 ; i ++){
            userInfo.setEmail(StringUtils.getAllCharString(20));
            userInfo.setPassword("test");
            userInfo.setType(PublicConstant.DEFAULT_USER_TYPE);
            userMapper.baseInsertAndReturnKey(userInfo);
        }
    }

}
