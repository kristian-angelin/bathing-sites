package se.miun.kran1800.dt031g.bathingsites;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class ShowRandomBathingSiteDialog extends DialogFragment {

    private BathingSite bathingSite;

    // on new instance put bathing site in bundle.
    public static ShowRandomBathingSiteDialog newInstance(BathingSite bathingSite) {
        ShowRandomBathingSiteDialog fragment = new ShowRandomBathingSiteDialog();
        Bundle args = new Bundle();
        args.putSerializable("bathingSite", bathingSite);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowRandomBathingSiteDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bathingSite = (BathingSite) getArguments().getSerializable("bathingSite");
    }

    public ShowRandomBathingSiteDialog(BathingSite bathingSite) {
        this.bathingSite = bathingSite;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add text to dialog
        builder.setTitle(getString(R.string.title_show_bathing_site));
        if(bathingSite != null) {
            builder.setMessage(getString(R.string.form_name) + ": " + bathingSite.name + "\n" +
                    getString(R.string.form_description) + ": " + bathingSite.description + "\n" +
                    getString(R.string.form_address) + ": " + bathingSite.address + "\n" +
                    getString(R.string.form_latitude) + ": " + bathingSite.latitude + "\n" +
                    getString(R.string.form_longitude) + ": " + bathingSite.longitude + "\n" +
                    getString(R.string.form_grade) + ": " + bathingSite.grade + "\n" +
                    getString(R.string.form_water_temp) + ": " + bathingSite.waterTemp + "\n" +
                    getString(R.string.form_date_for_temp) + ": " + bathingSite.dateForTemp);
        } else
            builder.setMessage(getString(R.string.error_no_bathing_sites));
        // Create the ok button
        // Add view and a button to builder
        builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });


        return builder.create();
    }
}
