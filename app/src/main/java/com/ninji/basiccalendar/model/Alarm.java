package com.ninji.basiccalendar.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.ninji.basiccalendar.database.DateConverter;

import java.util.Date;

@Entity(tableName = "alarm")
public class Alarm {

    @PrimaryKey(autoGenerate = true    )
    private int ID;
    @ColumnInfo
    private String Title ;
    @ColumnInfo
    private boolean isSet ;
    @ColumnInfo
    @TypeConverters({DateConverter.class})
    private Date  alarmDate ;
    @ColumnInfo
    private String alarmTime ;

    public Alarm() {
    }

    public Alarm(int ID, String title, Date date , boolean isSet, String alarmTime) {
        this.ID = ID;
        Title = title;
        this.isSet = isSet;
        this.alarmTime = alarmTime;
        this.alarmDate = date;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

}
