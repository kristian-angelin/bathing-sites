package se.miun.kran1800.dt031g.bathingsites;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewBathingSiteActivityFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText nameField;
    private TextInputEditText descField;
    private TextInputEditText addressField;
    private TextInputEditText latField;
    private TextInputEditText longField;
    private RatingBar gradeBar;
    private TextInputEditText tempField;
    private TextInputEditText dateField;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_bathing_site, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up date format.
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Get all fields
        nameField = getView().findViewById(R.id.form_name_field);
        descField = getView().findViewById(R.id.form_desc_field);
        addressField = getView().findViewById(R.id.form_address_field);
        latField = getView().findViewById(R.id.form_latitude_field);
        longField = getView().findViewById(R.id.form_longitude_field);
        gradeBar = getView().findViewById(R.id.form_rating_bar);
        tempField = getView().findViewById(R.id.form_temp_field);
        dateField = getView().findViewById(R.id.form_date_field);
        // Set date
        setDefaultDate();

        // Setup click event on date field.
        dateField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    // Set the current date as default value in date field of form.
    private void setDefaultDate() {
        calendar = Calendar.getInstance();
        String currentDate = dateFormat.format(calendar.getTime());
        dateField.setText(currentDate);
    }

    // Clear form
    public void clearForm() {
        nameField.setText("");
        descField.setText("");
        addressField.setText("");
        latField.setText("");
        longField.setText("");
        gradeBar.setRating(0);
        tempField.setText("");
        setDefaultDate();
    }

    public void saveBatingSite() {

    }

    // Creates and display a date picker dialog.
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // When date has been selected set it to show in form.
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = dateFormat.format(calendar.getTime());
        dateField.setText(selectedDate);
    }
}
