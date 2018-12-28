package br.edu.unidavi.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText inputNewTask = findViewById(R.id.input_new_task);
                String value = inputNewTask.getText().toString();
                if (!value.isEmpty()) {
                    TasksStore.getInstance(getApplicationContext())
                            .getTasksDao().insert(new Task(value, false));
                    finish();
                }
            }
        });
    }
}
