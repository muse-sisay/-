package com.ninji.basiccalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.database.DataManager;
import com.ninji.basiccalendar.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarGridAdapter extends ArrayAdapter {

    private static final String CAL_TAG = "THisISWHere";
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Date firstDayofTheMonthDate; // first Day of the month

    public static String[] gMonths = {
            "Jan","Feb","MAr","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec","ጳጉሜ"
    };
    private final Context mContext;
    private FragmentManager mFragmentManager;

    public CalendarGridAdapter(Context context, List<Date> monthlyDates, Date firstDayofTheMonthDate ) {
        super(context, R.layout.cell_layout);
        mContext = context;
        this.monthlyDates = monthlyDates;
        this.firstDayofTheMonthDate = firstDayofTheMonthDate;

        mInflater = LayoutInflater.from(context);

    }

    public void setDates ( List<Date>  list , Date cal ){
        monthlyDates = list ;
        firstDayofTheMonthDate = cal;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getPosition(Object item) {
//        return monthlyDates.indexOf(item);
        return 0 ;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Date mTodayDate = monthlyDates.get(position);

        Calendar calendarSelected = Calendar.getInstance();
        calendarSelected.setTime(mTodayDate);

        int eCurrentYear = calendarSelected.get(Calendar.YEAR);
        int eCurrentMonth = calendarSelected.get(Calendar.MONTH);
        int eCurrentDate = calendarSelected.get(Calendar.DAY_OF_MONTH);

//        final String ID= ""+ eCurrentDate + eCurrentMonth + eCurrentYear;

        EthiopicCalendar eCal = new EthiopicCalendar(calendarSelected);

        int [] values = eCal.gregorianToEthiopic();
        final int dayValueEth = values[2];
        int displayMonthEth = values[1];
        int displayYearEth = values[0];
//        Log.d(CAL_TAG ,"month of calendar " + displayMonthEth );

//        dateCal.setTime(firstDayofTheMonthDate);


        calendarSelected.setTime(firstDayofTheMonthDate);
        values = new EthiopicCalendar(calendarSelected.get(Calendar.YEAR), calendarSelected.get(Calendar.MONTH) +1 , calendarSelected.get(Calendar.DAY_OF_MONTH)).gregorianToEthiopic();
        int currentMonth = values[1] ;
        int currentYear =values[0];
//        Log.d(CAL_TAG ,"CURRENT MONTH " + currentMonth );


        View view = convertView;
        if(view == null){
            view = mInflater.inflate(R.layout.cell_layout2, parent, false);
        }

        calendarSelected.setTime(mTodayDate);

        ImageView todayIndicator = (ImageView) view.findViewById(R.id.today_bookmark);
        todayIndicator.setImageDrawable(null);

        ImageView eventIndicator = (ImageView) view.findViewById(R.id.event_indicator);
        eventIndicator.setImageDrawable(null);

        TextView cellDateEth = (TextView)view.findViewById(R.id.calendar_date_id);
        cellDateEth.setText(String.valueOf(dayValueEth));


        TextView cellDateGreg = view.findViewById(R.id.calendar_date_id_greg);
        cellDateGreg.setText( gMonths[calendarSelected.get(Calendar.MONTH)] + " " + calendarSelected.get(Calendar.DAY_OF_MONTH));

        TypedValue typedValue = new TypedValue();

        if ( Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == calendarSelected.get(Calendar.DAY_OF_MONTH) &&
             Calendar.getInstance().get(Calendar.YEAR) == calendarSelected.get(Calendar.YEAR) &&
             Calendar.getInstance().get(Calendar.MONTH) == calendarSelected.get(Calendar.MONTH) )
        {
            // this is the current day
            mContext.getTheme().resolveAttribute(R.attr.cellTextColorToday , typedValue , true);
            int color = typedValue.data;

            Drawable unwrapped = mContext.getDrawable(R.drawable.ic_bookmark_black_24dp);
            Drawable wrapped = DrawableCompat.wrap(unwrapped);
            DrawableCompat.setTint(wrapped , color );

            todayIndicator.setImageDrawable(wrapped);
            cellDateEth.setTextColor(color);

            cellDateGreg.setTextColor(color);
//            showEventList(ID , mTodayDate);

       } else if  (displayMonthEth == currentMonth && displayYearEth == currentYear){
          mContext.getTheme().resolveAttribute(R.attr.cellTextColor, typedValue , true);
          int color = typedValue.data;
          cellDateEth.setTextColor(color);
          cellDateGreg.setTextColor(color);
        }else{
            mContext.getTheme().resolveAttribute(R.attr.cellTextColorOff, typedValue , true);
            int color = typedValue.data;
            cellDateEth.setTextColor(color);
            cellDateGreg.setTextColor(color);
        }

        SimpleDateFormat dateFormater = new SimpleDateFormat ("ddMMyyyy");
        String dateString = dateFormater.format(mTodayDate);

//        List<Event> event = DataManager.getInstance().getEvents(dateString);
        List<Event> event = AppDatabse.getInstance(getContext()).mDatabaseDao().getEvents(dateString);
        if ( !  event.isEmpty()){
            mContext.getTheme().resolveAttribute(R.attr.cellTextColorToday , typedValue , true);
            int color = typedValue.data;

            Drawable unwrapped = mContext.getDrawable(R.drawable.ic_star_black_24dp);
            Drawable wrapped = DrawableCompat.wrap(unwrapped);
            DrawableCompat.setTint(wrapped , color );
            eventIndicator.setImageDrawable(wrapped);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEventList(mTodayDate );
            }
        });

        //Add day to calendar

        //Add events to the calendar
//        TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);


        return view;
    }

    private void showEventList( Date date ) {
        EventListFragment eventListFragment = EventListFragment.newInstance(date);
        mFragmentManager.beginTransaction()
                .replace(R.id.event_list_fragment_container, eventListFragment)
                .commit();

    }


    public void setFM(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager ;
    }

}
