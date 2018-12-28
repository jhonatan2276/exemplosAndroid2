package br.edu.unidavi.todolist;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private final OnTaskClickListener listener;
    private List<Task> tasks = new ArrayList<>();

    public TasksAdapter(OnTaskClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Task task = tasks.get(i);
        viewHolder.title.setText(task.getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(task);
            }
        });
        if (task.isDone()) {
            viewHolder.title.setTextColor(Color.RED);
        } else {
            viewHolder.title.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setup(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
        }
    }

    interface OnTaskClickListener {
        void onClick(Task task);
    }
}
