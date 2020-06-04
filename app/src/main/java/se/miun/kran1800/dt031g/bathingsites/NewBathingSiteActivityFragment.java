package se.miun.kran1800.dt031g.bathingsites;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
        clearForm();

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
        clearErrors();
        clearFocus();
    }

    // Save bathing site by showing a toast.
    public void saveBatingSite() {
        if(validFormEntries()) {
            String toastMsg = "Name: " + nameField.getText().toString() + "\n"
                            + "Description: " + descField.getText().toString() + "\n"
                            + "Address: " + addressField.getText().toString() + "\n"
                            + "Latitude: " + latField.getText().toString() + "\n"
                            + "Longitude: " + longField.getText().toString() + "\n"
                            + "Grade: " + gradeBar.getRating() + "\n"
                            + "water temo: " + tempField.getText().toString() + "\n"
                            + "Date for temp: " + dateField.getText().toString() + "\n";
            Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
        }
    }

    // Validate form and display error messages.
    private boolean validFormEntries() {
        boolean validStatus = true;
        // Validate name field.
        if(nameField.getText().toString().trim().isEmpty()) {
            nameField.setError(getString(R.string.error_no_name));
            validStatus = false;
        }
        // Validate position fields, must have either address or long/lat.
        if(addressField.getText().toString().trim().isEmpty()) {
            if(latField.getText().toString().trim().isEmpty() || longField.getText().toString().trim().isEmpty()) {
                addressField.setError(getString(R.string.error_no_position));
                latField.setError(getString(R.string.error_no_position));
                longField.setError(getString(R.string.error_no_position));
                validStatus = false;
            }
            // If lat/long is set remove error on address.
            else {
                addressField.setError(null);
            }
        }
        // If address is set, remove error on lat/long.
        else {
            latField.setError(null);
            longField.setError(null);
        }

        // Check empty string before cast do double
        if(!latField.getText().toString().trim().isEmpty()) {
            double latitude = Double.parseDouble(latField.getText().toString());
            // Check correct latitude
            if(latitude < -90 || latitude > 90) {
                latField.setError(getString(R.string.error_invalid_lat));
                validStatus = false;
            }
        }
        // Check empty string before cast do double
        if(!longField.getText().toString().trim().isEmpty()) {
            double longitude = Double.parseDouble(longField.getText().toString());
            // Check correct longitude
            if(longitude < -180 || longitude > 180) {
                longField.setError(getString(R.string.error_invalid_long));
                validStatus = false;
            }
        }

        // Remove error messages if form is valid.
        if(validStatus) {
            clearErrors();
        }
        clearFocus();
        return validStatus;
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

    // Clear focused object and hide keyboard.
    private void clearFocus() {
        View view = getActivity().getCurrentFocus();
        if(view != null) {
            InputMethodManager inputMan = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMan.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        LinearLayout formLayout = getView().findViewById(R.id.form_layout);
        formLayout.requestFocus();
    }

    // Clear all form errors
    private void clearErrors() {
        nameField.setError(null);
        addressField.setError(null);
        latField.setError(null);
        longField.setError(null);
    }

    // Get position as either address or lat/long. Lat/long prioritized.
    public String getPositionDataAsHttpGet() {
        if(!latField.getText().toString().equals("") &&
            !longField.getText().toString().equals("")) {
                return "?lat=" + latField.getText().toString() +
                        "&lon=" + longField.getText().toString();
        }
        else if(!addressField.getText().toString().equals("")) {
            return "?q=" + addressField.getText().toString();
        }
        // If fields are empty, set error message accordingly.
        addressField.setError(getString(R.string.error_no_position));
        latField.setError(getString(R.string.error_no_position));
        longField.setError(getString(R.string.error_no_position));
        return null;
    }

    // Display error message that position can't be found.
    public void SetErrorPosDoesNotExist() {
        addressField.setError(getString(R.string.error_invalid_position));
        latField.setError(getString(R.string.error_invalid_position));
        longField.setError(getString(R.string.error_invalid_position));
    }
}
