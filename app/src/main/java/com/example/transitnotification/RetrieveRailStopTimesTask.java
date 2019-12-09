package com.example.transitnotification;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

//RetrieveFeedTask calls API in background via asynchronous thread
public class RetrieveRailStopTimesTask extends AsyncTask<String, Void, String> {

    favorites anActivity;

    public RetrieveRailStopTimesTask(favorites a) {
        anActivity = a;
    }

    protected String doInBackground(String... urls) {

        try {
            //connect to api endpoints
            URL url = new URL("http://www3.septa.org/hackathon/Arrivals/" +
                    urls[0].replace(" ", "%20") + "/" + "1");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //put information into a string variable: line
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString(); //returns response, move to onPostExecute
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    protected void onPostExecute(String response) {
        android.os.Debug.waitForDebugger();

        if(response == null) {
            response = "THERE WAS AN ERROR";
        }

        //turn response(string) into JSON Object which we extract desired values from
        try {
            JSONObject stopList = (JSONObject) new JSONTokener(response).nextValue();
            Iterator<String> keys = stopList.keys();
            JSONArray stationList = stopList.getJSONArray(keys.next());

            JSONObject directions = stationList.getJSONObject(0);
            JSONArray direction = directions.getJSONArray("Northbound");
            JSONObject stop = direction.getJSONObject(0);

            String time = stop.getString("sched_time");

            //TODO: Handle time variable
            Log.d("ASYNCTASK", "time = "+time);
            anActivity.buildTimes(time);


        } catch (JSONException | ClassCastException f) {
            // Appropriate error handling code
        }

    }

}


