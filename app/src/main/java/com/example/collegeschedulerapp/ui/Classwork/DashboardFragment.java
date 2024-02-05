package com.example.collegeschedulerapp.ui.Classwork;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.example.collegeschedulerapp.ui.Classes.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


// Inside DashboardFragment.java

public class DashboardFragment extends Fragment implements ClassworkAdapter.OnClassworkEditListener {

    private RecyclerView recyclerViewClasswork;
    private FloatingActionButton floatingActionButtonClasswork;
    private List<Classwork> classworkList;
    private ClassworkAdapter classworkAdapter;

    // ...

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

        // Set up FloatingActionButton click listener
        floatingActionButtonClasswork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddClassworkDialog();
            }
        });

        loadClassworkFromPrefs();

        return view;
    }

    private void showAddClassworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_add_classwork, null);

        // Get references to the views in the layout
        EditText editTextTitle = view.findViewById(R.id.editClassworkTitle);
        Spinner spinnerType = view.findViewById(R.id.spinnerClassworkType);
        Spinner spinnerClass = view.findViewById(R.id.spinnerClass);
        DatePicker datePicker = view.findViewById(R.id.datePickerDueDate);
        Button buttonAddClass = view.findViewById(R.id.buttonAddClass);
        Button buttonCancelClasswork = view.findViewById(R.id.buttonCancelClasswork);

        EditText editTextLocation = view.findViewById(R.id.editClassworkLocation);


        // Set up ArrayAdapter for the Classwork Type Spinner
        String[] classesArr = new String[HomeFragment.returnClassList().size()];
        for (int i = 0; i < HomeFragment.returnClassList().size(); i++) {
            classesArr[i] = HomeFragment.returnClassList().get(i).getClassName();
        }

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, classesArr);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        builder.setView(view);
        AlertDialog dialog = builder.create();

        buttonAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from dialog views
                String title = editTextTitle.getText().toString();
                String type = spinnerType.getSelectedItem().toString();
                String classes = spinnerClass.getSelectedItem().toString();
                long dueDateInMillis = getDueDateInMillis(datePicker);

                // Create a new Classwork object
                String location = editTextLocation.getText().toString();
                Classwork newClasswork = new Classwork(title, type, classes, dueDateInMillis, location);

                // Add the new classwork item to the list (assuming classworkList is accessible)
                classworkList.add(newClasswork);

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

    private void showEditClassworkDialog(Classwork classwork) {
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
        String[] classesArr = new String[HomeFragment.returnClassList().size()];
        for (int i = 0; i < HomeFragment.returnClassList().size(); i++) {
            classesArr[i] = HomeFragment.returnClassList().get(i).getClassName();
        }

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
                String location = editTextLocation.getText().toString();
                String type = spinnerType.getSelectedItem().toString();
                String classes = spinnerClass.getSelectedItem().toString();
                long dueDateInMillis = getDueDateInMillis(datePicker);

                // Update the Classwork object
                classwork.setName(title);
                classwork.setLocation(location);
                classwork.setClassworkType(type);
                classwork.setAssociatedClass(classes);
                classwork.setDueDateInMillis(dueDateInMillis);

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
        showEditClassworkDialog(classwork);
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