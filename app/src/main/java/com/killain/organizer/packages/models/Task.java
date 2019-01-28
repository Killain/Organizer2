package com.killain.organizer.packages.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "task")
public class Task implements Cloneable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private int id;

    @ColumnInfo(name = "task_string")
    private String task_string;

    @ColumnInfo(name = "task_group_tag")
    private int group_tag;

    @ColumnInfo(name = "task_date")
    private String date;

    @ColumnInfo(name = "task_time")
    private String time;

    @ColumnInfo(name = "task_title")
    private String title;

    @ColumnInfo(name = "task_is_completed")
    private boolean isCompleted;

    @ColumnInfo(name = "is_notification_showed")
    private boolean isNotificationShowed;

    @ColumnInfo(name = "is_deleted")
    private boolean isDeleted;

    @ColumnInfo(name = "has_reference")
    private boolean hasReference;

    @ColumnInfo(name = "task_list_index")
    private int list_index;

    public Task() {
    }

    public int getGroup_tag() {
        return group_tag;
    }

    public String getTask_string() {
        return task_string;
    }

    public void setGroup_tag(int group_tag) {
        this.group_tag = group_tag;
    }

    public void setTask_string(String task_string) {
        this.task_string = task_string;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getList_index() {
        return list_index;
    }

    public void setList_index(int list_index) {
        this.list_index = list_index;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNotificationShowed() {
        return isNotificationShowed;
    }

    public void setNotificationShowed(boolean notificationShowed) {
        isNotificationShowed = notificationShowed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isHasReference() {
        return hasReference;
    }

    public void setHasReference(boolean hasReference) {
        this.hasReference = hasReference;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Task task = (Task) super.clone();
        return task;
    }
}