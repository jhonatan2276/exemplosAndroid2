package br.edu.unidavi.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TasksAdapter adapter = new TasksAdapter(new TasksAdapter.OnTaskClickListener() {
        @Override
        public void onClick(Task task) {
            Intent intent = new Intent(getApplicationContext(), TaskDetailActivity.class);
            intent.putExtra("id", task.getId());
            startActivity(intent);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView taskList = findViewById(R.id.task_list);
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(adapter);

        FloatingActionButton buttonCreate = findViewById(R.id.button_create);
        buttonCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        NewTaskActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> tasks = TasksStore.getInstance(this).getTasksDao().fetchTasks();
        adapter.setup(tasks);
    }
}
