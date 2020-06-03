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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetWeatherDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetWeatherDialog extends DialogFragment {

    /*public static GetWeatherDialogFragment newInstance(int title) {
        GetWeatherDialogFragment fragment = new GetWeatherDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        fragment.setArguments(args);
        return fragment;
    }*/

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

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        AlertDialog.Builder builder = new Builder(this);
        return builder
                .setMessage("Are you sure you want to reset the count?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, "Did not reset!", 5).show();

                    }
                })

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this, "Did Reset!", 5).show();

                    }
                })
                .create();
        }
    }*/

