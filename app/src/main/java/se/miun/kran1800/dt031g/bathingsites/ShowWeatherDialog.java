package se.miun.kran1800.dt031g.bathingsites;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowWeatherDialog extends DialogFragment {

    private String descriptionText;
    private String tempText;
    private Drawable drawable;

    private TextView description;
    private TextView temp;
    private ImageView image;

    public ShowWeatherDialog() {}

    public ShowWeatherDialog(String description, String temp, Drawable drawable) {
        this.descriptionText = description;
        this.tempText = temp;
        this.drawable = drawable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_show_weather_dialog, null);

        description = view.findViewById(R.id.current_weather_description);
        temp = view.findViewById(R.id.current_weather_temp);
        image = view.findViewById(R.id.current_weather_image);

        description.setText(descriptionText);
        temp.setText(tempText);
        image.setImageDrawable(drawable);

        builder.setView(view)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
