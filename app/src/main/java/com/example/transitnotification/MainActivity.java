package com.example.transitnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.util.Log;

public class MainActivity extends AppCompatActivity implements LocationListener {
    // THIS SHOULD BE IN A DIFFERENT PLACE THAT STORES GLOBAL VARIABLES
    public static final int REQUEST_FINE_LOCATION = 100;
    protected  LocationManager locationManager;
    public static double latitude = -1, longitude = -1;
    RetrieveNearestStation nearestStation = new RetrieveNearestStation();
    TextView txtLat;
    public static String nearestStationInfo;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        txtLat = (TextView) findViewById(R.id.curLocation);
        LaunchLocationUpdates();

        // Longitude, latitude

        //Anonymous on click function that redirects to the bus selector activity
        /*
        ImageView bus_img = (ImageView) findViewById(R.id.bus_icon);
        bus_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), bus_selector.class);
                startActivity(myIntent);
            }
        });
        */

        //Anonymous on click function that redirects to the subway selector activity
        ImageView subway_img = (ImageView) findViewById(R.id.subway_icon);
        subway_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), subway_selector.class);
                startActivity(myIntent);
            }
        });

        //Anonymous on click function that redirects to the rail selector activity
        ImageView rail_img = (ImageView) findViewById(R.id.rail_icon);
        rail_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), rail_selector.class);
                startActivity(myIntent);
            }
        });

        //Anonymous on click function that redirects to the favorites activity
        ImageView favorites_img = (ImageView) findViewById(R.id.favorites_icon);
        favorites_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), favorites.class);
                startActivity(myIntent);
            }
        });
    }
    private void LaunchLocationUpdates(){
        int check = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        boolean granted = (check == PackageManager.PERMISSION_GRANTED);
        if (!granted) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an app-defined int constant.
            // The callback method gets the result of the requests
        } else {
            // Permission has already been granted
            Log.i("Location", "Location Permission Previously Granted");
            StartLocationUpdates();
        }
    }
    private void StartLocationUpdates(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }catch(SecurityException e){
            Log.e("Latitude", "Failed to get location permission", e);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        txtLat = (TextView) findViewById(R.id.curLocation);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        txtLat.setText("Latitude:" + latitude + ", Longitude:" + longitude);
        RetrieveNearestStation nearestStation = new RetrieveNearestStation();
        if(!nearestStation.executing) {
            nearestStation.execute(longitude, latitude);
        }

    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    Log.i("Location", "Location Permission Granted");
                    StartLocationUpdates();
                } else {
                    // permission denied,
                    Log.i("Location", "Location Permission Denied");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
