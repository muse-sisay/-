package com.ninji.basiccalendar;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.model.Alarm;

import java.util.Calendar;
import java.util.Date;


public class AddAlarmActivity extends BaseActivity {


    private static final String KEY_ALARM = "alarm_key";
    private EditText mAlarmTitle;
    private Button mAlarmSave;
    private TimePicker mTimePicker;
    Alarm mAlarm ;
    private Boolean mNewAlarm;
    private AppDatabse mDb;
    private Button mBtnDelete;

    public static Intent newIntent( Context context , int id) {
        Intent intent = new Intent(context , AddAlarmActivity.class);
        intent.putExtra( KEY_ALARM , id);
        return intent ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabse.getInstance(this);

        setContentView(R.layout.activity_add_alarm);
        getSupportActionBar().setElevation(0);

        getStoredValues ();
        onBindViews();

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !mNewAlarm){
                    mDb.mDatabaseDao().deleteAlarm(mAlarm);
                    finish();
                } else {
                    finish();
                }

            }
        });

        mAlarmSave.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();
                String alarmTime = hour + " : " + minute ;

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis( System.currentTimeMillis() );

                cal.set(Calendar.HOUR_OF_DAY , hour);
                cal.set(Calendar.MINUTE,minute );

                mAlarm.setTitle(mAlarmTitle.getText().toString());
                mAlarm.setAlarmTime(alarmTime);
                mAlarm.setSet(true);
                mAlarm.setAlarmDate( cal.getTime() );

                if( mNewAlarm){
                    mDb.mDatabaseDao().insertAlarms(mAlarm);
                } else {
                    mDb.mDatabaseDao().updateAlarm(mAlarm);

                }

                // add the title of the alarm
                Toast.makeText(AddAlarmActivity.this , "Alarm Set for " + mAlarm.getTitle() , Toast.LENGTH_LONG).show();

                // the alarm Reciver class will send the notfication
                Intent intent = new Intent( getApplicationContext() , MastaweshaBrodcastReciver.class);
                intent.putExtra("type" , "alarm");
                intent.putExtra("id" , mAlarm.getID());

                // the request code should me modfied with the alaram_id
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext() , mAlarm.getID() ,intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP , cal.getTimeInMillis() , pendingIntent);

                finish();
            }
        });

    }

    private void getStoredValues() {
        Intent intent = getIntent() ;
        int id = intent.getIntExtra(KEY_ALARM , -1);

        mNewAlarm = id == -1;

        if (mNewAlarm) {
            mAlarm = new Alarm();
//            DataManager.getInstance().addAlarm(mAlarm);
//            mDb.mDatabaseDao().insertAlarms(mAlarm);
        } else {
//            mAlarm = DataManager.getInstance().getAlarm(id);
            mAlarm = mDb.mDatabaseDao().getAlarmWithID(id);
        }



    }

    private void onBindViews() {

        mTimePicker = findViewById(R.id.time_picker);
        mAlarmSave = findViewById(R.id.start_alarm);
        mAlarmTitle = findViewById(R.id.et_alarm_title);
        mBtnDelete = findViewById(R.id.btn_delete_alram);


        if ( !mNewAlarm ){
            Date date = mAlarm.getAlarmDate() ;
            int hour = date.getHours();
            int min = date.getMinutes();

            mAlarmTitle.setText( mAlarm.getTitle());
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(min);
        } else {
            mBtnDelete.setEnabled(false);
        }

    }
}
