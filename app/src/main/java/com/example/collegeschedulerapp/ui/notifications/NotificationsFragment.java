package com.example.collegeschedulerapp.ui.notifications;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.Task;
import com.example.collegeschedulerapp.TaskAdapter;
import com.example.collegeschedulerapp.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the task list and adapter
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        // Find the RecyclerView in the fragment layout
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);

        // Set up the RecyclerView with a LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(taskAdapter);

        // Add sample tasks
        taskList.add(new Task("Task 1"));
        taskList.add(new Task("Task 2"));
        taskList.add(new Task("CS 2110 Homework"));
        taskList.add(new Task("CS 2110 Exam"));
        taskList.add(new Task("CS 2110 Lab"));
        taskList.add(new Task("CS 2110 Lecture"));
        taskList.add(new Task("Task 1"));
        taskList.add(new Task("Task 2"));
        taskList.add(new Task("CS 2110 Homework"));
        taskList.add(new Task("CS 2110 Exam"));
        taskList.add(new Task("CS 2110 Lab"));
        taskList.add(new Task("CS 2110 Lecture"));
        taskList.add(new Task("Task 1"));
        taskList.add(new Task("Task 2"));
        taskList.add(new Task("CS 2110 Homework"));
        taskList.add(new Task("CS 2110 Exam"));
        taskList.add(new Task("CS 2110 Lab"));
        taskList.add(new Task("CS 2110 Lecture"));


        // Notify the adapter that the data set has changed
        taskAdapter.notifyDataSetChanged();

        // Find the FloatingActionButton in the fragment layout
        com.google.android.material.floatingactionbutton.FloatingActionButton fab =
                root.findViewById(R.id.floatingActionButtonToDo);

        // Set click listener for the FloatingActionButton
        fab.setOnClickListener(view -> showAddTaskDialog());

        return root;
    }


    private void showAddTaskDialog() {
        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_add_task, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Find views in the dialog layout
        EditText editTextTask = dialogView.findViewById(R.id.editTextTask);
        Button buttonAddTask = dialogView.findViewById(R.id.buttonAddTask);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        // Set click listener for the "Add Task" button
        buttonAddTask.setOnClickListener(v -> {
            String taskTitle = editTextTask.getText().toString();
            if (!taskTitle.isEmpty()) {
                // Add the new task to the list
                taskList.add(new Task(taskTitle));
                // Notify the adapter that the data set has changed
                taskAdapter.notifyDataSetChanged();
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Set click listener for the "Cancel" button
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
