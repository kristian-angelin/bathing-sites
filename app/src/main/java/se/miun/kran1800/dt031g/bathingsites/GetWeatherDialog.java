package se.miun.kran1800.dt031g.bathingsites;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;

public class GetWeatherDialog extends DialogFragment {

    public GetWeatherDialog() {}

    public static GetWeatherDialog newInstance() {
        return new GetWeatherDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_get_weather_dialog, null);
        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

