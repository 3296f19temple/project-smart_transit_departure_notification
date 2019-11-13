package temple.edu.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class bus_selector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_selector);

        ImageView logo_img = (ImageView) findViewById(R.id.app_logo);
        logo_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
