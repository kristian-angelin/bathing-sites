package se.miun.kran1800.dt031g.bathingsites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Activity for showing Google maps and displaying markers for bathing sites.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    // Objects for handling location changes.
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private BathingSite[] bathingSites;     // Stores all bathingsSites from database
    private ArrayList<Marker> markerArray = new ArrayList<>();   // Saves all markers for easy show/hide

    private int ViewRadius;         // View distance from device
    boolean loadedSites = false;    // Used the report that async task has loaded database files.
    private Location lastLocation;  // Used for checking if location has changed.
    private Circle circle;          // Visual indicator for marker showing range

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lastLocation = new Location("");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
    }

    // Check permissions since they might change during runtime.
    @Override
    protected void onResume() {
        super.onResume();
        if (locationPermissionGranted()) {
            setupLocationCallback();
            startLocationUpdates();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get radius from preferences times 1000 to convert to km.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ViewRadius = sharedPreferences.getInt(
                "map_distance_to_show_sites",
                getResources().getInteger(R.integer.default_map_distance)) * 1000;

        // Create the circle with temporary position.
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(0,0))
                .radius(ViewRadius)
                .strokeColor(Color.RED));

        // Check if location permission is granted.
        if(locationPermissionGranted()) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(this, getString(R.string.location_permission_not_granted), Toast.LENGTH_LONG).show();
            circle.setVisible(false);
        }
        // Prepare and load markers from database.
        setupCustomMarker();
        new LoadSiteMarkers().execute();
    }

    // Setup location request.
    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Start receiving updates.
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    // Callback setup for location changes.
    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    // Only run if location has changed.
                    if(location.getLatitude() != lastLocation.getLatitude() &&
                            location.getLongitude() != lastLocation.getLongitude()) {
                        lastLocation = location; // Update last location.

                        // Move circle to center of device locatin.
                        circle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
                        updateMarkers(location);

                        // Update camera position.
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(
                                new LatLng(location.getLatitude(), location.getLongitude())));
                    }
                    // Only called once after sites are loaded to update markers and set camera.
                    // Will zoom camera when starting map activity.
                    if(loadedSites) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()),
                                getResources().getInteger(R.integer.map_camera_start_zoom)));
                        updateMarkers(location);
                        loadedSites = false;
                        if(!circle.isVisible()) {
                            circle.setVisible(true);
                        }
                    }
                }
            }
            // Loop through all markers and make visible depending on distance to location.
            private void updateMarkers(Location location) {
                for(Marker marker : markerArray) {
                    Location markerLocation = new Location("");
                    markerLocation.setLatitude(marker.getPosition().latitude);
                    markerLocation.setLongitude(marker.getPosition().longitude);
                    // If inside circle radius, show marker
                    if(location.distanceTo(markerLocation) < ViewRadius) {
                        marker.setVisible(true);
                    }
                    else {
                        marker.setVisible(false);
                    }
                }
            }
        };
    }

    // Check if we have permission to use location services
    private boolean locationPermissionGranted () {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setupCustomMarker() {
        // Creates a custom layout for the marker to enable multiple rows.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.map_marker_info_window, null);

                TextView title = view.findViewById(R.id.marker_title);
                TextView text = view.findViewById(R.id.marker_text);

                title.setText(marker.getTitle());
                text.setText(marker.getSnippet());

                return view;
            }
        });
    }

    // Loads all call locations from database and prints them to map.
    public class LoadSiteMarkers extends AsyncTask<String, Integer, String> {
        BathingSiteDatabase database;

        @Override
        protected String doInBackground(String... strings) {
            try {
                // Load phone call from database
                database = BathingSiteDatabase.getInstance(getApplicationContext());
                bathingSites = database.bathingSiteDao().getAllBathingsSites();
            } catch (Exception e) {
                Log.d("Exception message", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Loop through all sites and place on map
            for(BathingSite site : bathingSites) {
                // Create marker
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(site.latitude, site.longitude))
                        .visible(false)
                        .title(site.name)
                        .snippet(getString(R.string.form_description) + ": " + site.description + "\n" +
                                getString(R.string.form_address) + ": " + site.address + "\n" +
                                getString(R.string.form_latitude) + ": " + site.latitude + "\n" +
                                getString(R.string.form_longitude) + ": " + site.longitude + "\n" +
                                getString(R.string.form_grade) + ": " + site.grade + "\n" +
                                getString(R.string.form_water_temp) + ": " + site.waterTemp + "\n" +
                                getString(R.string.form_date_for_temp) + ": " + site.dateForTemp));
                markerArray.add(marker);
            }
            loadedSites = true;
        }
    }
}
