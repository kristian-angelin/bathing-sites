package se.miun.kran1800.dt031g.bathingsites;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Dialog for showing weather by loading text and image in string format (base64).
 */
public class ShowWeatherDialog extends DialogFragment {

    private String descriptionText;
    private String tempText;
    private String base64Image;

    // Call the dialog with newInstance().
    public static ShowWeatherDialog newInstance(String description, String temp, String base64Image) {
        ShowWeatherDialog fragment = new ShowWeatherDialog();
        Bundle args = new Bundle();
        args.putString("description", description);
        args.putString("temp", temp);
        args.putString("base64Image", base64Image);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowWeatherDialog() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        descriptionText = getArguments().getString("description");
        // Set temp and add degree sign.
        tempText = getArguments().getString("temp") + getString(R.string.degree_sign);
        base64Image = getArguments().getString("base64Image");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_show_weather_dialog, null);

        // Convert base64 string to drawable
        byte[] byteString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(byteString, 0, byteString.length);
        Drawable weatherImage = new BitmapDrawable(getResources(), decodedByte);

        // Get all items in view to and set the values.
        TextView description = view.findViewById(R.id.current_weather_description);
        TextView temp = view.findViewById(R.id.current_weather_temp);
        ImageView image = view.findViewById(R.id.current_weather_image);

        description.setText(descriptionText);
        temp.setText(tempText);
        image.setImageDrawable(weatherImage);

        // Add view and a button to builder
        builder.setView(view)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog and return it.
        return builder.create();
    }
}
