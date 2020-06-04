package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

public class NewBathingSiteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    private NewBathingSiteActivityFragment bathingSiteForm;
    private GetWeatherDialog getWeatherDialog;
    private String weatherUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bathing_site);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up actionbar
        ActionBar ac = getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        initFormFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_bathing_site, menu);

        return true;
    }

    private void initFormFields() {
        bathingSiteForm = (NewBathingSiteActivityFragment) getSupportFragmentManager()
                            .findFragmentById(
                                    R.id.fragment_new_bathing_site);
    }

    // When clicking menu items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                clearForm();
                return true;
            case R.id.menu_item_save:
                saveForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearForm() {
        bathingSiteForm.clearForm();
    }

    private void saveForm() {
        bathingSiteForm.saveBatingSite();
    }

    public void getCurrentWeather(MenuItem item) {
        SharedPreferences sharedPreferences =  this.getPreferences(Context.MODE_PRIVATE);
        // TODO: REMOVE DEBUG CODE STOCKHOLM
        weatherUrl = sharedPreferences.getString("weather_url", getString(R.string.default_weather_url));
        String imageUrl = getString(R.string.default_weather_icon_url);
        // Check if position is set before getting weather
        if(bathPositionSet()) {
            Bundle argsBundle = new Bundle();
            argsBundle.putString("weatherUrl", weatherUrl);
            argsBundle.putString("imageUrl", imageUrl);

            getWeatherDialog = GetWeatherDialog.newInstance();
            getWeatherDialog.show(getSupportFragmentManager(), "getWeatherDialog");
            Log.d("START CLASS", "before initLoader");
            getSupportLoaderManager().restartLoader(0, argsBundle, this);
        }
    }

    // Check if either address or lat/long is set. Return
    private boolean bathPositionSet() {
        String address = bathingSiteForm.getPositionDataAsHttpGet();
        //String[] latLong = bathingSiteForm.getLatLong();
        if(address != null) {
            weatherUrl = weatherUrl.concat(address);
            weatherUrl = sanitizeWebAddress(weatherUrl);
            return true;
        }
        return false;
    }

    private String sanitizeWebAddress(String url) {
        url = url.replace('å', 'a');
        url = url.replace('Å', 'A');
        url = url.replace('ä', 'a');
        url = url.replace('Ä', 'A');
        url = url.replace('ö', 'o');
        url = url.replace('Ö', 'O');
        url = url.replace(" ", "");
        Log.d("EDITED URL", url);
        return url;
    }

    public void startSettingsActivity (MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("START CLASS", "onCreateLoader()");
        return new WeatherLoader(this, args.getString("weatherUrl"), args.getString("imageUrl"));
    }

    // Will return info about weather as String[0 = description; 1 = temp; 2 = png as Base64]
    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
        if(getWeatherDialog != null) {
            getWeatherDialog.dismiss();
        }
        if(data == null) {
            bathingSiteForm.SetErrorPosDoesNotExist();
        }
        else {
            String description = data.get(0);
            String temp = data.get(1);
            String base64Image = data.get(2);

            //showWeatherDialog
            ShowWeatherDialog showWeatherDialog = ShowWeatherDialog.newInstance(description, temp, base64Image);
            showWeatherDialog.show(getSupportFragmentManager(), "showWeatherDialog");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {

    }
}
