package com.justdoit.keller;

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

}
