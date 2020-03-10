package com.ninji.basiccalendar;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.model.Alarm;
import com.ninji.basiccalendar.model.Event;


public  class MastaweshaBrodcastReciver extends BroadcastReceiver {
    public static final String KEY_DATE ="com.ninji.basiccalendar.MastaweshaBrodcastReciver.key_date" ;

    private MastaweshaNotficationManager mNotificationUtils;
    private String mMessage;
    private Notification.Builder mNb;
    private int mId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ALARM_RING" , "Riniging");

        AppDatabse db = AppDatabse.getInstance(context);
        mNotificationUtils = new MastaweshaNotficationManager(context);


        String type = intent.getStringExtra("type");

        if ( type.equals("add5min")){
            Toast.makeText(context, "Alarm will ring again in 30 min" , Toast.LENGTH_LONG).show();
        } else if ( type.equals("alarm")) {
            mId = intent.getIntExtra("id", -1);
            Alarm alarm = db.mDatabaseDao().getAlarmWithID(mId);
            mMessage = alarm.getTitle();
            mNb = mNotificationUtils.
                    getAlarmChannelNotfication("ማስታወሻ + alarm", mMessage );

        } else {
            // this is an event reminder

            String id = intent.getStringExtra(KEY_DATE);
            Event event = db.mDatabaseDao().getEventWithID(id);
            mMessage =event.getEventDescription();
            String eventTitle = event.getEventTitle() ;
            mNb = mNotificationUtils.
                    getAlarmChannelNotfication(eventTitle, mMessage );

        }

        mNotificationUtils.getManager().notify(101, mNb.build());

    }
}
