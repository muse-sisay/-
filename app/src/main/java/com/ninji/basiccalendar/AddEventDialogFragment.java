package com.ninji.basiccalendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.model.Event;

import java.text.ParseException;
import java.util.Calendar;

public class AddEventDialogFragment extends DialogFragment {

    private static final String KEY_ID = "key_id";
    private static final String KEY_DATE = "key_date";
    private static final String KEY_TITLE = "key_title";
    private View mView;
    private EditText mTitleEdit;
    private EditText mDescEdit;
    private String mID;
    private Event mEvent;
    private String mDate;
    private boolean mNewEvent;
    private Button mBtnOk;
    private Button mBtnCancel;
    private String mTitle;
    private Switch mNotify;
    private AppDatabse mDb;
    private ImageButton mBtnDelete;


    public static AddEventDialogFragment newInstance( String id , String date ) {
        
        Bundle args = new Bundle();
        args.putString(KEY_ID, id);
        args.putString(KEY_DATE , date);
//        args.putString(KEY_TITLE , title);
        AddEventDialogFragment fragment = new AddEventDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    
    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_add_event, null);

        mDb = AppDatabse.getInstance(getContext());

        readStatevalues();

        bindViews();

        if ( !mNewEvent)
            displayNotes();

        clickListeneres();

        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(mView)
                .setTitle(null)
//                .setPositiveButton(R.string.ok_button , null)
//                .setNegativeButton(R.string.cancel_button , null )
                .create();
//        builder.setCancelable(false);
        builder.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
        return builder;
    }

    private void clickListeneres() {

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mNewEvent ){
                    addEvent();
                } else {
                    mEvent.setEventTitle(mTitleEdit.getText().toString());
                    mEvent.setEventDescription(mDescEdit.getText().toString());
                    mEvent.setSendNotif(mNotify.isChecked());
                    mDb.mDatabaseDao().updateEvent(mEvent);
                    setANotfication();
                }
                dismiss();

            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.mDatabaseDao().deleteEvent(mEvent);
                dismiss();
            }
        });
    }

    private void setANotfication() {

        final AlarmManager alarmManager = (AlarmManager) getContext().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent( getContext().getApplicationContext() , MastaweshaBrodcastReciver.class);
        // put date as extra
        intent.putExtra( MastaweshaBrodcastReciver.KEY_DATE, mEvent.getID());
        intent.putExtra("type" , "event");
        PendingIntent pendingIntent = PendingIntent.getBroadcast( getContext().getApplicationContext()  , Integer.parseInt(mEvent.getDate()) ,intent , PendingIntent.FLAG_UPDATE_CURRENT);

        if ( mNotify.isChecked()){

            java.util.Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(Helper.getDateFromString(mEvent.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long alarmTime = cal.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP ,  alarmTime ,pendingIntent);

        } else {
            alarmManager.cancel(pendingIntent);

        }
    }


//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//
//
//        mView = LayoutInflater.from(getActivity())
//                .inflate(R.layout.dialog_add_event, null);
//
//        readStatevalues();
//
//        bindViews();
//
//        if ( !mNewEvent)
//            displayNotes();
//
//        mBtnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        mBtnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ( mNewEvent ){
//                    addEvent();
//                } else {
//                    mEvent.setEventTitle(mTitleEdit.getText().toString());
//                    mEvent.setEventDescription(mDescEdit.getText().toString());
//                }
//                dismiss();
//
//            }
//        });
//
//        return mView;
//    }

    private void displayNotes() {
//        mEvent = DataManager.getInstance().getEv(mID);
        mEvent =  mDb.mDatabaseDao().getEventWithID(mID);
        if ( mEvent != null){
            mTitleEdit.setText( mEvent.getEventTitle());
            mDescEdit.setText( mEvent.getEventDescription());
            mNotify.setChecked( mEvent.isSendNotif() );
        }
    }

    private void readStatevalues() {
        Bundle bundle = getArguments();
        mID = bundle.getString(KEY_ID);
        mDate = bundle.getString(KEY_DATE);
//        mTitle = bundle.getString(KEY_TITLE);
        mNewEvent = mID == null;

    }

    private void bindViews() {
        mTitleEdit = mView.findViewById(R.id.edit_event_title);
        mDescEdit = mView.findViewById(R.id.edit_event_desc);
        mNotify = mView.findViewById(R.id.event_send_notif);

        TextView eventTitle = mView.findViewById(R.id.event_title);

        try {
            eventTitle.setText(Helper.getEthiopicDateFromString( getContext() , mDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mBtnCancel = mView.findViewById(R.id.button_cancel);
        mBtnOk = mView.findViewById(R.id.button_ok);
        mBtnDelete = mView.findViewById(R.id.button_delete);


    }

    private void addEvent() {
        String title = mTitleEdit.getText().toString();
        String description = mDescEdit.getText().toString();
        Boolean sendNotification = mNotify.isChecked();

        Event event = new Event();

        event.setDate(mDate);
        event.setEventTitle(title);
        event.setEventDescription(description);
        event.setSendNotif(sendNotification);

//        DataManager.getInstance().addEvent(event);

        mDb.mDatabaseDao().insertEvent(event);


        // add this to data manager

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppDatabse.destroyInstance();
    }
}
