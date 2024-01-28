package com.example.collegeschedulerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> taskList;

    public interface OnCheckedChangeListener {
        void onCheckboxChanged(int position, boolean isChecked);
    }

    private static OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }



    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    private static OnDeleteButtonClickListener onDeleteButtonClickListener;

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());

        // Set the checkbox state without triggering the listener
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isChecked());

        // Set click listener for the checkbox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckboxChanged(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.todoTitle);
            checkBox = itemView.findViewById(R.id.checkBoxTODO);
            Button buttonDelete = itemView.findViewById(R.id.deleteTODO);
            buttonDelete.setOnClickListener(v -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(getAdapterPosition());
                }
            });

            // Set click listener for the checkbox
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckboxChanged(getAdapterPosition(), isChecked);
                }
            });
        }
    }
}
