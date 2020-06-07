package se.miun.kran1800.dt031g.bathingsites;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Creates an downloading dialog.
 */
public class DownloadingDialog extends DialogFragment {

    public DownloadingDialog() {}

    public static DownloadingDialog newInstance(String downloadingMessage) {
        DownloadingDialog downloadingDialog = new DownloadingDialog();
        Bundle args = new Bundle();
        args.putString("message", downloadingMessage);
        downloadingDialog.setArguments(args);
        return downloadingDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_downloading_dialog, null);

        TextView textView = view.findViewById(R.id.downloading_message);
        textView.setText(getArguments().getString("message"));

        builder.setView(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

