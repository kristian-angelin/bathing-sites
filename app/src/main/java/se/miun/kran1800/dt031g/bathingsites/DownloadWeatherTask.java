package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

public class DownloadWeatherTask extends AsyncTask<String, String, String> {

    private Context context;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    // Constructor to pass context
    DownloadWeatherTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_download_weather_layout);
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
