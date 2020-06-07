package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * AsyncTaskLoader for loading weather data from two URLs.
 */
public class LoadWeatherTask extends AsyncTaskLoader<List<String>> {

    // Urls for getting weather data.
    private String weatherDataUrl;
    private String imageUrl;
    // Return string containing weather data.
    private List<String> weatherData = new ArrayList<>();
    private String imageName;

    public LoadWeatherTask(@NonNull Context context, String weatherDataUrl, String imageUrl) {
        super(context);
        this.weatherDataUrl = weatherDataUrl;
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        // If we can get weather data, get the image.
        if (downloadWeatherData(weatherDataUrl)) {
            downloadWeatherImage(imageUrl);
        }
        return weatherData;
    }

    // Download current weather from URL and return true if successful.
    private boolean downloadWeatherData(String weatherUrl) {
        boolean result = false;
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            // Establish connection to weather site
            URL forecastURL = new URL(weatherUrl);
            connection = (HttpURLConnection) forecastURL.openConnection();
            connection.connect();

            // Get the request response.
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String serverResponse = bufferedReader.readLine();

            // Convert response to json object and parse it.
            parseWeatherData(new JSONObject(serverResponse));
            // If we cant get weatherData return false
            if(weatherData == null) {
                return false;
            }
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
            // Add description to result list.
            weatherData.add(weatherObj.getString("description"));

            imageName = weatherObj.getString("icon") + ".png";
            // Parse temp and cast to int to remove decimals.
            JSONObject mainObj = jsonWeatherObject.getJSONObject("main");
            int weatherTemp = (int) mainObj.getDouble("temp");
            // Add temp to result list.
            weatherData.add(Integer.toString(weatherTemp));

        } catch (JSONException e) {
            e.printStackTrace();
            weatherData = null;
        }
    }

    // Download weather image from URL.
    private void downloadWeatherImage(String imageUrl) {
        try {
            // File name to end of URL String.
            imageUrl = imageUrl + imageName;

            // Create URL object and open stream.
            URL imageURL = new URL(imageUrl);
            InputStream inStream = imageURL.openStream();
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

            int data;
            while ((data = inStream.read()) != -1) {
                byteArrayOut.write(data);
            }
            byte[] imageBytes = byteArrayOut.toByteArray();
            // Convert byte array of image to string.
            String base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            weatherData.add(base64String);
        } catch (Exception e) {
            Log.d("Exception message", e.toString());
        }
    }
}
