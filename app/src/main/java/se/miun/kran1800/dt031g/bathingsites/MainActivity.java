package se.miun.kran1800.dt031g.bathingsites;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;


// TODO: Go through and check if any public things can be set private instead.
public class MainActivity extends AppCompatActivity {

    final int ACCESS_LOCATION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("START", "MAIN CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.addBathSiteButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewBathingSiteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void startSettingsActivity (MenuItem menuItem) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startDownloadActivity (MenuItem menuItem) {
        Intent intent = new Intent(this, DownloadActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String url = sharedPreferences.getString("download_bathing_sites_url", getString(R.string.default_download_url));
        intent.putExtra("url", url);
        startActivity(intent);
    }

    // Try to start map activity by checking permissions first.
    public void startMapActivity (MenuItem menuItem) {
        // If permission has been granted
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else {
            requestLocationPermission();
        }
    }

    // Request location permission
    private void requestLocationPermission() {
        // If permission has been denied before, show dialog with explanation.
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.title_permission_required))
                    .setMessage(getString(R.string.message_location_required))
                    .setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                        // Click ok to make request again.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_CODE);
                        }
                    })
                    .setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                        // Click cancel to dismiss dialog.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
        // Ask for permission.
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_CODE);
        }
    }

    // Check result of permissions dialog
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ACCESS_LOCATION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
