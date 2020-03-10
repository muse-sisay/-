package com.ninji.basiccalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninji.basiccalendar.RecyclerAdapters.AlarmRecyclerAdapter;
import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.model.Alarm;

import java.util.List;

public class AlarmFragment extends Fragment {

    private AlarmRecyclerAdapter mAdapter;
    private List<Alarm> mAlarmList;
    private AppDatabse mDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm , container , false);

        mDb = AppDatabse.getInstance(getContext());
        mAlarmList = mDb.mDatabaseDao().getAlarms();
        RecyclerView alarmRecyclerView = view.findViewById(R.id.alarm_recycler);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new AlarmRecyclerAdapter(getContext() , mAlarmList);
        alarmRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = view.findViewById(R.id.alarmFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getContext() , AddAlarmActivity.class);
                startActivity(intent);
            }
        });
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAlarmList = mDb.mDatabaseDao().getAlarms();
        mAdapter.setData( mAlarmList );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDatabse.destroyInstance();
    }
}
