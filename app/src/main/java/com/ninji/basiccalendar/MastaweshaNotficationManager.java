package com.ninji.basiccalendar;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;

public class MastaweshaNotficationManager extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ALARM_CHANNEL_ID = "ccom.ninji.basiccalendar.ALARM";
    public static final String REMINDER_CHANNEL_ID = "com.ninji.basiccalendar.REMINDER";
    public static final String ALARM_CHANNEL_NAME = "ማስታወሻ + alarm";
    public static final String REMINDER_CHANNEL_NAME = "ማስታወሻ + reminder";
    private int mColor;

    public MastaweshaNotficationManager(Context base) {
        super(base);
        createChannels();
    }

    public void createChannels() {

        TypedValue typedValue = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(R.attr.backgroundColor , typedValue , true);
        mColor = typedValue.data;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            NotificationChannel androidChannel = new NotificationChannel(ALARM_CHANNEL_ID,
                    ALARM_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(androidChannel);

            // create ios channel
            NotificationChannel iosChannel = new NotificationChannel(REMINDER_CHANNEL_ID,
                    REMINDER_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            iosChannel.enableLights(true);
            iosChannel.enableVibration(true);
            iosChannel.setLightColor(Color.GRAY);
            iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(iosChannel);
        }
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }


    public Notification.Builder getAlarmChannelNotfication(String title, String body) {
        Intent intent = new Intent( getApplicationContext(), MastaweshaBrodcastReciver.class);
        // put date as extra
        intent.putExtra("type", "add5min");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext()  , 0 ,intent , PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), ALARM_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setOnlyAlertOnce(true)
//                    .addAction()
                    .setAutoCancel(true);
        }
        return null ;
    }

    public Notification.Builder getEventChannelNotfication(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), REMINDER_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(mColor)
                    .setSmallIcon(android.R.drawable.stat_notify_more)
                    .setAutoCancel(true);
        }
        return null;
    }

}