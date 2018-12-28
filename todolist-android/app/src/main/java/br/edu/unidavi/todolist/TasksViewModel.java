package br.edu.unidavi.todolist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {

    public final MutableLiveData<List<Task>> tasks =
            new MutableLiveData<>();
    public final MutableLiveData<Task> taskLiveData =
            new MutableLiveData<>();
    public final MutableLiveData<Boolean> success =
            new MutableLiveData<>();

    public TasksViewModel(@NonNull Application application) {
        super(application);
    }

    public void fetchTasks() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                tasks.postValue(TasksStore
                        .getInstance(getApplication())
                        .getTasksDao().fetchTasks());
                return null;
            }
        }.execute();
    }

    public void findTaskById(final int id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Task task = TasksStore.getInstance(getApplication())
                        .getTasksDao().findById(id);
                taskLiveData.postValue(task);
                return null;
            }
        }.execute();
    }

    public void saveTask(final Task task) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                TasksStore.getInstance(getApplication())
                        .getTasksDao()
                        .insert(task);
                success.postValue(true);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final Task task) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                TasksStore.getInstance(getApplication())
                        .getTasksDao()
                        .delete(task);
                success.postValue(true);
                return null;
            }
        }.execute();
    }

    public void updateTask(final Task task) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                TasksStore.getInstance(getApplication())
                        .getTasksDao()
                        .update(task);
                success.postValue(true);
                return null;
            }
        }.execute();
    }
}
