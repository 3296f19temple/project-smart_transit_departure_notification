package com.example.transitnotification;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. We retrieve the selected item using
        String choice = (String) parent.getItemAtPosition(pos);
        //get route_id and pass it to method that will call api
        String[] routeNum = choice.split(" ");
        new RetrieveStopsTask().execute(routeNum[1]);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

