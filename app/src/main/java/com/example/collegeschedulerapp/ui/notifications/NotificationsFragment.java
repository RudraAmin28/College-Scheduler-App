package com.example.collegeschedulerapp.ui.notifications;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


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

public class NotificationsFragment extends Fragment implements TaskAdapter.OnDeleteButtonClickListener {

    private FragmentNotificationsBinding binding;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    private static final String PREFS_NAME = "TodoPrefs";
    private static final String KEY_TASK_LIST = "taskList";


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
        taskAdapter.setOnDeleteButtonClickListener(this);
        recyclerView.setAdapter(taskAdapter);


        loadTasksFromPrefs();

        // Add sample tasks


        // Notify the adapter that the data set has changed
        taskAdapter.notifyDataSetChanged();

        // Find the FloatingActionButton in the fragment layout
        com.google.android.material.floatingactionbutton.FloatingActionButton fab =
                root.findViewById(R.id.floatingActionButtonToDo);

        // Set click listener for the FloatingActionButton
        fab.setOnClickListener(view -> showAddTaskDialog());

        return root;
    }

    public void onDeleteButtonClick(int position) {
        // Remove the task at the clicked position
        if (position >= 0 && position < taskList.size()) {
            taskList.remove(position);

            // Save tasks to SharedPreferences
            saveTasksToPrefs();

            // Notify the adapter that the data set has changed
            taskAdapter.notifyDataSetChanged();
        }
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

                // Save tasks to SharedPreferences
                saveTasksToPrefs();

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

    private void loadTasksFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String tasksJson = prefs.getString(KEY_TASK_LIST, null);

        if (tasksJson != null) {
            // Convert JSON string to List<Task>
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> loadedTasks = new Gson().fromJson(tasksJson, listType);

            // Clear existing tasks and add loaded tasks
            taskList.clear();
            taskList.addAll(loadedTasks);
        }
    }

    private void saveTasksToPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Convert List<Task> to JSON string
        String tasksJson = new Gson().toJson(taskList);

        // Save tasks to SharedPreferences
        editor.putString(KEY_TASK_LIST, tasksJson);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
