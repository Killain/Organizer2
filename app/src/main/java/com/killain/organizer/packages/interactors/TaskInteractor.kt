package com.killain.organizer.packages.interactors

import android.content.Context

import com.killain.organizer.packages.enums.DialogType
import com.killain.organizer.packages.models.SubTask
import com.killain.organizer.packages.models.Task

import java.util.ArrayList

class TaskInteractor(context: Context) {

    private val createdTask: Task? = Task()
    private val dataManager: DataManager? = DataManager(context)
    var subTaskArray: ArrayList<SubTask>? = null
    private val newArrayList: ArrayList<SubTask?> = ArrayList()

    fun createTask(taskText: String?,
                   taskDate: Long?,
                   taskTime: String?,
                   isNotificationShowed: Boolean?,
                   taskCompleted: Boolean?,
                   subTaskArray: ArrayList<SubTask>?,
                   dialogType: DialogType?): Task? {

        this.subTaskArray = subTaskArray

        createdTask?.task_string = taskText
        createdTask?.date = taskDate
        createdTask?.time = taskTime
        createdTask?.isNotificationShowed = isNotificationShowed
        createdTask?.isCompleted = taskCompleted

        if (dialogType == DialogType.ADD_NEW_TASK) {
            if (subTaskArray != null) {
                for (subTask in subTaskArray) {
                    createdTask?.isHasReference = true
                    subTask.reference = createdTask?.task_string
                    dataManager?.addSubTask(subTask)
                }
            }
        } else {
            if (subTaskArray != null) {
                newArrayList.addAll(subTaskArray)
                for (subTask in subTaskArray) {
                    dataManager?.deleteSubTask(subTask)
                }
                for (subTask in newArrayList) {
                    createdTask?.isHasReference = true
                    subTask?.reference = createdTask?.task_string
                    dataManager?.addSubTask(subTask)
                }
            }
        }

        return createdTask
    }
}
