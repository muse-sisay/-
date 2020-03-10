package com.ninji.basiccalendar.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.ninji.basiccalendar.model.Alarm;
import com.ninji.basiccalendar.model.Event;
import com.ninji.basiccalendar.model.Note;

@Database(entities = {Alarm.class , Event.class , Note.class} , version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabse extends RoomDatabase {

    private static AppDatabse instance ;

    public abstract DatabaseDao mDatabaseDao ();

    public static AppDatabse getInstance( Context context) {

        if ( instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,AppDatabse.class
            ,"Mastawesha").allowMainThreadQueries().build();
        }
        return  instance ;
    }

    public static void destroyInstance () {
        instance = null ;
    }
}

