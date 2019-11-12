package temple.edu.smarttransit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "push_notifications";
    private int NOTIFICATION_ID = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void DisplayNotification(View view){

        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_directions_subway_black_24dp);
        builder.setContentTitle("SmartTransit");
        builder.setContentText("BSL Southbound is arriving at Susquehanna-Dauphin Station in 10 minutes");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        // display notification
        NotificationManagerCompat nf_mngr = NotificationManagerCompat.from(this);
        nf_mngr.notify(NOTIFICATION_ID, builder.build());
    }
}
