package com.example.transitnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity{

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

//        //Anonymous on click function that redirects to the bus selector activity
//        ImageView bus_img = (ImageView) findViewById(R.id.bus_icon);
//        bus_img.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent myIntent = new Intent(getBaseContext(), bus_selector.class);
//                startActivity(myIntent);
//            }
//        });

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
