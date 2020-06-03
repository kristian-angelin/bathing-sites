package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewBathingSiteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    private NewBathingSiteActivityFragment bathingSiteForm;
    private AlertDialog getForecastDialog;
    private AlertDialog showForecastDialog;

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

    @Override
    protected void onDestroy() {
        if(getForecastDialog != null) getForecastDialog.cancel();
        if(showForecastDialog != null) showForecastDialog.cancel();
        super.onDestroy();
    }


    private void initFormFields() {
        bathingSiteForm = (NewBathingSiteActivityFragment) getSupportFragmentManager()
                            .findFragmentById(
                                    R.id.fragment_new_bathing_site);
    }

    // When menu is clicked.
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
        String weatherUrl = sharedPreferences.getString("weather_url", getString(R.string.default_weather_url).concat("?q=Stockholm"));
        String imageUrl = getString(R.string.default_weather_icon_url);

        Bundle argsBundle = new Bundle();
        argsBundle.putString("weatherUrl", weatherUrl);
        argsBundle.putString("imageUrl", imageUrl);

        // Create download dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_get_weather_layout);
        getForecastDialog = builder.create();
        getForecastDialog.show();
        Log.d("START CLASS", "before initLoader");
        getSupportLoaderManager().initLoader(0, argsBundle, this);
        //AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setView(R.layout.dialog_current_weather_layout);
        //showForecastDialog = builder.create();*/

        //new DownloadWeatherTask(this, getForecastDialog, showForecastDialog).execute(weatherUrl, imageUrl);
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
        getForecastDialog.hide();
        LayoutInflater inflater = LayoutInflater.from(this);
        View weatherView = inflater.inflate(R.layout.dialog_current_weather_layout, null);

        // Set dialog attributes
        TextView description = weatherView.findViewById(R.id.current_weather_description);
        description.setText(data.get(0));
        // Set temp
        TextView temp = weatherView.findViewById(R.id.current_weather_temp);
        temp.setText(String.valueOf(data.get(1)));

        // Parse Base64 string to image.
        byte[] byteString = Base64.decode(data.get(2), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(byteString, 0, byteString.length);
        Drawable weatherImage = new BitmapDrawable(getResources(), decodedByte);

        // Set image
        ImageView image = weatherView.findViewById(R.id.current_weather_image);
        image.setImageDrawable(weatherImage);

        // Set view and create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(weatherView);
        showForecastDialog = builder.create();
        showForecastDialog.show();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<String>> loader) {

    }

}
