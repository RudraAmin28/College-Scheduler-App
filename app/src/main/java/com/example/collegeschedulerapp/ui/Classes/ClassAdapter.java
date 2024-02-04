package com.example.collegeschedulerapp.ui.Classes;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private List<Classes> classesList;


    public ClassAdapter(List<Classes> classList) {
        this.classesList = classList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Classes classy = classesList.get(position);
        holder.classTextView.setText(classy.getClassName());
        holder.professorTextView.setText(classy.getProfessor());

        // Displaying the start and end times
        String startTime = classy.getStartTime();
        String endTime = classy.getEndTime();
        String timeRange = (startTime != null && endTime != null) ? startTime + " - " + endTime : "N/A";
        holder.ClassTimeTextView.setText(timeRange);

        // Displaying the selected days
        List<String> selectedDays = classy.getSelectedDays();
        String days = (selectedDays != null && !selectedDays.isEmpty()) ? TextUtils.join(", ", selectedDays) : "N/A";
        holder.classDaysTextView.setText(days);
    }



    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView classTextView;
        public TextView professorTextView;
        public TextView ClassTimeTextView;
        public TextView classDaysTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            classTextView = itemView.findViewById(R.id.classNameTextView);
            professorTextView = itemView.findViewById(R.id.ClassInstructorTextView);
            ClassTimeTextView = itemView.findViewById(R.id.ClassTimeTextView);
            classDaysTextView = itemView.findViewById(R.id.classDaysTextView);

            Button buttonEdit = itemView.findViewById(R.id.editClass);
            buttonEdit.setOnClickListener(v -> {
                if (onEditButtonClickListener != null) {
                    onEditButtonClickListener.onEditButtonClick(getAdapterPosition());
                }
            });

            Button buttonDelete = itemView.findViewById(R.id.deleteClass);
            buttonDelete.setOnClickListener(v -> {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(getAdapterPosition());
                }
            });

        }
    }
}
