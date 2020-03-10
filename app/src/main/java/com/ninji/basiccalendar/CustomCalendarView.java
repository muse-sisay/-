package com.ninji.basiccalendar;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ninji.basiccalendar.model.DayAndDates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomCalendarView extends LinearLayout {

    private static final String THIS_TAG = "CUSTOM CALENDAR";

    private Context mContext;
    private Calendar mCalendar;
    private Button mPreviousBtn;
    private Button mNextBtn;
    private TextView mCurrentMonthEC;
    private TextView mCurrentMonthGC;

    private GridView mCalendarGridView;

    private CalendarGridAdapter mAdapter ;
    private List<Date> dayValueInCells = new ArrayList<Date>();
    private static boolean is_pagume;

    private int mCurrentEthiopianYear;
    private int mCurrentEthiopianMonth;
    private int mCurrentEthiopianDay;
    private Date mFirstDayOfTheMonth;
    private FragmentManager mFragmentManager;

    public CustomCalendarView(Context context) {
        super(context);
    }
    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CustomCalendarView(Context context , AttributeSet attrs ) {

        super(context, attrs);
        mContext = context ;
        mCalendar = Calendar.getInstance();

        int [] values = new EthiopicCalendar(mCalendar).gregorianToEthiopic();
        mCurrentEthiopianYear = values[0];
        mCurrentEthiopianMonth = values[1];
        mCurrentEthiopianDay = values[2];

        dayValueInCells = getListOfDates(mCalendar);
        bindViews();



        mAdapter = new CalendarGridAdapter(mContext, dayValueInCells , mFirstDayOfTheMonth );

        mCalendarGridView.setAdapter(mAdapter);
        mCalendarGridView.setHorizontalSpacing(4);
        mCalendarGridView.setVerticalSpacing(4);

        if ( mContext != null ){
            Log.d("CALENDAR", "Context manager is not null");
//            Toast.makeText(mContext , "FM is NOT NULL ", Toast.LENGTH_LONG);
        } else {
            Log.d("CALENDAR", "Context manager is NULL");
//            Toast.makeText(mContext , "FM is NULL ", Toast.LENGTH_LONG);
        }




    }

    private void bindViews() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_view , this);

        mPreviousBtn = view.findViewById(R.id.previous_month_button);
        mNextBtn = view.findViewById(R.id.next_month_button);

        mCurrentMonthEC = view.findViewById(R.id.month_display_EC);
        mCurrentMonthGC = view.findViewById(R.id.moth_display_GC);
        setLabel();

        mCalendarGridView = view.findViewById(R.id.calendar_grid);

        mNextBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               mCurrentEthiopianYear = ( mCurrentEthiopianMonth ==13 ) ? mCurrentEthiopianYear +1 : mCurrentEthiopianYear ;
               mCurrentEthiopianMonth = (mCurrentEthiopianMonth == 13 ) ? 1 : mCurrentEthiopianMonth+ 1 ;

                changeMonth();
            }
        });

        mPreviousBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentEthiopianYear = ( mCurrentEthiopianMonth > 1 ) ? mCurrentEthiopianYear : mCurrentEthiopianYear -1 ;
                mCurrentEthiopianMonth = (mCurrentEthiopianMonth > 1) ? mCurrentEthiopianMonth -1 : 13 ;
                changeMonth();
            }
        });

//        mCalendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(mContext, "Clicked " + position, Toast.LENGTH_LONG).show();
//            }
//        });


    }

    private void changeMonth() {
        List<Date> list = getListOfDates(mCalendar);
        mAdapter.setDates(list, mFirstDayOfTheMonth);
        mAdapter.notifyDataSetChanged();
        setLabel();
    }

    private void setLabel() {
        String currentMonthECD = mContext.getString( DayAndDates.Months.ethMonths[mCurrentEthiopianMonth - 1]) + " " + mCurrentEthiopianYear;

        Calendar cal = Calendar.getInstance();
        cal.setTime(mFirstDayOfTheMonth);
        int month = cal.get(Calendar.MONTH) ;

        String currentMonthGCD = DayAndDates.Months.gMonths[month] + " " + cal.get(Calendar.YEAR);
        month = (month == 11 ) ? 0 : month + 1 ;
        currentMonthGCD += " - " + DayAndDates.Months.gMonths[month] + " " + cal.get(Calendar.YEAR);

        mCurrentMonthEC.setText(currentMonthECD);
        mCurrentMonthGC.setText(currentMonthGCD);
//        mCurrentMonthGC.setTypeface(null ,Typeface.BOLD);

    }


    private List<Date> getListOfDates ( Calendar mCal ) {

        getFirstDayOfTheMonth();
        // at the end it should return a list of DATES
        List<Date> dayValueInCells = new ArrayList<Date>();


        // check whether we need 5 or 6 rows or for pagume 1 or 3 rows
        int day_of_week = mCal.get(Calendar.DAY_OF_WEEK) ;
        boolean isSunday = day_of_week== 1 ;

        int MAX_CALENDAR_CELLS =  isSunday ? 42 : 35 ;
        MAX_CALENDAR_CELLS = is_pagume ? 14 : MAX_CALENDAR_CELLS ;

        if( mCurrentEthiopianMonth ==13){
            MAX_CALENDAR_CELLS = 14 ;
        }

        int firstDayOfTheMonthCal =
                isSunday ?  6 : day_of_week - 2;

        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonthCal);
        while ( dayValueInCells.size() < MAX_CALENDAR_CELLS){
            dayValueInCells.add(mCal.getTime());
//            System.out.println(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH ,  1);
        }

        return dayValueInCells ;

    }

    private void getFirstDayOfTheMonth( ) {

        EthiopicCalendar ecal = new EthiopicCalendar( mCurrentEthiopianYear , mCurrentEthiopianMonth , 1 );
        int [] values = ecal.ethiopicToGregorian();

        mCalendar.set(values[0] , values[1] -1 ,values[2]);
        Log.d(THIS_TAG ,  "Current Month GC " + values[1] + " CURRENT MONTH EC " + mCurrentEthiopianMonth );
        mFirstDayOfTheMonth = mCalendar.getTime();

//          mCurrentDay = values[2];
//        mCurrentMonth = values[1];
//        mCurrentYear = values[0];
//
//        mCurrentMonthEC.setText(ethMonths[values[1] - 1]);
//
//        int firstDayOfTheMonth = mCurrentDay - 1 ;
//        ecal.set(mCurrentYear, mCurrentMonth, mCurrentDay - firstDayOfTheMonth);
//
//        values = ecal.ethiopicToGregorian();
//
//        cal.set(values[0] , values[1] -1, values[2]);
//
//        is_pagume = mCurrentMonth == 13;
//        return cal;
    }

    public void setFM(FragmentManager childFragmentManager) {


    }

    public void setFM(FragmentManager childFragmentManager, Context context) {
        mFragmentManager = childFragmentManager;
        Log.d("CALENDAR" , "recived fragment");

        if ( mFragmentManager != null ){
            Log.d("CALENDAR", "Fragment manager is not null");
//            Toast.makeText(mContext , "FM is NOT NULL ", Toast.LENGTH_LONG);
        } else {
            Log.d("CALENDAR", "Fragment manager is NULL");
//            Toast.makeText(mContext , "FM is NULL ", Toast.LENGTH_LONG);
        }
        mAdapter.setFM(mFragmentManager);
    }
}
