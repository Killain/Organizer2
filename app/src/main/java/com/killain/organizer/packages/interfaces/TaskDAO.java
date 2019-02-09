package com.killain.organizer.packages.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.killain.organizer.packages.models.Task;
import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void addTask(Task... tasks);

    @Delete
    void deleteTask(Task... tasks);

    @Update
    void updateTask(Task... tasks);

    @Query("UPDATE task SET task_string = :text_string, " +
            "task_date = :task_date, task_time = :time, " +
            "task_is_completed = :isCompleted, is_notification_showed = :isNotificationShowed, " +
            "is_deleted = :isDeleted, has_reference = :hasReference WHERE task_string = :old_text"
        )
    void customUpdateTask(String text_string,
                          long task_date, String time,
                          boolean isCompleted, boolean isNotificationShowed,
                          boolean isDeleted, boolean hasReference, String old_text);

    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    @Query("SELECT * FROM task WHERE task_date = :date AND task_is_completed == 0 AND is_deleted == 0")
    List<Task> getAllTasksByDate(long date);

    @Query("SELECT * FROM task WHERE task_is_completed == :isCompleted AND is_deleted == :isDeleted")
    List<Task> getAllTasksByState(boolean isCompleted, boolean isDeleted);

    @Query("SELECT * FROM task WHERE is_deleted == 0")
    List<Task> getUndeletedTasks();

    //TODO: сделать сортировку
}
