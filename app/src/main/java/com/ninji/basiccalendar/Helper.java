package com.ninji.basiccalendar;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.ninji.basiccalendar.model.DayAndDates;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper {

    public static String simpleDateFormat ( Date date ){

        SimpleDateFormat dateFormater = new SimpleDateFormat("ddMMyyyy");
        String dateString = dateFormater.format(date);

        return dateString ;
    }

    public static String converEthiopicDateString (Context context , Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        EthiopicCalendar ethiopicCalendar = new EthiopicCalendar(cal);
        int [] values = ethiopicCalendar.gregorianToEthiopic() ;

        int dayOfWeek =  cal.get(Calendar.DAY_OF_WEEK) == 1 ?  7:  cal.get(Calendar.DAY_OF_WEEK) - 1;

        String dayEth = context.getString( DayAndDates.DaysOfWeek.ethDays[dayOfWeek-1] ) + " ";
        String monthEth = context.getString(DayAndDates.Months.ethMonths[values[1] - 1 ] )+ " " ;
        String mDay = dayEth + monthEth + values[2]+ " " + values[0];
//           Toast.makeText(getContext(), cal.getTime().toString(), Toast.LENGTH_SHORT).show();
        return mDay;
    }

    public static String [] convertEthiopicDateStringArray (Context context ,Calendar cal) {

        EthiopicCalendar ethiopicCalendar = new EthiopicCalendar(cal);
        int [] values = ethiopicCalendar.gregorianToEthiopic() ;

        int dayOfWeek =  cal.get(Calendar.DAY_OF_WEEK) == 1 ?  7:  cal.get(Calendar.DAY_OF_WEEK) - 1;

        String dayEth = context.getString( DayAndDates.DaysOfWeek.ethDays[dayOfWeek-1] ) + " ";
        String monthEth = context.getString(DayAndDates.Months.ethMonths[values[1] - 1 ] )+ " " ;

        String [] mDay = new String[2];
        mDay[0]= dayEth ;
        mDay[1]= monthEth + values[2];
        return mDay;
    }

    public static String getEthiopicDateFromString  (Context context , String  dateString)
    throws ParseException{

        DateFormat dateFormater = new SimpleDateFormat ("ddMMyyyy");
        Date date = dateFormater.parse(dateString);

        return converEthiopicDateString( context, date);
    }

    public static Date getDateFromString ( String dateString)
            throws ParseException{

        DateFormat dateFormater = new SimpleDateFormat ("ddMMyyyy");
        Date date = dateFormater.parse(dateString);

        return date ;
    }

}
