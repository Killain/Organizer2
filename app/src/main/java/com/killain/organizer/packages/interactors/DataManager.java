package com.killain.organizer.packages.interactors;

import android.content.Context;

import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.interfaces.DBComponent;

import com.killain.organizer.packages.interfaces.DaggerDBComponent;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DataManager {

    @Inject
    public AppDatabase db;

    @Inject
    public TaskDAO taskDAO;

    private List<Task> arrayList;
    private String date;
    private Context context;

    DBComponent component;

    public DataManager(Context context, ArrayList<Task> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        component = DaggerDBComponent.builder().dBInjectorModule(new DBInjectorModule(context)).build();
        component.injectInManager(this);
    }

    public ArrayList<Task> getTasksByState(boolean state) {
        arrayList = taskDAO.getAllTasksByState(state);
        return (ArrayList<Task>) arrayList;
    }

    public ArrayList<Task> getTasksByDate() {
        arrayList = taskDAO.getAllTasksByDate(date);
        return (ArrayList<Task>) arrayList;
    }

    public void updateTask (Task task) {
        taskDAO.updateTask(task);
    }

    public void addTask(Task task) {
        taskDAO.addTask(task);
    }

    public void deleteTask(Task task) {
        taskDAO.deleteTask(task);
    }

    public void setDate(String date) {
        this.date = date;
    }
}
