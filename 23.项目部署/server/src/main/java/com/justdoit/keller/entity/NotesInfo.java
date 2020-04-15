package com.justdoit.keller.entity;

import com.justdoit.keller.common.mybatis.BaseEntity;
import com.justdoit.keller.common.mybatis.annotation.*;

import java.util.Date;

/**
 * @date 2020-03-31 15:07:17
 * @author yangkaile
 * 笔记本实体类
 */
@TableAttribute(name = "notes_info",comment = "笔记本详情")
public class NotesInfo extends BaseEntity {
    @FieldAttribute
    @KeyAttribute(autoIncr = true)
    private Integer id;

    @FieldAttribute("用户Id")
    @IndexAttribute
    private Integer userId;

    @FieldAttribute(value = "笔记本标题",length = 50,notNull = true)
    private String title;

    @FieldAttribute(value = "笔记本副标题",length = 200)
    private String subTitle;

    @FieldAttribute("笔记本中的笔记总数")
    private int noteCount;

    @FieldAttribute("排序")
    @SortAttribute
    private Integer sort;

    @FieldAttribute("创建时间")
    private Date createTime = new Date();

    @FieldAttribute("是否已删除，0 正常，1 已删除")
    private int isDelete;

    @FieldAttribute("最后修改时间")
    private Date updateTime;

    @FieldAttribute("最后修改人")
    private Integer updateUserId;

    @FieldAttribute("笔记本状态 1 默认笔记本")
    private int status;

    public NotesInfo() {
    }

    public NotesInfo(Integer id) {
        this.id = id;
    }

    public NotesInfo(Integer userId, String title) {
        this.userId = userId;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(int noteCount) {
        this.noteCount = noteCount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotesInfo{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", noteCount=" + noteCount +
                ", sort=" + sort +
                ", createTime=" + createTime +
                ", isDelete=" + isDelete +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                ", status=" + status +
                '}';
    }
}
