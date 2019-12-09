package com.example.transitnotification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class favorites extends AppCompatActivity {

    private Button delete_fave_button;
    private Button add_fave_button;
    String yourFileName = "fave_stops.txt";
    ArrayList<String> list_for_table=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public String[] getFavorites()
    {
        StringBuilder text = new StringBuilder();

        //READING favorite stops from file
        try {
            FileInputStream fIS = getApplicationContext().openFileInput(yourFileName);
            InputStreamReader isr = new InputStreamReader(fIS, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line;

            while ((line = br.readLine()) != null) {
                text.append(line + '&');
            }
            br.close();
        } catch (IOException e) {
            Log.e("Error!", "Error occured while reading text file from Internal Storage!");
        }

        String fave_stops_str = text.toString();
        Log.i("FAVE STOP STRING: ", fave_stops_str);

        //tokenizes string to remove spaces present in text file to separate stop names
        StringTokenizer strTok = new StringTokenizer(fave_stops_str, "&");
        String[] fave_stops = new String[strTok.countTokens()];
        int count = 0;
        while(strTok.hasMoreTokens())
        {
            fave_stops[count++] = strTok.nextToken();
        }

        return fave_stops;
    }

    public void deleteFavorite(String lineToRemove)
    {
        File inputFile = new File(getApplicationContext().getFilesDir(), "fave_stops.txt");
        File tempFile = new File(getApplicationContext().getFilesDir(),"myTempFile.txt");

        try {
            boolean is_inside_for_deletion = false;
            Scanner check_for_duplicate = new Scanner(inputFile);

            while(check_for_duplicate.hasNextLine()){
                String line = check_for_duplicate.nextLine();
                if(line.equals(lineToRemove))
                {
                    is_inside_for_deletion = true;
                }
            }

            if(is_inside_for_deletion) //if there to delete
            {

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String currentLine;

                while((currentLine = reader.readLine()) != null)
                {

                    // trim newline when comparing with lineToRemove
                    String trimmedLine = currentLine.trim();

                    if(trimmedLine.equals(lineToRemove)) continue;

                    writer.write(currentLine + System.getProperty("line.separator"));
                }

                writer.close();
                reader.close();

                boolean successful = tempFile.renameTo(inputFile);

                Log.i("SUCCESS", successful + " - DELETION - " + lineToRemove + " deleted from fave_stops.txt");
            }
            else
            {
                Log.i("NOT DELETED", "No need for deletion; station is not inside file.");
            }
        }

        catch (FileNotFoundException e)
        {
            Log.i("FILE EXCEPTION", "File not found exception.");
        }
        catch (IOException e) {
            Log.i("IO EXCEPTION", "IO exception.");
        }
    }

    public void populateFavorites(String time)
    {
        String[] favorites = getFavorites();
        int iterator;

        for(iterator = 0; iterator < favorites.length; iterator++)
        {
            Log.i("SS: ", makeFaveCard(favorites[iterator]));
            adapter.add(makeFaveCard(favorites[iterator]));
        }

    }

    public String makeFaveCard(String station, String time)
    {
        String station_string = "";

        //append all station information to card
        //name
        station_string = station_string.concat(station + "\n");

        //time
        station_string = station_string.concat("Next train arrives at " + time + "\n");

        return station_string;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        String[] favorite_stops = getFavorites();


        ImageView logo_img = (ImageView) findViewById(R.id.app_logo);
        logo_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list_for_table);

        ListView listView = (ListView) findViewById(R.id.stations_list);
        listView.setAdapter(adapter);

        populateFavorites();

        //populate spinner with different line types: Rail, Bus, Subway
        Spinner type_spinner = (Spinner) findViewById(R.id.favorite_type_spinner);
        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(this,
                R.array.line_types_array, android.R.layout.simple_spinner_item);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type_spinner.setAdapter(type_adapter);

        //Automate selection to populate line selector
        type_spinner.setOnItemSelectedListener(new ItemSelectedListener());

        add_fave_button = findViewById(R.id.fave_button);
        add_fave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Spinner favorite_station_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                String fave = favorite_station_spinner.getSelectedItem().toString();

                new RetrieveRailStopTimesTask(favorites.this).execute("Suburban Station");
                //Log.i("FAVE: ", fave);

                try {
                    /* File myFile = new File(Environment.getExternalStorageDirectory().getPath()+"/fave_stops.txt"); */
                    File file = new File(getApplicationContext().getFilesDir(), "fave_stops.txt");
                    boolean is_inside = false;

                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    Scanner check_for_duplicate = new Scanner(file);

                    while(check_for_duplicate.hasNextLine()){
                        String line = check_for_duplicate.nextLine();
                        if(line.equals(fave))
                        {
                            is_inside = true;
                        }
                    }

                    if(is_inside)
                    {
                        Log.i("ALREADY FAVE: ", fave);
                    }
                    else
                    {
                        try {
                            Log.i("WRITE TO FILE: ", fave);
                            FileWriter writer = new FileWriter(file, true);
                            writer.write(fave + "\n");
                            writer.flush();
                            writer.close();

                        } catch (Exception e) {
                            Log.i("FAVE ADD CATCH1: ", fave);
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e) {
                    Log.i("FAVE ADD CATCH2: ", fave);
                    e.printStackTrace();
                }

                Log.i("AFTER ADD: ", fave);
            }
        });

        delete_fave_button = findViewById(R.id.delete_button);
        delete_fave_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Spinner favorite_station_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                String fave = favorite_station_spinner.getSelectedItem().toString();

                //Log.i("FAVE: ", fave);

                try {
                    deleteFavorite(fave);
                }
                catch (Exception e) {
                    Log.i("Delete", "ONCLICK delete catch");
                    e.printStackTrace();
                }

                Log.i("AFTER DELETE: ", "Deleted");
            }
        });
    }

    //used to dynamically respond when selecting item: can be used on ALL spinners
    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        String selected;

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            selected = parent.getItemAtPosition(pos).toString();

            Log.i("Selected", selected);

            // ### LINE NAME SPINNER POPULATION BEGINS ###

            switch(selected)
            {
                // ### LINE NAME SPINNER POPULATION BEGINS ###
                case("Rail"):
                    Log.i("RAIL SHOULD FILL: ", selected);
                    Spinner rail_spinner = (Spinner) findViewById(R.id.favorite_line_spinner);
                    ArrayAdapter<CharSequence> rail_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.rail_line_array, android.R.layout.simple_spinner_item);
                    rail_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    rail_spinner.setAdapter(rail_adapter);

                    rail_spinner.setOnItemSelectedListener(new ItemSelectedListener());
                    break;

                case("Subway"):
                    Log.i("SUBWAY SHOULD FILL: ", selected);
                    Spinner subway_spinner = (Spinner) findViewById(R.id.favorite_line_spinner);
                    ArrayAdapter<CharSequence> subway_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.subway_line_array, android.R.layout.simple_spinner_item);
                    subway_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subway_spinner.setAdapter(subway_adapter);

                    subway_spinner.setOnItemSelectedListener(new ItemSelectedListener());
                    break;

                // ### STATION SPINNER POPULATION BEGINS ###

                //SUBWAY
                case("Broad Street Line"):
                    //favorite_station_spinner
                    Log.i("BSL SHOULD FILL: ", selected);
                    Spinner bsl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> bsl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.bsl_stop_array, android.R.layout.simple_spinner_item);
                    bsl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bsl_spinner.setAdapter(bsl_adapter);
                    break;

                case("Market Frankford Line"):
                    //favorite_station_spinner
                    Log.i("MFL SHOULD FILL: ", selected);
                    Spinner mfl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> mfl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.mfl_stop_array, android.R.layout.simple_spinner_item);
                    mfl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mfl_spinner.setAdapter(mfl_adapter);
                    break;

                case("Norristown High Speed Line"):
                    Log.i("N HS L SHOULD FILL: ", selected);
                    Spinner nhsl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> nhsl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.nhsl_stop_array, android.R.layout.simple_spinner_item);
                    nhsl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    nhsl_spinner.setAdapter(nhsl_adapter);
                    break;

                //REGIONAL RAIL
                case("Airport Line"):
                    Log.i("APORT LINE SH'LD FILL: ", selected);
                    Spinner aportl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> aportl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.air_stops, android.R.layout.simple_spinner_item);
                    aportl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    aportl_spinner.setAdapter(aportl_adapter);
                    break;

                case "Manayunk/Norristown Line":
                    Log.i("M/N LINE SHOULD FILL: ", selected);
                    Spinner mnl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> mnl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.nor_stops, android.R.layout.simple_spinner_item);
                    mnl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mnl_spinner.setAdapter(mnl_adapter);
                    break;

                case "Chestnut Hill East Line":
                    Log.i("CH E LINE SHOULD FILL: ", selected);
                    Spinner chel_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> chel_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.che_stops, android.R.layout.simple_spinner_item);
                    chel_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chel_spinner.setAdapter(chel_adapter);
                    break;

                case("Media/Elwyn Line"):
                    Log.i("M/E LINE SHOULD FILL: ", selected);
                    Spinner mel_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> mel_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.med_stops, android.R.layout.simple_spinner_item);
                    mel_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mel_spinner.setAdapter(mel_adapter);
                    break;

                case("Chestunut Hill West Line"):
                    Log.i("CH W LINE SHOULD FILL: ", selected);
                    Spinner chwl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> chwl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.chw_stops, android.R.layout.simple_spinner_item);
                    chwl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    chwl_spinner.setAdapter(chwl_adapter);
                    break;

                case("Paoli/Thorndale Line"):
                    Log.i("P/T LINE SHOULD FILL: ", selected);
                    Spinner ptl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> ptl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.pao_stops, android.R.layout.simple_spinner_item);
                    ptl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ptl_spinner.setAdapter(ptl_adapter);
                    break;

                case("Cynwyd Line"):
                    Log.i("CYNWYD L SHOULD FILL: ", selected);
                    Spinner cynl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> cynl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.cyn_stops, android.R.layout.simple_spinner_item);
                    cynl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cynl_spinner.setAdapter(cynl_adapter);
                    break;

                case("Trenton Line"):
                    Log.i("TRENDON L SHOULD FILL: ", selected);
                    Spinner tl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> tl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.tre_stops, android.R.layout.simple_spinner_item);
                    tl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tl_spinner.setAdapter(tl_adapter);
                    break;

                case("Fox Chase Line"):
                    Log.i("FOX C L SHOULD FILL: ", selected);
                    Spinner fcl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> fcl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.fox_stops, android.R.layout.simple_spinner_item);
                    fcl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fcl_spinner.setAdapter(fcl_adapter);
                    break;

                case("Warminster Line"):
                    Log.i("WARMIN L SHOULD FILL: ", selected);
                    Spinner wl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> wl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.war_stops, android.R.layout.simple_spinner_item);
                    wl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wl_spinner.setAdapter(wl_adapter);
                    break;

                case("Glenside Line"):
                    Log.i("GLENS L SHOULD FILL: ", selected);
                    Spinner gl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> gl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.glen_stops, android.R.layout.simple_spinner_item);
                    gl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    gl_spinner.setAdapter(gl_adapter);
                    break;

                case("West Trenton Line"):
                    Log.i("W TRENT L SHOULD FILL: ", selected);
                    Spinner wtl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> wtl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.tre_stops, android.R.layout.simple_spinner_item);
                    wtl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wtl_spinner.setAdapter(wtl_adapter);
                    break;

                case("Lansdale/Doylestown Line"):
                    Log.i("L/D L SHOULD FILL: ", selected);
                    Spinner ldl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> ldl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.lan_stops, android.R.layout.simple_spinner_item);
                    ldl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ldl_spinner.setAdapter(ldl_adapter);
                    break;

                case("Wilmington/Newark Line"):
                    Log.i("W/N L SHOULD FILL: ", selected);
                    Spinner wnl_spinner = (Spinner) findViewById(R.id.favorite_station_spinner);
                    ArrayAdapter<CharSequence> wnl_adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.wil_stops, android.R.layout.simple_spinner_item);
                    wnl_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    wnl_spinner.setAdapter(wnl_adapter);
                    break;


                default:
                    Log.i("CASE DEFAULT: ", selected);
                    break;
            }

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

}
