package br.edu.unidavi.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class NewTaskActivity extends AppCompatActivity {

    private TasksViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        viewModel = ViewModelProviders.of(this)
                .get(TasksViewModel.class);
        viewModel.success.observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                finish();
            }
        });

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(v -> {
            EditText inputNewTask = findViewById(R.id.input_new_task);
            String value = inputNewTask.getText().toString();
            if (!value.isEmpty()) {
                viewModel.saveTask(new Task(value));
            }
        });
    }
}
