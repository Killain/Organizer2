package com.killain.organizer.packages.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.killain.organizer.packages.interfaces.SubTaskDAO;
import com.killain.organizer.packages.interfaces.TaskDAO;
import com.killain.organizer.packages.tasks.SubTask;
import com.killain.organizer.packages.tasks.Task;

@Database(entities = {Task.class, SubTask.class}, version = 2, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract TaskDAO getTaskDAO();
    public abstract SubTaskDAO getSubTaskDAO();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'task' "
                    + " ADD COLUMN 'has_reference' INTEGER");
        }
    };

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            buildDatabase(context);
//            INSTANCE = Room.databaseBuilder(context,
//                    AppDatabase.class,
//                    "app-database")
//                            // allow queries on the main thread.
//                            // Don't do this on a real app! See PersistenceBasicSample for an example.
//                            .allowMainThreadQueries()
//                            .addMigrations(MIGRATION_1_2)
//                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static void buildDatabase(Context context) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class, "organizer-database")
                    .allowMainThreadQueries()
//                    .addMigrations(MIGRATION_1_2)
                    .build();
    }
}
