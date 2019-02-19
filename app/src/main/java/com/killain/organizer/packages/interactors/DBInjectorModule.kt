package com.killain.organizer.packages.interactors

import android.content.Context

import com.killain.organizer.packages.database.AppDatabase
import com.killain.organizer.packages.interfaces.SubTaskDAO
import com.killain.organizer.packages.interfaces.TaskDAO

import dagger.Module
import dagger.Provides
import org.jetbrains.annotations.Nullable

@Module
class DBInjectorModule(context: Context) {

    val db: AppDatabase = AppDatabase.getAppDatabase(context)!!
        @Provides
        get() = field

    val taskDAO: TaskDAO
        @Provides
        get() = field

    val subTaskDAO: SubTaskDAO
        @Provides
        get() = field

    init {
        taskDAO = db.taskDAO
        subTaskDAO = db.subTaskDAO
    }

//    @Provides
//    fun getDb() : AppDatabase? {
//        return db
//    }

//    @Provides
//    fun getTaskDao() : TaskDAO? {
//        return taskDAO
//    }
//
//    @Provides
//    fun getSubTaskDAO() : SubTaskDAO? {
//        return subTaskDAO
//    }
}
