package com.example.collegeschedulerapp.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;
import com.example.collegeschedulerapp.R;

public class ClassworkAdapter extends RecyclerView.Adapter<ClassworkAdapter.ViewHolder> {

    private Context context;
    private List<Classwork> classworkList;

    public ClassworkAdapter(Context context, List<Classwork> classworkList) {
        this.context = context;
        this.classworkList = classworkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popup_add_classwork, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(classworkList.get(position));
    }

    @Override
    public int getItemCount() {
        return classworkList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewType;
        private TextView textViewDueDate;
        private TextView textViewClass;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewClassworkTitle);
            textViewType = itemView.findViewById(R.id.textViewClassworkType);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            textViewClass = itemView.findViewById(R.id.textViewClass);
        }

        public void bind(Classwork classwork) {
            // Bind data to the TextViews
            textViewTitle.setText(classwork.getName());
            textViewType.setText("Type: " + classwork.getClassworkType());
            textViewDueDate.setText("Due Date: " + formatDate(classwork.getDueDateInMillis()));
            textViewClass.setText("Class: " + classwork.getAssociatedClass());
        }

        private String formatDate(long millis) {
            // Implement a date formatting logic as needed
            // You can use SimpleDateFormat or other date formatting methods
            return "Formatted Date";
        }
    }
}