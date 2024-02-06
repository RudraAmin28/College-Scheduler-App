package com.example.collegeschedulerapp.ui.ToDo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.databinding.FragmentTasksBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment implements TaskAdapter.OnDeleteButtonClickListener, TaskAdapter.OnCheckedChangeListener, TaskAdapter.OnEditButtonClickListener {

    private FragmentTasksBinding binding;
    private List<Task> taskList;
    private TaskAdapter taskAdapter;

    private static final String PREFS_NAME = "TodoPrefs";
    private static final String KEY_TASK_LIST = "taskList";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewTODO);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskAdapter.setOnDeleteButtonClickListener(this);
        recyclerView.setAdapter(taskAdapter);

        taskAdapter.setOnCheckedChangeListener(this);

        loadTasksFromPrefs();

        taskAdapter.notifyDataSetChanged();

        com.google.android.material.floatingactionbutton.FloatingActionButton fab =
                root.findViewById(R.id.floatingActionButtonToDo);

        fab.setOnClickListener(view -> showAddTaskDialog());
        taskAdapter.setOnEditButtonClickListener(this);

        return root;
    }

    public void onCheckboxChanged(int position, boolean isChecked) {
        if (position >= 0 && position < taskList.size()) {
            taskList.get(position).setChecked(isChecked);
            saveTasksToPrefs();
            taskAdapter.notifyDataSetChanged();
        }
    }

    public void onDeleteButtonClick(int position) {
        if (position >= 0 && position < taskList.size()) {
            taskList.remove(position);

            saveTasksToPrefs();

            taskAdapter.notifyDataSetChanged();
        }
    }


    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_add_task, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText editTextTaskTitle = dialogView.findViewById(R.id.editTextTaskTitle);
        EditText editTextTaskCategory = dialogView.findViewById(R.id.editTextTaskCategory);
        Button buttonAddTask = dialogView.findViewById(R.id.buttonAddTask);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancelTask);

        buttonAddTask.setOnClickListener(v -> {
            String taskTitle = editTextTaskTitle.getText().toString();
            String taskCategory = editTextTaskCategory.getText().toString();
            if (!taskTitle.isEmpty()) {
                taskList.add(new Task(taskTitle, taskCategory, false));

                saveTasksToPrefs();

                taskAdapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void loadTasksFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String tasksJson = prefs.getString(KEY_TASK_LIST, null);

        if (tasksJson != null) {
            // Convert JSON string to List<Task>
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> loadedTasks = new Gson().fromJson(tasksJson, listType);

            taskList.clear();
            taskList.addAll(loadedTasks);
        }
    }

    private void saveTasksToPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Convert List<Task> to JSON string
        String tasksJson = new Gson().toJson(taskList);

        editor.putString(KEY_TASK_LIST, tasksJson);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onEditButtonClick(int position) {
        if (position >= 0 && position < taskList.size()) {

            showEditTaskDialog(position);
        }
    }


    private void showEditTaskDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_edit_task, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText editTextTaskTitle = dialogView.findViewById(R.id.editTextTaskTitle);
        EditText editTextTaskCategory = dialogView.findViewById(R.id.editTextTaskCategory);
        Button buttonEditTask = dialogView.findViewById(R.id.buttonAddTask);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancelTask);

        editTextTaskTitle.setText(taskList.get(position).getTitle());

        editTextTaskCategory.setText(taskList.get(position).getCategory());

        buttonEditTask.setOnClickListener(v -> {
            String editedTaskTitle = editTextTaskTitle.getText().toString();
            String taskCategory = editTextTaskCategory.getText().toString();
            if (!editedTaskTitle.isEmpty()) {
                taskList.get(position).setTitle(editedTaskTitle);
                taskList.get(position).setCategory(taskCategory);

                saveTasksToPrefs();
                taskAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
