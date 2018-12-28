package br.edu.unidavi.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TaskDetailActivity extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        int id = getIntent().getIntExtra("id", 0);
        task = TasksStore.getInstance(this).getTasksDao().findById(id);
        setTitle(task.getTitle());

        Button buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksStore.getInstance(getApplicationContext())
                        .getTasksDao()
                        .delete(task);
                finish();
            }
        });

        Button buttonDone = findViewById(R.id.button_done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TasksStore.getInstance(getApplicationContext())
                        .getTasksDao()
                        .update(new Task(task.getId(),
                                task.getTitle(), true));
                finish();
            }
        });
    }
}
