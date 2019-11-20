package com.example.transitnotification;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SubwaySpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

    public AppCompatActivity anActivity;
    public SubwaySpinnerActivity(AppCompatActivity a) {
        anActivity = a;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. We retrieve the selected item using
        String choice = (String) parent.getItemAtPosition(pos);
        //get route_id and pass it to method that will call api

        int res;
        if (choice.equals("Broad Street Line"))
            res = R.array.bsl_stop_array;
        else
            res = R.array.mfl_stop_array;

        Spinner frmSpinner = (Spinner) anActivity.findViewById(R.id.from_spinner);
        Spinner toSpinner = (Spinner) anActivity.findViewById(R.id.to_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(anActivity,
                res, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frmSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
