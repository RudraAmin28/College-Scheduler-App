package com.example.collegeschedulerapp.ui.Classes;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ClassAdapter.OnDeleteButtonClickListener, ClassAdapter.OnEditButtonClickListener {

    private FragmentHomeBinding binding;
    private List<Classes> classesList;
    private ClassAdapter classesAdapter;

    private static final String PREFS_NAME = "ClassesPrefs";
    private static final String KEY_TASK_LIST = "ClassesList";



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the task list and adapter
        classesList = new ArrayList<>();
        classesAdapter = new ClassAdapter(classesList);

        // Find the RecyclerView in the fragment layout
        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewClasses);

        // Set up the RecyclerView with a LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set the adapter for the RecyclerView
        classesAdapter.setOnDeleteButtonClickListener(this);
        recyclerView.setAdapter(classesAdapter);

        loadTasksFromPrefs();

        // Add sample tasks


        // Notify the adapter that the data set has changed
        classesAdapter.notifyDataSetChanged();

        // Find the FloatingActionButton in the fragment layout
        com.google.android.material.floatingactionbutton.FloatingActionButton fab =
                root.findViewById(R.id.floatingActionButtonClasses);

        // Set click listener for the FloatingActionButton
        fab.setOnClickListener(view -> showAddTaskDialog());
        classesAdapter.setOnEditButtonClickListener(this);

        return root;
    }


    public void onDeleteButtonClick(int position) {
        // Remove the task at the clicked position
        if (position >= 0 && position < classesList.size()) {
            classesList.remove(position);

            // Save tasks to SharedPreferences
            saveTasksToPrefs();

            // Notify the adapter that the data set has changed
            classesAdapter.notifyDataSetChanged();
        }
    }

    private void showAddTaskDialog() {
        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_add_class, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Find views in the dialog layout
        EditText addClassNameTextView = dialogView.findViewById(R.id.addClassNameTextView);
        EditText addClassProfessorTextView = dialogView.findViewById(R.id.addClassProfessorTextView);
        Button buttonAddClass = dialogView.findViewById(R.id.buttonAddClass);
        Button buttonCancelClass = dialogView.findViewById(R.id.buttonCancelClass);

        // Set click listener for the "Add Task" button
        buttonAddClass.setOnClickListener(v -> {
            String className = addClassNameTextView.getText().toString();
            String professorName = addClassProfessorTextView.getText().toString();
            if (!className.isEmpty() && !professorName.isEmpty()) {
                // Add the new task to the list
                classesList.add(new Classes(className, professorName));

                // Save tasks to SharedPreferences
                saveTasksToPrefs();

                // Notify the adapter that the data set has changed
                classesAdapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Set click listener for the "Cancel" button
        buttonCancelClass.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }

    private void loadTasksFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String tasksJson = prefs.getString(KEY_TASK_LIST, null);

        if (tasksJson != null) {
            // Convert JSON string to List<Task>
            Type listType = new TypeToken<List<Classes>>() {}.getType();
            List<Classes> loadedTasks = new Gson().fromJson(tasksJson, listType);

            // Clear existing tasks and add loaded tasks
            classesList.clear();
            classesList.addAll(loadedTasks);
        }
    }

    private void saveTasksToPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Convert List<Task> to JSON string
        String tasksJson = new Gson().toJson(classesList);

        // Save tasks to SharedPreferences
        editor.putString(KEY_TASK_LIST, tasksJson);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onEditButtonClick(int position) {
        // Handle the edit action for the task at the clicked position
        if (position >= 0 && position < classesList.size()) {

            showEditTaskDialog(position);
        }
    }


    private void showEditTaskDialog(int position) {
        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.popup_edit_class, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Find views in the dialog layout
        EditText addClassNameTextView = dialogView.findViewById(R.id.addClassNameTextView);
        EditText addClassProfessorTextView = dialogView.findViewById(R.id.addClassProfessorTextView);
        Button buttonAddClass = dialogView.findViewById(R.id.buttonAddClass);
        Button buttonCancelClass = dialogView.findViewById(R.id.buttonCancelClass);

        // Set the current task title in the edit text
        addClassNameTextView.setText(classesList.get(position).getclassName());
        addClassProfessorTextView.setText(classesList.get(position).getProfessor());

        // Set click listener for the "Edit Task" button
        buttonAddClass.setOnClickListener(v -> {
            String editedTaskTitle = addClassNameTextView.getText().toString();
            String editedProfessorTitle = addClassProfessorTextView.getText().toString();
            if (!editedTaskTitle.isEmpty() && !editedProfessorTitle.isEmpty()) {
                // Update the task title
                classesList.get(position).setClassName(editedTaskTitle);
                classesList.get(position).setProfessor(editedProfessorTitle);

                // Save tasks to SharedPreferences
                saveTasksToPrefs();

                // Notify the adapter that the data set has changed
                classesAdapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Set click listener for the "Cancel" button
        buttonCancelClass.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


}