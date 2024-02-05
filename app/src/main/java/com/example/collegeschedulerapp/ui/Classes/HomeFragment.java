package com.example.collegeschedulerapp.ui.Classes;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.Locale;

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


    private void showTimePickerDialog(final TextView textView) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        // Format the selected time and set it to the TextView in AM/PM format
                        String amPm;
                        int hour;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                            hour = (hourOfDay == 12) ? 12 : (hourOfDay - 12);
                        } else {
                            amPm = "AM";
                            hour = (hourOfDay == 0) ? 12 : hourOfDay;
                        }

                        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);
                        textView.setText(formattedTime);
                    }
                },
                // Set the default time to 12:00 AM
                0,
                0,
                false // Set to false to use 12-hour format
        );

        timePickerDialog.show();
    }

    private List<String> getSelectedDays(CheckBox mondayCheckBox, CheckBox tuesdayCheckBox, CheckBox wednesdayCheckBox, CheckBox thursdayCheckBox, CheckBox fridayCheckBox, CheckBox saturdayCheckBox, CheckBox sundayCheckBox) {
        List<String> selectedDays = new ArrayList<>();

        if (mondayCheckBox.isChecked()) {
            selectedDays.add("Mon");
        }
        if (tuesdayCheckBox.isChecked()) {
            selectedDays.add("Tue");
        }
        if (wednesdayCheckBox.isChecked()) {
            selectedDays.add("Wed");
        }
        if (thursdayCheckBox.isChecked()) {
            selectedDays.add("Thu");
        }
        if (fridayCheckBox.isChecked()) {
            selectedDays.add("Fri");
        }
        if (saturdayCheckBox.isChecked()) {
            selectedDays.add("Sat");
        }
        if (sundayCheckBox.isChecked()) {
            selectedDays.add("Sun");
        }

        return selectedDays;
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

        Button buttonStartTime = dialogView.findViewById(R.id.ButtonStartTime);
        Button buttonEndTime = dialogView.findViewById(R.id.ButtonEndTime);

        TextView selectedStartTime = dialogView.findViewById(R.id.selectedStartTime);
        TextView selectedEndTime = dialogView.findViewById(R.id.selectedEndTime);

        CheckBox mondayCheckBox = dialogView.findViewById(R.id.mondayCheckBox);
        CheckBox tuesdayCheckBox = dialogView.findViewById(R.id.tuesdayCheckBox);
        CheckBox wednesdayCheckBox = dialogView.findViewById(R.id.wednesdayCheckBox);
        CheckBox thursdayCheckBox = dialogView.findViewById(R.id.thursdayCheckBox);
        CheckBox fridayCheckBox = dialogView.findViewById(R.id.fridayCheckBox);
        CheckBox saturdayCheckBox = dialogView.findViewById(R.id.saturdayCheckBox);
        CheckBox sundayCheckBox = dialogView.findViewById(R.id.sundayCheckBox);

        buttonAddClass.setOnClickListener(v -> {
            String className = addClassNameTextView.getText().toString();
            String professorName = addClassProfessorTextView.getText().toString();
            List<String> selectedDays = getSelectedDays(mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox);

            if (!className.isEmpty() && !professorName.isEmpty() && selectedDays != null) {
                // Add the new class to the list
                Classes newClass = new Classes(className, professorName, selectedStartTime.getText().toString(), selectedEndTime.getText().toString(), selectedDays);
                classesList.add(newClass);

                // Save classes to SharedPreferences
                saveTasksToPrefs();

                // Notify the adapter that the data set has changed
                classesAdapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });


        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(selectedStartTime);
            }
        });

        buttonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(selectedEndTime);
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

        Button buttonStartTime = dialogView.findViewById(R.id.ButtonStartTime);
        Button buttonEndTime = dialogView.findViewById(R.id.ButtonEndTime);

        TextView selectedStartTime = dialogView.findViewById(R.id.selectedStartTime);
        TextView selectedEndTime = dialogView.findViewById(R.id.selectedEndTime);

        // Set the current task title, professor, start time, and end time in the edit text and text views
        addClassNameTextView.setText(classesList.get(position).getClassName());
        addClassProfessorTextView.setText(classesList.get(position).getProfessor());
        selectedStartTime.setText(classesList.get(position).getStartTime());
        selectedEndTime.setText(classesList.get(position).getEndTime());

        addClassNameTextView.setText(classesList.get(position).getClassName());
        addClassProfessorTextView.setText(classesList.get(position).getProfessor());
        selectedStartTime.setText(classesList.get(position).getStartTime());
        selectedEndTime.setText(classesList.get(position).getEndTime());


        CheckBox mondayCheckBox = dialogView.findViewById(R.id.mondayCheckBox);
        CheckBox tuesdayCheckBox = dialogView.findViewById(R.id.tuesdayCheckBox);
        CheckBox wednesdayCheckBox = dialogView.findViewById(R.id.wednesdayCheckBox);
        CheckBox thursdayCheckBox = dialogView.findViewById(R.id.thursdayCheckBox);
        CheckBox fridayCheckBox = dialogView.findViewById(R.id.fridayCheckBox);
        CheckBox saturdayCheckBox = dialogView.findViewById(R.id.saturdayCheckBox);
        CheckBox sundayCheckBox = dialogView.findViewById(R.id.sundayCheckBox);

        List<String> originalSelectedDays = classesList.get(position).getSelectedDays();

        mondayCheckBox.setChecked(originalSelectedDays.contains("Mon"));
        tuesdayCheckBox.setChecked(originalSelectedDays.contains("Tue"));
        wednesdayCheckBox.setChecked(originalSelectedDays.contains("Wed"));
        thursdayCheckBox.setChecked(originalSelectedDays.contains("Thu"));
        fridayCheckBox.setChecked(originalSelectedDays.contains("Fri"));
        saturdayCheckBox.setChecked(originalSelectedDays.contains("Sat"));
        sundayCheckBox.setChecked(originalSelectedDays.contains("Sun"));


        buttonAddClass.setOnClickListener(v -> {
            String editedClassName = addClassNameTextView.getText().toString();
            String editedProfessorName = addClassProfessorTextView.getText().toString();
            String editedStartTime = selectedStartTime.getText().toString();
            String editedEndTime = selectedEndTime.getText().toString();
            List<String> editedSelectedDays = getSelectedDays(mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox, fridayCheckBox, saturdayCheckBox, sundayCheckBox);

            // Check if any of the essential fields are empty or days are null
            if (!editedClassName.isEmpty() && !editedProfessorName.isEmpty() && editedStartTime != null && editedEndTime != null && editedSelectedDays != null) {
                // Update the class information
                classesList.get(position).setClassName(editedClassName);
                classesList.get(position).setProfessor(editedProfessorName);
                classesList.get(position).setStartTime(editedStartTime);
                classesList.get(position).setEndTime(editedEndTime);
                classesList.get(position).setSelectedDays(editedSelectedDays);

                // Save classes to SharedPreferences
                saveTasksToPrefs();

                // Notify the adapter that the data set has changed
                classesAdapter.notifyDataSetChanged();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });



        buttonStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(selectedStartTime);
            }
        });

        buttonEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(selectedEndTime);
            }
        });


        // Set click listener for the "Cancel" button
        buttonCancelClass.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


}