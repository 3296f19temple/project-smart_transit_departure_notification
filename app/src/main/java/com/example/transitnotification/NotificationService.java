package com.example.transitnotification;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

class NotificationService extends IntentService {

    public NotificationService(){
        super("NotificationService");
    }

    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        notificationTimer(extras.getString("ROUTE"),
                extras.getString("DIRECTION"),
                extras.getString("STATION"),
                extras.getString("TIME"));
    }

    public void notificationTimer(String route, String direction, String station, final String arrival_str) {

        Calendar arrival_time = stringToCalendar(arrival_str);
        Calendar curr_time = Calendar.getInstance();
        int hour_offset, min_offset, mins_depart = 0;

        Log.d("notificationTimer", "curr_time.get(AM_PM) = "+curr_time.get(Calendar.AM_PM));
        Log.d("notificationTimer", "arrival_time.get(AM_PM) = "+arrival_time.get(Calendar.AM_PM));

        if(arrival_time.get(Calendar.AM_PM) == curr_time.get(Calendar.AM_PM)){
            hour_offset = arrival_time.get(Calendar.HOUR) - curr_time.get(Calendar.HOUR);
            min_offset = arrival_time.get(Calendar.MINUTE) - curr_time.get(Calendar.MINUTE);
            mins_depart = (hour_offset * 60) + min_offset;
            createNotification(route, direction, station, mins_depart);
        }

        if(mins_depart != 0){

            final String rt = route;
            final String dir = direction;
            final String stat = station;

            Handler handler = new Handler();
            class timer implements Runnable {

                int time;
                public timer(int t){ time = t;}

                @Override
                public void run() {
                    createNotification(rt, dir, stat, time);
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
            handler.postDelayed(new timer(0), mins_depart*60000);

        }

    }

    public Calendar stringToCalendar(String arrival_str){

        Calendar arrival_time = Calendar.getInstance();
        int hour = Integer.parseInt(arrival_str.substring(0,1));
        int min = Integer.parseInt(arrival_str.substring(3,4));
        int am_pm = 0;
        if(arrival_str.substring(5,6).equals("pm")){
            am_pm = 1;
        }

        arrival_time.set(Calendar.HOUR, hour);
        arrival_time.set(Calendar.MINUTE, min);
        arrival_time.set(Calendar.AM_PM, am_pm);

        return arrival_time;
    }

    public void createNotification(String route, String direction, String station, int minutes){

        final String CHANNEL_ID = "push_notifications";
        int NOTIFICATION_ID = 001;
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // create full screen intent
        Intent fullscreenIntent = new Intent();
        PendingIntent fullscreenPendingIntent = PendingIntent.getActivity(this,
                0, fullscreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_directions_subway_black_24dp);
        builder.setContentTitle("SmartTransit");

        if(minutes == 0) {
            builder.setContentText(route + " " + direction + " has arrived at " + station);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(route + " " + direction
                    + " has arrived at " + station));
        } else {

            builder.setContentText(route + " " + direction + " is arriving at " + station + " in "
                    + minutes + " minutes");
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(route + " " + direction
                    + " is arriving at " + station + " in " + minutes + " minutes"));

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
