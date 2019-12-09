package com.example.transitnotification;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationService extends IntentService {

    public NotificationService(){
        super("NotificationService");
    }

    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        Log.d("NotificationService", "Enter NotificationService");

        notificationTimer(extras.getString("ROUTE"),
                extras.getString("TO"),
                extras.getString("FROM"),
                extras.getString("TIME"));
    }

    public void notificationTimer(String route, String to, String from, final String arrival_str) {

        Calendar arrival_time = stringToCalendar(arrival_str);
        Calendar curr_time = Calendar.getInstance();
        int hour_offset = 0, min_offset, mins_depart = 0;


        Log.d("notificationTimer", "curr_time.get(AM_PM) = "+curr_time.get(Calendar.AM_PM));
        Log.d("notificationTimer", "arrival_time.get(AM_PM) = "+arrival_time.get(Calendar.AM_PM));

        // case when arrival time and current time are both AM or both PM
        if(arrival_time.get(Calendar.HOUR) >= curr_time.get(Calendar.HOUR)){
            hour_offset = arrival_time.get(Calendar.HOUR) - curr_time.get(Calendar.HOUR);
            Log.d("notificationTimer","arrival_time.get(hour) = "+arrival_time.get(Calendar.HOUR));
            Log.d("notificationTimer","curr_time.get(hour) = "+curr_time.get(Calendar.HOUR));
            Log.d("notificationTimer", "hour_offset = "+hour_offset);

            Log.d("notificationTimer","arrival_time.get(min) = "+arrival_time.get(Calendar.MINUTE));
            Log.d("notificationTimer","curr_time.get(min) = "+curr_time.get(Calendar.MINUTE));
        }else if(arrival_time.get(Calendar.HOUR) < curr_time.get(Calendar.HOUR)){
            hour_offset = (arrival_time.get(Calendar.HOUR) + 12) - curr_time.get(Calendar.HOUR);
        }

        min_offset = arrival_time.get(Calendar.MINUTE) - curr_time.get(Calendar.MINUTE);
        Log.d("notificationTimer","min_offset = "+min_offset);
        mins_depart = (hour_offset * 60) + min_offset;
        Log.d("notificationTimer","mins_depart = "+mins_depart);
        createNotification(route, to, from, mins_depart);

        if(mins_depart != 0){

            final String rt = route;
            final String to_station = to;
            final String from_station = from;

            HandlerThread handlerThread = new HandlerThread("background-thread");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
            class timer implements Runnable {

                int time;

                public timer(int t){
                    time = t;
                    Log.d("timer", "time = "+time);
                }

                @Override
                public void run() {
                    Log.d("timer", "enter run()");
                    createNotification(rt, to_station, from_station, time);
                }
            }



            if(mins_depart > 15){
                handler.postDelayed(new timer(15), (mins_depart - 15) * 60000);
            }
            if(mins_depart > 10){
                handler.postDelayed(new timer(10), (mins_depart - 10) * 60000);
            }
            if(mins_depart > 5){
                handler.postDelayed(new timer(5), (mins_depart - 5) * 60000);
            }
            if(mins_depart > 1){
                handler.postDelayed(new timer(1), (mins_depart - 1) * 60000);
            }


            handler.postDelayed(new timer(0), mins_depart * 60000);

        }

    }

    public Calendar stringToCalendar(String arrival_str){

        Calendar arrival_time = Calendar.getInstance();
        int colonPos = arrival_str.indexOf(':');
        Log.d("stringToCalendar", "arrival_str = "+arrival_str);
        Log.d("stringToCalendar", "colonPos = "+colonPos);
        Log.d("stringToCalendar","hourParse = "+arrival_str.substring(0,2));
        int hour;
        String hourStr = arrival_str.substring(0,2);
        if(hourStr.contains(" ")) {
            hour = Integer.parseInt(arrival_str.substring(1,2));
        } else {
            hour = Integer.parseInt(arrival_str.substring(0,2));
        }
        int min = Integer.parseInt(arrival_str.substring(3,5));
        Log.d("stringToCalendar","min = "+arrival_str.substring(3,5));
        int am_pm = 0;

        if(arrival_str.contains("PM")){
            am_pm = 1;
        }

        arrival_time.set(Calendar.HOUR, hour);
        arrival_time.set(Calendar.MINUTE, min);
        arrival_time.set(Calendar.AM_PM, am_pm);

        return arrival_time;
    }

    public void createNotification(String route, String to, String from, int minutes){
        final String CHANNEL_ID = "push_notifications";
        int NOTIFICATION_ID = 001;
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // create full screen intent
        Intent fullscreenIntent = new Intent();
        PendingIntent fullscreenPendingIntent = PendingIntent.getActivity(this,
                0, fullscreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                App.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_directions_subway_black_24dp);
        builder.setContentTitle("SmartTransit");

        if(minutes == 0) {
            builder.setContentText(route + " to " + to + " has arrived at " + from);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(route + " to " + to
                    + " has arrived at " + from));
        } else {

            builder.setContentText(route + " to " + to + " is arriving at " + from + " in "
                    + minutes + " minutes");
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(route + " to " + to
                    + " is arriving at " + from + " in " + minutes + " minutes"));

        }

        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});
        builder.setSound(sound);
        builder.setFullScreenIntent(fullscreenPendingIntent, true);

        // display notification
        NotificationManagerCompat nf_mngr = NotificationManagerCompat.from(this);
        nf_mngr.notify(NOTIFICATION_ID, builder.build());
    }
}
