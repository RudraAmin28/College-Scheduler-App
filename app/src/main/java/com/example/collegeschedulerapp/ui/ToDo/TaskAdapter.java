package com.example.collegeschedulerapp.ui.ToDo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;

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

    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }

    private static OnEditButtonClickListener onEditButtonClickListener;

    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.onEditButtonClickListener = listener;
    }

    private static OnDeleteButtonClickListener onDeleteButtonClickListener;

    public void setOnDeleteButtonClickListener(OnDeleteButtonClickListener listener) {
        this.onDeleteButtonClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.titleTextView.setText(task.getTitle());
        if (task.isChecked()) {
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.categoryTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.categoryTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (task.getCategory() == null || task.getCategory().isEmpty()) {
            holder.categoryTextView.setText("Category: N/A");
        } else {
            holder.categoryTextView.setText("Category: " + task.getCategory());
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isChecked());

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView categoryTextView;
        public CheckBox checkBox;
        public Button buttonEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitle);
            categoryTextView = itemView.findViewById(R.id.taskCategory);
            checkBox = itemView.findViewById(R.id.checkBoxTODO);
            buttonEdit = itemView.findViewById(R.id.editTODO);

            buttonEdit.setOnClickListener(v -> {
                if (onEditButtonClickListener != null) {
                    onEditButtonClickListener.onEditButtonClick(getAdapterPosition());
                }
            });

            Button buttonDelete = itemView.findViewById(R.id.deleteTODO);
            buttonDelete.setOnClickListener(v -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(getAdapterPosition());
                }
            });

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckboxChanged(getAdapterPosition(), isChecked);
                }
            });
        }
    }
}
