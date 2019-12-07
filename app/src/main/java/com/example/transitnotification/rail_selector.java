package com.example.transitnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class rail_selector extends AppCompatActivity {

    String line_selected_global = null;
    String from_stop_global = null;
    String to_stop_global = null;
    public static String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rail_selector);

        ImageView logo_img = (ImageView) findViewById(R.id.app_logo);
        logo_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        // populate rail line spinner
        Spinner line_spinner = (Spinner) findViewById(R.id.rail_spinner);
        final ArrayAdapter<CharSequence> line_adapter = ArrayAdapter.createFromResource(this,
                R.array.rail_lines, android.R.layout.simple_spinner_item);
        line_spinner.setAdapter((SpinnerAdapter) line_adapter);

        // determine action for item selection
        line_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String line_selected = (String) line_adapter.getItem(position);
                line_selected_global = line_selected;
                String[] lines = getResources().getStringArray(R.array.rail_lines);
                String[] from_array = null;

                if(line_selected.equals(lines[0])){
                    from_array = getResources().getStringArray(R.array.air_stops);
                }
                else if(line_selected.equals(lines[1])){
                    from_array = getResources().getStringArray(R.array.che_stops);
                }
                else if(line_selected.equals(lines[2])){
                    from_array = getResources().getStringArray(R.array.chw_stops);
                }
                else if(line_selected.equals(lines[3])){
                    from_array = getResources().getStringArray(R.array.cyn_stops);
                }
                else if(line_selected.equals(lines[4])){
                    from_array = getResources().getStringArray(R.array.fox_stops);
                }
                else if(line_selected.equals(lines[5])){
                    from_array = getResources().getStringArray(R.array.lan_stops);
                }
                else if(line_selected.equals(lines[6])){
                    from_array = getResources().getStringArray(R.array.nor_stops);
                }
                else if(line_selected.equals(lines[7])){
                    from_array = getResources().getStringArray(R.array.med_stops);
                }
                else if(line_selected.equals(lines[8])){
                    from_array = getResources().getStringArray(R.array.pao_stops);
                }
                else if(line_selected.equals(lines[9])){
                    from_array = getResources().getStringArray(R.array.tre_stops);
                }
                else if(line_selected.equals(lines[10])){
                    from_array = getResources().getStringArray(R.array.war_stops);
                }
                else if(line_selected.equals(lines[11])){
                    from_array = getResources().getStringArray(R.array.wtr_stops);
                }
                else if(line_selected.equals(lines[12])){
                    from_array = getResources().getStringArray(R.array.wil_stops);
                }

                PopulateFromSpinner(from_array);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button goButton = findViewById(R.id.goBtn);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveRailTimesTask(rail_selector.this).execute(from_stop_global, to_stop_global);
            }
        });

    }

    private void PopulateFromSpinner(final String[] from_array) {
        Spinner from_spinner = (Spinner) findViewById(R.id.from_spinner);
        final ArrayAdapter<String> from_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, from_array);
        from_spinner.setAdapter((SpinnerAdapter) from_adapter);

        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String from_selected = (String) from_adapter.getItem(position);
                from_stop_global = from_selected;

                //String[] to_array = PopulateToStringArray(from_array, from_selected);
                PopulateToSpinner(from_array, from_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private String[] PopulateToStringArray(String[] from_array, String from_selected){
        String[] to_array = new String[from_array.length - 1];
        for(int i = 0, j = 0; i < from_array.length; i++, j++){
            if(from_array[j].equals(from_selected)){
                j--;
            }
            else{
                to_array[j] = from_array[i];
                Log.d("ToStringArray","to_array[j] = "+to_array[j]);
            }
        }

        return to_array;
    }

    private void PopulateToSpinner(String[] to_array, final String fromStop) {

        Spinner to_spinner = (Spinner) findViewById(R.id.to_spinner);
        ArrayAdapter<String> to_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, to_array);
        to_spinner.setAdapter((SpinnerAdapter) to_adapter);

        //TODO: Change below to buttonListener
        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //An item was selected. We retrieve the selected item using
                String toStop = (String) parent.getItemAtPosition(pos);
                to_stop_global = toStop;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getArrivalTime() {

        Log.d("rail_selector", "enter getArrivalTime()");

        Intent notificationIntent = new Intent(rail_selector.this, NotificationService.class);
        Bundle extras = new Bundle();
        extras.putString("ROUTE", line_selected_global);
        extras.putString("TO", to_stop_global);
        extras.putString("FROM", from_stop_global);
        extras.putString("TIME", time);

        notificationIntent.putExtras(extras);

        Log.d("rail_selector", "Created intent and bundle");

        startService(notificationIntent);
    }
}
