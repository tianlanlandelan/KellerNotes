package com.keller.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yangkaile
 * @date 2020-05-19 08:23:34
 * 笔记服务，提供笔记相关的服务
 *  包括笔记、笔记本的增删改查和数据统计等
 */
@SpringBootApplication
public class NoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteApplication.class, args);
    }

}
