package com.example.transitnotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "channel_1";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel(){

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel pushNotifications = new NotificationChannel(
                    CHANNEL_ID,
                    "time_notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            pushNotifications.setDescription("push notifications for train times");

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            pushNotifications.setSound(sound, audioAttributes);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(pushNotifications);
        }
    }
}
