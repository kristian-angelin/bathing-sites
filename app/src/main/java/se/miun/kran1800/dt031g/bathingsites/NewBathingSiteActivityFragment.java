package se.miun.kran1800.dt031g.bathingsites;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment for taking user input and saving bath site to database.
 * Implements LoaderCallbacks to create asyncTaskLoader for saving bath site in separate thread.
 */
public class NewBathingSiteActivityFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Boolean> {

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

            // Get all variables.
            String name = nameField.getText().toString();
            String description = descField.getText().toString();
            String address = addressField.getText().toString();
            Double latitude = getDoubleFromText(latField);
            Double longitude = getDoubleFromText(longField);
            float grade = gradeBar.getRating();
            Double waterTemp = getDoubleFromText(tempField);
            String dateForTemp = dateField.getText().toString();

            // Create a bathingSite and put in bundle to send to SaveToDatabaseTask
            BathingSite bathingSite = new BathingSite(0, name, description, address,
                    latitude, longitude, grade, waterTemp, dateForTemp);
            Bundle argsBundle = new Bundle();
            argsBundle.putSerializable("bathing_site", bathingSite);
            getLoaderManager().restartLoader(0, argsBundle, this);

        }
    }

    private double getDoubleFromText(TextInputEditText textField) {
        double value = 0;
        if(!textField.getText().toString().trim().isEmpty()) {
            value = Double.parseDouble(textField.getText().toString());
        }
        return value;
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

    @NonNull
    @Override
    public Loader<Boolean> onCreateLoader(int id, @Nullable Bundle args) {
        BathingSite bathingSite = (BathingSite) args.getSerializable("bathing_site");
        return new SaveToDatabaseTask(getContext(), bathingSite);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Boolean> loader, Boolean entrySuccess) {
        if(entrySuccess) {
            Toast.makeText(getContext(), getString(R.string.database_save_success), Toast.LENGTH_LONG).show();
            clearForm();
            // Navigate to main activity.
            NavUtils.navigateUpFromSameTask(getActivity());
        } else {
            Toast.makeText(getContext(), getString(R.string.database_save_failed), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Boolean> loader) {}
}
