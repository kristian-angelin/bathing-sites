package se.miun.kran1800.dt031g.bathingsites;

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
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

/**
 * Activity for letting user create a new bathing site.
 * Has a form for taking input.
 * Also shows bathing site fragment in landscape mode.
 * Implements LoaderCallbacks to create asyncTaskLoader for downloading in separate thread.
 */
public class NewBathingSiteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    private NewBathingSiteActivityFragment bathingSiteForm;
    private DownloadingDialog downloadingDialog;
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
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(this);

        weatherUrl = sharedPreferences.getString("weather_url", getString(R.string.default_weather_url));
        String imageUrl = getString(R.string.default_weather_icon_url);
        // Check if position is set before getting weather
        if(bathPositionSet()) {
            Bundle argsBundle = new Bundle();
            argsBundle.putString("weatherUrl", weatherUrl);
            argsBundle.putString("imageUrl", imageUrl);

            // Create and show download dialog and start download process.
            downloadingDialog = DownloadingDialog.newInstance(getString(R.string.downloading_weather_message));
            downloadingDialog.show(getSupportFragmentManager(), "downloadWeatherDialog");
            getSupportLoaderManager().restartLoader(0, argsBundle, this);
        }
    }

    // Check if either address or lat/long is set. Return
    private boolean bathPositionSet() {
        String address = bathingSiteForm.getPositionDataAsHttpGet();
        if(address != null) {
            weatherUrl = weatherUrl.concat(address);
            weatherUrl = sanitizeWebUrl(weatherUrl);
            return true;
        }
        return false;
    }

    // Cleaning up web address from swedish characters and spaces.
    private String sanitizeWebUrl(String url) {
        url = url.replace('å', 'a');
        url = url.replace('Å', 'A');
        url = url.replace('ä', 'a');
        url = url.replace('Ä', 'A');
        url = url.replace('ö', 'o');
        url = url.replace('Ö', 'O');
        url = url.replaceAll(" ", "");
        return url;
    }

    public void startSettingsActivity (MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //--------------- LoaderCallbacks required methods ---------------------

    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {
        return new LoadWeatherTask(this, args.getString("weatherUrl"), args.getString("imageUrl"));
    }

    // Will return info about weather as String[0 = description; 1 = temp; 2 = png as Base64]
    @Override
    public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
        // If download dialog exist, dismiss it.
        if(downloadingDialog != null) {
            downloadingDialog.dismiss();
        }
        if(data.isEmpty()) {
            bathingSiteForm.SetErrorPosDoesNotExist();
        }
        else {
            String description = data.get(0);
            String temp = data.get(1);
            String base64Image = data.get(2);

            // Show weather dialog.
            ShowWeatherDialog showWeatherDialog = ShowWeatherDialog.newInstance(description, temp, base64Image);
            showWeatherDialog.show(getSupportFragmentManager(), "showWeatherDialog");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {}
}
