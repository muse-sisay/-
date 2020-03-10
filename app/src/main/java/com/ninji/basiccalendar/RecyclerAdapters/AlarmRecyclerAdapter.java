package com.ninji.basiccalendar.RecyclerAdapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ninji.basiccalendar.AddAlarmActivity;
import com.ninji.basiccalendar.MastaweshaBrodcastReciver;
import com.ninji.basiccalendar.R;
import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.model.Alarm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> {

    private List<Alarm> mAlarmList ;
    private Context mContext ;
    private LayoutInflater mLayoutInflater ;
    private int mAlarmId;
    private int mAlramPostion;
    private AppDatabse mAppDatabse;

    public AlarmRecyclerAdapter( Context context,  List<Alarm> alarmList) {
        mAlarmList = alarmList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
//        mAppDatabse = AppDatabse.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.item_alarm , viewGroup , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return mAlarmList.size();
    }

    public void setData(List<Alarm> alarmList) {
        mAlarmList = alarmList ;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private Alarm mAlarm;
        private int mPosition;
        private final TextView mTitle;
        private final TextView mAlarmTime;
        private final Switch mToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.alaram_title);
            mAlarmTime = itemView.findViewById(R.id.alarmTime);
            mToggle = itemView.findViewById(R.id.switch1);

            mToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String alarmNote = "Alarm  " +  mAlarm.getTitle();
                    if ( isChecked ){
                        Toast.makeText(mContext ,( alarmNote + " is ON" ) ,Toast.LENGTH_LONG).show();
                        setAlarm( true);
                    } else {
                        // cancels the alarm
//                        Toast.makeText(mContext , ( alarmNote + " is OFF" ),Toast.LENGTH_LONG).show();
                        setAlarm(false);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AddAlarmActivity.newIntent( mContext , mAlarm.getID());
                    mContext.startActivity(intent);
                }
            });
        }

        public void bind(int i) {

            mPosition = i;
            mAlarm = mAlarmList.get(i);

            mAlarmId = mAlarm.getID();
            mAlramPostion = mPosition;
            mTitle.setText( mAlarm.getTitle() );
            mAlarmTime.setText(mAlarm.getAlarmTime());
            mToggle.setChecked( mAlarm.isSet() );

        }
    }

    private void setAlarm( boolean set) {

        Alarm alarm = mAlarmList.get(mAlramPostion) ;
        Date date = alarm.getAlarmDate();
        int hour = date.getHours();
        int minute = date.getMinutes();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( System.currentTimeMillis() );

        cal.set(Calendar.HOUR_OF_DAY , hour);
        cal.set(Calendar.MINUTE,minute );

        // the alarm Reciver class will send the notfication
        Intent intent = new Intent( mContext , MastaweshaBrodcastReciver.class);
        intent.putExtra("type" , "alarm");
        intent.putExtra("id" , alarm.getID());

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        if ( set ){
            // the request code should me modfied with the alaram_id
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext() , alarm.getID() ,intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP , cal.getTimeInMillis() , pendingIntent);
        } else {

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext() , alarm.getID() , intent , PendingIntent.FLAG_NO_CREATE);
            alarmManager.cancel(pendingIntent);
        }



    }




}
