package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
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

public class WeatherLoader extends AsyncTaskLoader<List<String>> {

    // Urls for getting weather data.
    private String weatherDataUrl;
    private String imageUrl;
    // Return string containing weather data.
    private List<String> weatherData = new ArrayList<>();
    private String imageName;

    public WeatherLoader(@NonNull Context context, String weatherDataUrl, String imageUrl) {
        super(context);
        this.weatherDataUrl = weatherDataUrl;
        this.imageUrl = imageUrl;
        Log.d("LOADER CLASS", "constructor()");
        Log.d("DATA URL", weatherDataUrl);
        Log.d("IMG URL", imageUrl);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        //boolean downloadSuccess = downloadForecast(weatherDataUrl);
        Log.d("LOADER CLASS", "loadInBackground()");
        // If we can get weather data, get the image.
        if (downloadForecast(weatherDataUrl)) {
            downloadWeatherImage(imageUrl);
        }
        return weatherData;
    }

    private boolean downloadForecast(String weatherUrl) {
        boolean result = false;
        HttpURLConnection connection = null;
        try {
            // Establish connection to weather site
            URL forecastURL = new URL(weatherUrl);
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
            // Add description to result list.
            weatherData.add(weatherObj.getString("description"));

            imageName = weatherObj.getString("icon") + ".png";
            // Parse temp
            JSONObject mainObj = jsonWeatherObject.getJSONObject("main");
            double weatherTemp = mainObj.getDouble("temp");
            // Add temp to result list.
            weatherData.add(Double.toString(weatherTemp));

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
            InputStream inStream = imageURL.openStream();
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            //byte[] imageBytes = new byte[1024];
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
