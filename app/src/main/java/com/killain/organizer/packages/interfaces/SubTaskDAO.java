package com.killain.organizer.packages.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.killain.organizer.packages.models.SubTask;

import java.util.List;

@Dao
public interface SubTaskDAO {

    @Insert
    void addSubTask(SubTask... tasks);

    @Delete
    void deleteSubTask(SubTask... tasks);

    @Update
    void updateSubTask(SubTask... tasks);

    @Query("SELECT * FROM subtask")
    List<SubTask> getAllSubTasks();

    @Query("SELECT * FROM subtask WHERE subtask_reference = :reference")
    List<SubTask> getSubTasksByReference(String reference);

//    @Query("SELECT * FROM task WHERE subtask_date = :date")
//    List<SubTask> getAllSubTasksByDate(String date);

//    @Query("SELECT * FROM task WHERE isCompleted == :state")
//    List<SubTask> getAllTasksByState(boolean state);
}
