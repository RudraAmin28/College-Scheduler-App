package com.example.collegeschedulerapp.ui.Classwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.ui.Classes.ClassesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


// Inside DashboardFragment.java

public class ClassworkFragment extends Fragment implements ClassworkAdapter.OnClassworkEditListener {

    private RecyclerView recyclerViewClasswork;
    private FloatingActionButton floatingActionButtonClasswork;
    private List<Classwork> classworkList;
    private ClassworkAdapter classworkAdapter;
    private Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerViewClasswork = view.findViewById(R.id.recyclerViewClasswork);
        floatingActionButtonClasswork = view.findViewById(R.id.floatingActionButtonClasswork);

        // Initialize the classworkList and classworkAdapter
        classworkList = new ArrayList<>();
        classworkAdapter = new ClassworkAdapter(requireContext(), classworkList);
        classworkAdapter.setOnClassworkEditListener(this);

        // Set up RecyclerView
        recyclerViewClasswork.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewClasswork.setAdapter(classworkAdapter);

        // Initialize the spinner
        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Sort the classworkList based on the selected option
                sortClassworkList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up FloatingActionButton click listener
        floatingActionButtonClasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Access the selected item position from the spinner
                int sortBy = spinner.getSelectedItemPosition();
                showAddClassworkDialog(sortBy);
            }
        });

        classworkAdapter.setOnClassworkDeleteListener(new ClassworkAdapter.OnClassworkDeleteListener() {
            @Override
            public void onDeleteClasswork(int position) {
                deleteClasswork(position);
            }
        });

        loadClassworkFromPrefs();

        return view;
    }


    private void sortClassworkList(int sortBy) {
        // Sort the classworkList based on the selected option
        switch (sortBy) {
            case 1: // Sort by class
                Collections.sort(classworkList, new Comparator<Classwork>() {
                    @Override
                    public int compare(Classwork c1, Classwork c2) {
                        return c1.getAssociatedClass().compareToIgnoreCase(c2.getAssociatedClass());
                    }
                });
                break;
            case 0: // Sort by due date
                Collections.sort(classworkList, new Comparator<Classwork>() {
                    @Override
                    public int compare(Classwork c1, Classwork c2) {
                        return Long.compare(c1.getDueDateInMillis(), c2.getDueDateInMillis());
                    }
                });
                break;
            default:
                break;
        }

        // Notify the adapter of the changes in the dataset
        classworkAdapter.notifyDataSetChanged();
    }

    private void showAddClassworkDialog(int sortBy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_add_classwork, null);

        // Get references to the views in the layout
        EditText editTextTitle = view.findViewById(R.id.editClassworkTitle);
        Spinner spinnerType = view.findViewById(R.id.spinnerClassworkType);
        Spinner spinnerClass = view.findViewById(R.id.spinnerClass);
        DatePicker datePicker = view.findViewById(R.id.datePickerDueDate);
        EditText editTextLocation = view.findViewById(R.id.editClassworkLocation);
        Button buttonAddClasswork = view.findViewById(R.id.buttonAddClasswork);
        Button buttonCancelClasswork = view.findViewById(R.id.buttonCancelClasswork);

        // Set up ArrayAdapter for the Classwork Type Spinner
        String[] classesArr = new String[ClassesFragment.returnClassList().size()];
        for (int i = 0; i < ClassesFragment.returnClassList().size(); i++) {
            classesArr[i] = ClassesFragment.returnClassList().get(i).getClassName();
        }

// Sort the classesArr array
        Arrays.sort(classesArr, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return c1.compareToIgnoreCase(c2);
            }
        });

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, classesArr);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        buttonAddClasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from dialog views
                String title = editTextTitle.getText().toString();
                String type = spinnerType.getSelectedItem().toString();
                String classes = spinnerClass.getSelectedItem().toString();
                long dueDateInMillis = getDueDateInMillis(datePicker);
                String location = editTextLocation.getText().toString();
                if (editTextLocation.getText().toString().isEmpty()) {
                    location = "N/A";
                }

                // Create a new Classwork object
                Classwork newClasswork = new Classwork(title, type, classes, dueDateInMillis, location);

                // Add the new classwork item to the list
                classworkList.add(newClasswork);

                // Sort the classworkList based on the selected option
                sortClassworkList(sortBy);

                // Notify the adapter that data has changed
                classworkAdapter.notifyDataSetChanged();

                // Save classwork data to SharedPreferences after adding
                saveClassworkToPrefs();

                // Dismiss the dialog after adding
                dialog.dismiss();
            }
        });

        buttonCancelClasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog on cancel
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showEditClassworkDialog(Classwork classwork, int sortBy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_edit_classwork, null);

        EditText editTextTitle = view.findViewById(R.id.editClassworkTitle);
        Spinner spinnerType = view.findViewById(R.id.spinnerEditClassworkType);
        Spinner spinnerClass = view.findViewById(R.id.spinnerEditClass);
        DatePicker datePicker = view.findViewById(R.id.datePickerEditDueDate);
        Button buttonUpdateClasswork = view.findViewById(R.id.buttonUpdateClasswork);
        Button buttonCancelEdit = view.findViewById(R.id.buttonCancelEdit);
        EditText editTextLocation = view.findViewById(R.id.editClassworkLocation);

        // Set initial values based on the provided Classwork object
        editTextTitle.setText(classwork.getName());
        editTextLocation.setText(classwork.getLocation());

        // Set the initial value for the Classwork Type Spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.classwork_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        int typePosition = typeAdapter.getPosition(classwork.getClassworkType());
        spinnerType.setSelection(typePosition);

        // Set the initial value for the Class Spinner (Assuming you have a list of classes)
        String[] classesArr = new String[ClassesFragment.returnClassList().size()];
        for (int i = 0; i < ClassesFragment.returnClassList().size(); i++) {
            classesArr[i] = ClassesFragment.returnClassList().get(i).getClassName();
        }

        Arrays.sort(classesArr, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return c1.compareToIgnoreCase(c2);
            }
        });

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, classesArr);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);
        int classPosition = classAdapter.getPosition(classwork.getAssociatedClass());
        spinnerClass.setSelection(classPosition);


        // Set the initial value for the Due Date DatePicker
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(classwork.getDueDateInMillis());
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        builder.setView(view);
        AlertDialog dialog = builder.create();

        buttonUpdateClasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String type = spinnerType.getSelectedItem().toString();
                String classes = spinnerClass.getSelectedItem().toString();
                long dueDateInMillis = getDueDateInMillis(datePicker);
                String location = editTextLocation.getText().toString();
                if (editTextLocation.getText().toString().isEmpty()) {
                    location = "N/A";
                }

                // Update the Classwork object
                classwork.setName(title);
                classwork.setClassworkType(type);
                classwork.setAssociatedClass(classes);
                classwork.setDueDateInMillis(dueDateInMillis);
                classwork.setLocation(location);


                sortClassworkList(sortBy);

                // Notify the adapter that data has changed for the specific Classwork object
                int position = classworkList.indexOf(classwork);
                if (position != -1) {
                    classworkAdapter.notifyItemChanged(position);
                }

                // Save classwork data to SharedPreferences after updating
                saveClassworkToPrefs();

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        buttonCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog on cancel
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onEditClasswork(Classwork classwork) {
        // Handle the edit action for the classwork
        int sortBy = spinner.getSelectedItemPosition();// Assuming you have a spinner for sorting
        showEditClassworkDialog(classwork, sortBy);
    }

    private void deleteClasswork(int position) {
        if (position >= 0 && position < classworkList.size()) {
            // Remove classwork from the list
            classworkList.remove(position);

            // Save the updated classwork list
            saveClassworkToPrefs();

            // Notify the adapter about the removal
            classworkAdapter.notifyItemRemoved(position);
        }
    }

    private void saveClassworkToPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Convert classworkList to a JSON string
        String classworkData = convertClassworkListToString(classworkList);

        editor.putString("classworkData", classworkData);
        editor.apply();
    }


    private String convertClassworkListToString(List<Classwork> classworkList) {
        // Implement your logic to convert classworkList to a JSON string
        // You can use Gson library or any other method to serialize the data
        // For simplicity, you can use a basic approach like concatenating values into a string
        // Remember to handle any exceptions appropriately

        StringBuilder stringBuilder = new StringBuilder();
        for (Classwork classwork : classworkList) {
            stringBuilder.append(classwork.getName()).append(",").append(classwork.getClassworkType()).append(",").append(classwork.getAssociatedClass()).append(",").append(classwork.getDueDateInMillis()).append(",").append(classwork.getLocation()).append(";");
        }
        return stringBuilder.toString();
    }

    private void loadClassworkFromPrefs() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String classworkData = prefs.getString("classworkData", "");

        // Convert the JSON string back to a list of Classwork objects
        List<Classwork> loadedClassworkList = convertStringToClassworkList(classworkData);

        // Update your classworkList with the loaded data
        classworkList.clear();
        classworkList.addAll(loadedClassworkList);
        classworkAdapter.notifyDataSetChanged();
    }

    private List<Classwork> convertStringToClassworkList(String classworkData) {
        List<Classwork> classworkList = new ArrayList<>();
        String[] classworkArray = classworkData.split(";");
        for (String classworkString : classworkArray) {
            String[] classworkValues = classworkString.split(",");
            if (classworkValues.length == 5) { // Adjust the length to match the number of fields including location
                String title = classworkValues[0];
                String type = classworkValues[1];
                String associatedClass = classworkValues[2];
                long dueDateInMillis = Long.parseLong(classworkValues[3]);
                String location = classworkValues[4]; // Add the location field

                Classwork classwork = new Classwork(title, type, associatedClass, dueDateInMillis, location);
                classworkList.add(classwork);
            }
        }
        return classworkList;
    }


    private long getDueDateInMillis(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTimeInMillis();
    }
}