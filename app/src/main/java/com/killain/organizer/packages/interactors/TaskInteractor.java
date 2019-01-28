package com.killain.organizer.packages.interactors;

import android.widget.EditText;

import com.killain.organizer.packages.models.Task;

public class TaskInteractor {
    private Task task;
    private EditText task_text;
    private String task_date;
    private String task_time;
    private boolean task_notification_showed;
    private boolean task_completed;
    private boolean task_reference;

    public TaskInteractor() {
        task = new Task();
    }

    public TaskInteractor(EditText task_text, String task_date,
                          String task_time,
                          boolean task_completed,
                          boolean task_notification_showed) {

        this.task_text = task_text;
        this.task_date = task_date;
        this.task_time = task_time;
        this.task_completed = task_completed;
        this.task_notification_showed = task_notification_showed;

        task = new Task();

        task.setTask_string(task_text.getText().toString());
        task.setTime(task_time);
        task.setDate(task_date);
        task.setCompleted(task_completed);
        task.setNotificationShowed(task_notification_showed);

    }


    public Task getTask() {
        return task;
    }

    public EditText getTask_text() {
        return task_text;
    }

    public void setTask_text(EditText task_text) {
        this.task_text = task_text;
        task.setTask_string(task_text.getText().toString());
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
        task.setDate(task_date);
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
        task.setTime(task_time);
    }

    public boolean isTask_notification_showed() {
        return task_notification_showed;
    }

    public void setTask_notification_showed(boolean task_notification_showed) {
        this.task_notification_showed = task_notification_showed;
        task.setNotificationShowed(task_notification_showed);
    }

    public boolean isTask_completed() {
        return task_completed;
    }

    public void setTask_completed(boolean task_completed) {
        this.task_completed = task_completed;
        task.setCompleted(task_completed);
    }

    public boolean isTask_reference() {
        return task_reference;
    }

    public void setTask_reference(boolean task_reference) {
        this.task_reference = task_reference;
        task.setHasReference(task_reference);
    }
}
