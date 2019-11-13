package temple.edu.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Anonymous on click function that redirects to the bus selector activity
        ImageView bus_img = (ImageView) findViewById(R.id.bus_icon);
        bus_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), bus_selector.class);
                startActivity(myIntent);
            }
        });

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
}
