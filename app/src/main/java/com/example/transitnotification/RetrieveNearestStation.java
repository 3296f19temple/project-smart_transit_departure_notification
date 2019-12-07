package com.example.transitnotification;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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

public class RetrieveNearestStation extends AsyncTask<Double, Void, String> {
    public boolean executing = false;
    @Override
    protected String doInBackground(Double... doubles) {
        executing = true;
        try{

            //connect to api endpoint
            //http://www3.septa.org/hackathon/locations/get_locations.php?lon=-75.33299748&lat=40.11043326&radius=3
            URL url = new URL("http://www3.septa.org/hackathon/locations/get_locations.php?lon=" + doubles[0] + "&lat=" + doubles[1] + "&location_type=rail_stations");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //put information into a string variable: line
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String station;
                while ((station = bufferedReader.readLine()) != null) {
                    stringBuilder.append(station).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString(); //returns response, move to onPostExecute
            }
            finally{
                urlConnection.disconnect();
            }
        }catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
    protected void onPostExecute(String response) {
        if(response == null) {
            response = "THERE WAS AN ERROR";
        }
        //Log.i("INFO", response);
        //a list where we'll keep the stop names we'll use to populate the above spinners
        ArrayList<String> stationNames = new ArrayList<String>();

        //turn response(string) into JSON Object which we extract desired values from
        try {
            JSONArray stopList = (JSONArray) new JSONTokener(response).nextValue();
            //Iterator<String> keys = JSONList.keys();
            //JSONArray stopList = JSONList.getJSONArray(keys.next());

            JSONObject stop = stopList.getJSONObject(0);
            String location_name = stop.getString("location_name");
            String location_type = stop.getString("location_type");

            MainActivity.nearestStationInfo = "nearest stop: " + location_name + " station type: " + location_type;
            Log.i("NAME", MainActivity.nearestStationInfo);
            //TODO: Handle time variable

        } catch (JSONException e) {
            // Appropriate error handling code
        }

        executing = false;
    }
}
