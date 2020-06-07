package se.miun.kran1800.dt031g.bathingsites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private BathingSite[] bathingSites;
    private ArrayList<Marker> markerArray = new ArrayList<Marker>();
    private int VIEW_RADIUS;

    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        getMarkersFromDatabase();

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        // Get radius from preferences times 1000 to convert to km.
        VIEW_RADIUS = sharedPreferences.getInt(
                "map_distance_to_show_sites",
                getResources().getInteger(R.integer.default_map_distance)) * 1000;

        // Create the circle with tempPos.
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(0,0))
                .radius(VIEW_RADIUS)
                .strokeColor(Color.RED));

        setupLocationCallback();
        startLocationUpdates();

        setupCustomMarker();
        new LoadSiteMarkers().execute();
    }

    private void getMarkersFromDatabase () {

    }

    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    circle.setCenter(new LatLng(location.getLatitude(), location.getLongitude()));
                    if(!circle.isVisible()) {
                        circle.setVisible(true);
                    }
                    // Loop through all markers and show those within
                    for(Marker marker : markerArray) {
                        Location markerLocation = new Location("");
                        markerLocation.setLatitude(marker.getPosition().latitude);
                        markerLocation.setLongitude(marker.getPosition().longitude);
                        if(location.distanceTo(markerLocation) < VIEW_RADIUS) {
                            marker.setVisible(true);
                        }
                        else {
                            marker.setVisible(false);
                        }
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
            // Loop through all calls and place on map
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
        }
    }
}
