package br.edu.unidavi.todolist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Task.class, version = 1)
public abstract class TasksStore extends RoomDatabase {

    public abstract TasksDao getTasksDao();

    private static TasksStore instance = null;

    public static TasksStore getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context,
                    TasksStore.class, "tasks.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
