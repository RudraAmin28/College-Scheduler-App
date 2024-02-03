package com.example.collegeschedulerapp.ui.dashboard;

import java.util.List;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager; // Add this import
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeschedulerapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.content.DialogInterface;


// Inside DashboardFragment.java

public class DashboardFragment extends Fragment {

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

        return view;
    }

    private void showAddClassworkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.popup_add_classwork, null);

        // Initialize views and set up any logic if needed

        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Retrieve data from dialog views
                        EditText editTextTitle = view.findViewById(R.id.editClassworkTitle);
                        Spinner spinnerType = view.findViewById(R.id.spinnerClassworkType);
                        DatePicker datePicker = view.findViewById(R.id.datePickerDueDate);
                        Spinner spinnerClass = view.findViewById(R.id.spinnerClass);

                        String title = editTextTitle.getText().toString();
                        String type = spinnerType.getSelectedItem().toString();
                        long dueDateInMillis = getDueDateInMillis(datePicker);
                        String selectedClass = spinnerClass.getSelectedItem().toString();

                        // Create a new Classwork object
                        Classwork newClasswork = new Classwork(title, type, dueDateInMillis, selectedClass);

                        // Add the new classwork item to the list
                        classworkList.add(newClasswork);

                        // Notify the adapter that data has changed
                        classworkAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel button click
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private long getDueDateInMillis(DatePicker datePicker) {
        // Implement logic to convert DatePicker values to milliseconds
        // You can use Calendar or other methods for this conversion
        return 0; // Replace with actual implementation
    }
}