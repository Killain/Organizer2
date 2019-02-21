package com.killain.organizer.packages.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.killain.organizer.packages.models.Task

@Dao
interface TaskDAO {

//    @get:Query("SELECT * FROM task")
//    val allTasks: MutableList<Task>

    @Query("SELECT * FROM task")
    fun getAllTasks() : MutableList<Task>

//    @get:Query("SELECT * FROM task WHERE is_deleted == 0")
//    val undeletedTasks: MutableList<Task>

    @Query("SELECT * FROM task WHERE is_deleted == 0")
    fun getUndeletedTasks() : MutableList<Task>

    @Insert
    fun addTask(vararg tasks: Task?)

    @Delete
    fun deleteTask(vararg tasks: Task?)

    @Update
    fun updateTask(vararg tasks: Task?)

    @Query("UPDATE task SET task_string = :text_string, " +
            "task_date = :task_date, task_time = :time, " +
            "task_is_completed = :isCompleted, is_notification_showed = :isNotificationShowed, " +
            "is_deleted = :isDeleted, has_reference = :hasReference WHERE task_string = :old_text")
    fun customUpdateTask(text_string: String?,
                         task_date: Long?, time: String?,
                         isCompleted: Boolean?, isNotificationShowed: Boolean?,
                         isDeleted: Boolean?, hasReference: Boolean?, old_text: String?)

    @Query("SELECT * FROM task WHERE task_date = :date AND task_is_completed == 0 AND is_deleted == 0")
    fun getAllTasksByDate(date: Long): List<Task>?

    @Query("SELECT * FROM task WHERE task_is_completed == :isCompleted AND is_deleted == :isDeleted")
    fun getAllTasksByState(isCompleted: Boolean, isDeleted: Boolean): List<Task>?
}
