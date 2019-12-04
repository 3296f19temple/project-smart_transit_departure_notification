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
public class RetrieveRailTimesTask extends AsyncTask<String, Void, String> {

    private Exception exception;
    //public AppCompatActivity anActivity;
    TimeSlave t;

    public RetrieveRailTimesTask(TimeSlave a) {
        t = a;
    }

    protected String doInBackground(String... urls) {

        try {
            //connect to api endpoint
            URL url = new URL("http://www3.septa.org/hackathon/NextToArrive/Temple%20University/University%20City");
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
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        Log.i("INFO", response);

        //a list where we'll keep the stop names we'll use to populate the above spinners
        ArrayList<String> stopnames = new ArrayList<String>();

        //turn response(string) into JSON Object which we extract desired values from
        try {
            JSONArray stopList = (JSONArray) new JSONTokener(response).nextValue();
            //Iterator<String> keys = JSONList.keys();
            //JSONArray stopList = JSONList.getJSONArray(keys.next());

            JSONObject stop = stopList.getJSONObject(0);
            String time = stop.getString("orig_arrival_time");

            //TODO: Handle time variable
            Log.d("ASYNCTASK", "time = "+time);
            t.time = time;

        } catch (JSONException | ClassCastException f) {
            // Appropriate error handling code
        }


    }
}

