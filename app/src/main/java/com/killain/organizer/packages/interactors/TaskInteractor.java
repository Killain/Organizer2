package com.killain.organizer.packages.interactors;

import android.content.Context;

import com.killain.organizer.packages.models.SubTask;
import com.killain.organizer.packages.models.Task;

import java.util.ArrayList;

public class TaskInteractor {

    private Task task;
    private DataManager dataManager;

    public TaskInteractor(Context context) {
        task = new Task();
        dataManager = new DataManager(context, null);
    }

    public Task createTask(String task_text,
                           long task_date,
                           String task_time,
                           boolean task_is_notification_showed,
                           boolean task_completed,
                           ArrayList<SubTask> arrayList) {

        task.setTask_string(task_text);
        task.setDate(task_date);
        task.setTime(task_time);
        task.setNotificationShowed(task_is_notification_showed);
        task.setCompleted(task_completed);

        if (arrayList != null) {
            for (SubTask subTask : arrayList) {
                task.setHasReference(true);
                subTask.setReference(task.getTask_string());
                dataManager.addSubTask(subTask);
            }
        }

        return task;
    }

    public Task getCreatedTask() {
        return task;
    }
}
