package br.edu.unidavi.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class TaskDetailActivity extends AppCompatActivity {

    private Task task;
    private TasksViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        viewModel = ViewModelProviders.of(this)
                .get(TasksViewModel.class);
        viewModel.taskLiveData.observe(this, task -> {
            if (task != null) {
                this.task = task;
                setTitle(task.getTitle());
            }
        });

        viewModel.success.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                finish();
            }
        });

        int id = getIntent().getIntExtra("id", 0);
        viewModel.findTaskById(id);

        Button buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(v -> viewModel.deleteTask(task));

        Button buttonDone = findViewById(R.id.button_done);
        buttonDone.setOnClickListener(v -> viewModel.updateTask(new Task(task.getId(),
                task.getTitle(), true, task.getCreationDate())));
    }
}
