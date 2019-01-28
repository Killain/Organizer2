package com.killain.organizer.packages.interactors;

import android.content.Context;

import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.interfaces.DBComponent;

import com.killain.organizer.packages.interfaces.DaggerDBComponent;
import com.killain.organizer.packages.interfaces.SubTaskDAO;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.models.SubTask;
import com.killain.organizer.packages.models.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DataManager {

    @Inject
    public AppDatabase db;

    @Inject
    public TaskDAO taskDAO;

    @Inject
    public SubTaskDAO subTaskDAO;

    private List<Task> taskList;

    private List<SubTask> subTaskList;
    private String date;
    private Context context;

    DBComponent component;

    public DataManager(Context context, ArrayList<Task> arrayList) {
        this.taskList = arrayList;
        this.context = context;
        component = DaggerDBComponent.builder().dBInjectorModule(new DBInjectorModule(context)).build();
        component.injectInManager(this);
    }

    public ArrayList<Task> getTasksByState(boolean isCompleted, boolean isDeleted) {
        taskList = taskDAO.getAllTasksByState(isCompleted, isDeleted);
        return (ArrayList<Task>) taskList;
    }

    public void updateTask (Task task) {
        taskDAO.updateTask(task);
    }

    public void addTask(Task task) {
        taskDAO.addTask(task);
    }

    public void addSubTask(SubTask subTask) {
        subTaskDAO.addSubTask(subTask);
    }

    public void updateSubTask(SubTask subTask) {
        subTaskDAO.updateSubTask(subTask);
    }

    public void deleteTask(Task task) {
        taskDAO.deleteTask(task);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<SubTask> getSubTasksByReference(String reference) {
        subTaskList = subTaskDAO.getSubTasksByReference(reference);
        return (ArrayList<SubTask>) subTaskList;
    }

    public ArrayList<Task> getAllTasksByDate(String date) {
        taskList = taskDAO.getAllTasksByDate(date);
        return (ArrayList<Task>) taskList;
    }
}
