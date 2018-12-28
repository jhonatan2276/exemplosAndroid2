package br.edu.unidavi.todolist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private TasksViewModel viewModel;

    private TasksAdapter adapter = new TasksAdapter(task -> {
        Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
        intent.putExtra("id", task.getId());
        startActivity(intent);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this)
                .get(TasksViewModel.class);
        viewModel.tasks.observe(this, tasks -> {
            if (tasks != null) {
                adapter.setup(tasks);
            }
        });

        RecyclerView taskList = findViewById(R.id.task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(adapter);

        FloatingActionButton buttonCreate = findViewById(R.id.button_create);
        buttonCreate.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),
                NewTaskActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.fetchTasks();
    }
}
