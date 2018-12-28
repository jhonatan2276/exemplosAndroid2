package br.edu.unidavi.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Tasks.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tasks(" +
                "_id integer primary key autoincrement," +
                "title text," +
                "done boolean" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTask(String title) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("done", false);
        db.insert("tasks", null, values);
    }

    public List<Task> fetchTasks() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("tasks", new String[] {
                "_id", "title", "done"
        }, null, null, null, null, null);
        cursor.moveToFirst();
        List<Task> tasks = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Task task = new Task(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getInt(cursor.getColumnIndex("done")) == 1
            );
            tasks.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return tasks;
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("tasks", "_id=" + task.getId(),
                null);
    }

    public void markTaskAsDone(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("done", true);
        db.update("tasks", values, "_id=" + task.getId(),
                null);
    }
}
