package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

// Download weather and display dialog.
public class DownloadWeatherTask extends AsyncTask<String, String, String> {

    private Context context = null; // TODO: Add a weak reference?

    private AlertDialog.Builder builder;
    private AlertDialog getWeatherDialog;
    private AlertDialog currentWeatherDialog;
    private String serverResponse;
    private JSONObject jsonObject;
    // Constructor to pass context
    DownloadWeatherTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create dialogs
        builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_get_weather_layout);
        getWeatherDialog = builder.create();
        builder.setView(R.layout.dialog_current_weather_layout);
        currentWeatherDialog = builder.create();

        getWeatherDialog.show();
    }

    @Override
    protected String doInBackground(String... url) {
        String weatherUrl = url[0];
        String imageUrl = url[1];
        HttpURLConnection connection = null;
        weatherUrl = weatherUrl.concat("?q=Stockholm"); //TODO: REMOVE TEST
        boolean weatherGetSuccess = false;
        try {
            // Establish connection to weather site
            URL forecastURL = new URL(weatherUrl); // TODO: Rename forecastURL and/or weatherUrl
            connection = (HttpURLConnection) forecastURL.openConnection();
            connection.connect();

            // Get the response
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            serverResponse = bufferedReader.readLine();

            // Convert response to json object for ease of use.
            JSONObject jsonObject = new JSONObject(serverResponse);

            // Check if connection was successful before
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                weatherGetSuccess = true;
            }

        } catch (Exception e) {
            Log.d("Exception message", e.toString());
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        // If weather was successfully returned, load image.
        if(weatherGetSuccess) {
            try {
                // Parse the icon value to end of URL.
                JSONObject weather = jsonObject.getJSONObject("weather");
                imageUrl = imageUrl + jsonObject.getString("icon") + ".png";

                // Establish connection to weather site
                URL forecastURL = new URL(imageUrl); // TODO: Rename forecastURL and/or weatherUrl
                connection = (HttpURLConnection) forecastURL.openConnection();
                connection.connect();

                Drawable d = Drawable.createFromStream(forecastURL.openStream(), jsonObject.getString("icon") + ".png");
                // Get the response
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                serverResponse = bufferedReader.readLine();


            } catch (Exception e) {
                Log.d("Exception message", e.toString());
            }
            finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        getWeatherDialog.hide();
        Log.d("SERVER RESPONSE", serverResponse);
        currentWeatherDialog.setTitle(serverResponse);
        currentWeatherDialog.show();
        super.onPostExecute(s);
    }
}
