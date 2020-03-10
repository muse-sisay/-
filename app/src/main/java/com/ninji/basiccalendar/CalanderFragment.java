package com.ninji.basiccalendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

public class CalanderFragment extends Fragment {

    private CustomCalendarView mCalendarView;
    private FragmentManager mChildFragmentManager;
    Date currentDate ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar , container , false);
//        CalendarCustomView calendarView = view.findViewById(R.id.cal_view);

        currentDate = Calendar.getInstance().getTime();
        mCalendarView = (CustomCalendarView) view.findViewById(R.id.calendar_view);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.event_list_fragment_container);

        if ( fragment == null){
            Calendar calendar = Calendar.getInstance();

            fragment = EventListFragment.newInstance( calendar.getTime());
            fm.beginTransaction()
                    .add(R.id.event_list_fragment_container , fragment)
                    .commit();
        }

        return view ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if( mCalendarView !=null ){
//            Toast.makeText (getContext() ,"THE CalanderView is NOT NULL" , Toast.LENGTH_LONG).show();
            mChildFragmentManager = getFragmentManager();
            mCalendarView.setFM(mChildFragmentManager , getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mCalendarView.n
    }
}
