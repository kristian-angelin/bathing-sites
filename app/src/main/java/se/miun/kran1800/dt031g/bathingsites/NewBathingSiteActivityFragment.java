package se.miun.kran1800.dt031g.bathingsites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewBathingSiteActivityFragment extends Fragment {

    private TextInputEditText nameField;
    private TextInputEditText descField;
    private TextInputEditText addressField;
    private TextInputEditText latField;
    private TextInputEditText longField;
    private RatingBar gradeBar;
    private TextInputEditText tempField;
    private TextInputEditText dateField;

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
    }

    // Set the current date as default value in date field of form.
    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        dateField.setText(currentDate);
    }

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
}
