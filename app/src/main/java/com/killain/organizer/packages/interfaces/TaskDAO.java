package com.killain.organizer.packages.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.killain.organizer.packages.tasks.Task;
import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void addTask(Task... tasks);

    @Delete
    void deleteTask(Task... tasks);

    @Update
    void updateTask(Task... tasks);

    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    @Query("SELECT * FROM task WHERE task_date = :date")
    List<Task> getAllTasksByDate(String date);

    @Query("SELECT * FROM task WHERE task_is_completed == :isCompleted AND is_deleted == :isDeleted")
    List<Task> getAllTasksByState(boolean isCompleted, boolean isDeleted);

    //TODO: сделать сортировку
}
