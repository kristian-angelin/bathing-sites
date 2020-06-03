package se.miun.kran1800.dt031g.bathingsites;

/*import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Download weather and display dialog.
public class DownloadWeatherTask extends AsyncTask<String, String, String> {

    private Context context;

    private AlertDialog getWeatherDialog;

    // Data fields from weather download
    private String imageName;
    private String weatherDescription;
    private double weatherTemp;

    private Drawable weatherImage; // Image for weather dialog

    // Constructor to pass context
    DownloadWeatherTask (Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create download dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_get_weather_layout);
        getWeatherDialog = builder.create();
        getWeatherDialog.show();
    }

    @Override
    protected String doInBackground(String... url) {
        // Save url strings.
        String forecastUrl = url[0];
        String imageUrl = url[1];

        boolean downloadSuccess = downloadForecast(forecastUrl);

        // If weather data was successfully returned, load image.
        if(downloadSuccess) {
            downloadWeatherImage(imageUrl);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        getWeatherDialog.hide();
        showForecastDialog();
    }

    // Download forecast and return true if succeeded.
    private boolean downloadForecast(String forecastUrl) {
        boolean result = false;
        HttpURLConnection connection = null;
        forecastUrl = forecastUrl.concat("?q=Stockholm"); //TODO: REMOVE TEST
        try {
            // Establish connection to weather site
            URL forecastURL = new URL(forecastUrl);
            connection = (HttpURLConnection) forecastURL.openConnection();
            connection.connect();

            // Get the request response.
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String serverResponse = bufferedReader.readLine();

            // Convert response to json object and parse it.
            parseWeatherData(new JSONObject(serverResponse));

            // Check if the request returned ok.
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = true;
            }

        } catch (Exception e) {
            Log.d("Exception message", e.toString());
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    // Takes weather JSON data and parse correct variable types.
    private void parseWeatherData(JSONObject jsonWeatherObject) {
        // Parse the icon value to end of URL.
        try {
            // Parse description and image name.
            JSONArray weatherArray = jsonWeatherObject.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            weatherDescription = weatherObj.getString("description");
            imageName = weatherObj.getString("icon") + ".png";

            // Parse temp
            JSONObject mainObj = jsonWeatherObject.getJSONObject("main");
            weatherTemp = mainObj.getDouble("temp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Download weather image.
    private void downloadWeatherImage(String imageUrl) {
        try {
            // File name to end of URL String.
            imageUrl = imageUrl + imageName;

            // Create URL object and open stream.
            URL imageURL = new URL(imageUrl);
            weatherImage = Drawable.createFromStream(imageURL.openStream(), imageName);

        } catch (Exception e) {
            Log.d("Exception message", e.toString());
        }
    }

    // Create and show forecast dialog
    private void showForecastDialog() {
        // Load layout into view to enable editing
        LayoutInflater inflater = LayoutInflater.from(context);
        View weatherView = inflater.inflate(R.layout.dialog_current_weather_layout, null);

        // Set dialog attributes
        TextView description = weatherView.findViewById(R.id.current_weather_description);
        description.setText(weatherDescription);

        TextView temp = weatherView.findViewById(R.id.current_weather_temp);
        temp.setText(String.valueOf(weatherTemp));

        ImageView image = weatherView.findViewById(R.id.current_weather_image);
        image.setImageDrawable(weatherImage);

        // Set view and create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(weatherView);
        AlertDialog currentWeatherDialog = builder.create();
        currentWeatherDialog.show();
    }
} public class DownloadWeatherTask extends AsyncTask<String, String, String> {

    private Context context;

    private AlertDialog getWeatherDialog;
    private AlertDialog showWeatherDialog;

    // Data fields from weather download
    private String imageName;
    private String weatherDescription;
    private double weatherTemp;

    private Drawable weatherImage; // Image for weather dialog

    // Constructor to pass context and the AlertDialogs.
    DownloadWeatherTask(Context context, AlertDialog getWeatherDialog, AlertDialog showWeatherDialog) {
        this.context = context;
        this.getWeatherDialog = getWeatherDialog;
        this.showWeatherDialog = showWeatherDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Create download dialog
            /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(R.layout.dialog_get_weather_layout);
            getWeatherDialog = builder.create();
        getWeatherDialog.show();
    }

    @Override
    protected String doInBackground(String... url) {
        // Save url strings.
        String forecastUrl = url[0];
        String imageUrl = url[1];

        boolean downloadSuccess = downloadForecast(forecastUrl);

        // If weather data was successfully returned, load image.
        if (downloadSuccess) {
            downloadWeatherImage(imageUrl);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        getWeatherDialog.hide();
        showForecastDialog();
    }

    // Download forecast and return true if succeeded.
    private boolean downloadForecast(String forecastUrl) {
        boolean result = false;
        HttpURLConnection connection = null;
        forecastUrl = forecastUrl.concat("?q=Stockholm"); //TODO: REMOVE TEST
        try {
            // Establish connection to weather site
            URL forecastURL = new URL(forecastUrl);
            connection = (HttpURLConnection) forecastURL.openConnection();
            connection.connect();

            // Get the request response.
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String serverResponse = bufferedReader.readLine();

            // Convert response to json object and parse it.
            parseWeatherData(new JSONObject(serverResponse));

            // Check if the request returned ok.
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = true;
            }

        } catch (Exception e) {
            Log.d("Exception message", e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    // Takes weather JSON data and parse correct variable types.
    private void parseWeatherData(JSONObject jsonWeatherObject) {
        // Parse the icon value to end of URL.
        try {
            // Parse description and image name.
            JSONArray weatherArray = jsonWeatherObject.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);
            weatherDescription = weatherObj.getString("description");
            imageName = weatherObj.getString("icon") + ".png";

            // Parse temp
            JSONObject mainObj = jsonWeatherObject.getJSONObject("main");
            weatherTemp = mainObj.getDouble("temp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Download weather image.
    private void downloadWeatherImage(String imageUrl) {
        try {
            // File name to end of URL String.
            imageUrl = imageUrl + imageName;

            // Create URL object and open stream.
            URL imageURL = new URL(imageUrl);
            weatherImage = Drawable.createFromStream(imageURL.openStream(), imageName);

        } catch (Exception e) {
            Log.d("Exception message", e.toString());
        }w
    }

    // Create and show forecast dialog
    private void showForecastDialog() {
        // Load layout into view to enable editing
        LayoutInflater inflater = LayoutInflater.from(context);
        View weatherView = inflater.inflate(R.layout.dialog_current_weather_layout, null);

        // Set dialog attributes
        TextView description = weatherView.findViewById(R.id.current_weather_description);
        description.setText(weatherDescription);

        TextView temp = weatherView.findViewById(R.id.current_weather_temp);
        temp.setText(String.valueOf(weatherTemp));

        ImageView image = weatherView.findViewById(R.id.current_weather_image);
        image.setImageDrawable(weatherImage);

        // Set view and create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(weatherView);
        showWeatherDialog = builder.create();
        showWeatherDialog.show();
    }
}*/