package br.edu.unidavi.todolist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = Task.class, version = 2)
@TypeConverters({ DateConverter.class })
public abstract class TasksStore extends RoomDatabase {

    public abstract TasksDao getTasksDao();

    private static TasksStore instance = null;

    public static TasksStore getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    TasksStore.class, "tasks.db")
                    .addMigrations(Migrations.FROM_1_TO_2)
                    .build();
        }
        return instance;
    }
}
