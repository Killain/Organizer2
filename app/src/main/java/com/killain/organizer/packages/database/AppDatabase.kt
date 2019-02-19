package com.killain.organizer.packages.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import android.content.Context

import com.killain.organizer.packages.interfaces.SubTaskDAO
import com.killain.organizer.packages.interfaces.TaskDAO
import com.killain.organizer.packages.models.SubTask
import com.killain.organizer.packages.models.Task
import org.jetbrains.annotations.Nullable

@Database(entities = [Task::class, SubTask::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val taskDAO: TaskDAO
    abstract val subTaskDAO: SubTaskDAO

    companion object {

        private var INSTANCE: AppDatabase? = null

        internal val MIGRATION_1_2: Migration = object : Migration(1, 2) {

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'task' " + " ADD COLUMN 'has_reference' INTEGER")
            }
        }

        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                buildDatabase(context)
                // INSTANCE = Room.databaseBuilder(context,
                //         AppDatabase.class,
                //         "app-database")
                //                 // allow queries on the main thread.
                //                 // Don't do this on a real app! See PersistenceBasicSample for an example.
                //                 .allowMainThreadQueries()
                //                 .addMigrations(MIGRATION_1_2)
                //                 .build();
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        private fun buildDatabase(context: Context) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase::class.java, "organizer-database")
                    //.addMigrations(MIGRATION_1_2)
                    .build()
        }
    }
}
