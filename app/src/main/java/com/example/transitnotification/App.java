package com.example.transitnotification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_ID = "channel_1";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel pushNotifications = new NotificationChannel(
                    CHANNEL_ID,
                    "time_notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            pushNotifications.setDescription("push notifications for train times");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(pushNotifications);
        }
    }
}
