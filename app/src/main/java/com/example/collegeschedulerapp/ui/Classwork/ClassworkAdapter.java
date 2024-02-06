package com.example.collegeschedulerapp.ui.Classwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClassworkAdapter extends RecyclerView.Adapter<ClassworkAdapter.ViewHolder> {
    private Context context;
    private List<Classwork> classworkList;
    private OnClassworkEditListener editListener;
    private OnClassworkDeleteListener deleteListener;


    public interface OnClassworkEditListener {
        void onEditClasswork(Classwork classwork);
    }

    public void setOnClassworkEditListener(OnClassworkEditListener listener) {
        this.editListener = listener;
    }

    public interface OnClassworkDeleteListener {
        void onDeleteClasswork(int position);
    }

    public void setOnClassworkDeleteListener(OnClassworkDeleteListener listener) {
        this.deleteListener = listener;
    }

    public ClassworkAdapter(Context context, List<Classwork> classworkList) {
        this.context = context;
        this.classworkList = classworkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.classwork_item_layout, parent, false);
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
        private TextView textViewClass;
        private TextView textViewDueDate;
        private Button editButton;
        private Button deleteButton;
        private TextView locationTextView;
        private TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewClassworkTitle);
            textViewType = itemView.findViewById(R.id.textViewClassworkType);
            textViewClass = itemView.findViewById(R.id.textViewClass);
            textViewDueDate = itemView.findViewById(R.id.textViewDueDate);
            editButton = itemView.findViewById(R.id.editClasswork);
            deleteButton = itemView.findViewById(R.id.deleteClasswork);
            locationTextView = itemView.findViewById(R.id.LocationTextView);
            timeTextView = itemView.findViewById(R.id.selectedTime);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && editListener != null) {
                        Classwork classwork = classworkList.get(position);
                        editListener.onEditClasswork(classwork);
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && deleteListener != null) {
                        deleteListener.onDeleteClasswork(position);
                    }
                }
            });
        }

        public void bind(Classwork classwork) {
            textViewTitle.setText(classwork.getName());
            textViewType.setText("Type: " + classwork.getClassworkType());
            textViewClass.setText("Class: " + classwork.getAssociatedClass());
            textViewDueDate.setText("Due Date: " + formatDate(classwork.getDueDateInMillis()));
            locationTextView.setText("Location: " + classwork.getLocation());
            timeTextView.setText("Time: " + classwork.getTime());

        }

        private String formatDate(long millis) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return dateFormat.format(new Date(millis));
        }
    }
}