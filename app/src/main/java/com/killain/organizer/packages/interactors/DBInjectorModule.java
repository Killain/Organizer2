package com.killain.organizer.packages.interactors;

import android.content.Context;

import com.killain.organizer.packages.database.AppDatabase;
import com.killain.organizer.packages.interfaces.SubTaskDAO;
import com.killain.organizer.packages.interfaces.TaskDAO;

import dagger.Module;
import dagger.Provides;

@Module
public class DBInjectorModule {

    private AppDatabase db;
    private TaskDAO taskDAO;
    private Context context;
    private SubTaskDAO subTaskDAO;

    public DBInjectorModule(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
        taskDAO = db.getTaskDAO();
        subTaskDAO = db.getSubTaskDAO();
    }

    @Provides
    public AppDatabase getDb() {
        return db;
    }

    @Provides
    public TaskDAO getTaskDAO() {
        return taskDAO;
    }

    @Provides
    public SubTaskDAO getSubTaskDAO() {
        return subTaskDAO;
    }
}
