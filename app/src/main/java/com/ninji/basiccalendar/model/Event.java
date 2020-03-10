package com.ninji.basiccalendar.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

@Entity(tableName = "event")
public class Event {
    @PrimaryKey
    @NonNull
    private String ID;
    @ColumnInfo
    private String mDate;
    @ColumnInfo
    private String eventTitle;
    @ColumnInfo
    private String eventDescription;
    @ColumnInfo
    private boolean sendNotif ;

    @Ignore
    public Event() {
      this( UUID.randomUUID()) ;
    }
    @Ignore
    public Event(UUID ID) {
        this.ID = ID.toString();
    }
    @Ignore
    public Event (String  id , String date, String eventTitle, String eventDescription) {
        this.ID = id ;
        this.mDate = date;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        sendNotif = false ;
    }

    public Event(@NonNull String ID, String date, String eventTitle, String eventDescription, boolean sendNotif) {
        this.ID = ID;
        mDate = date;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.sendNotif = sendNotif;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public boolean isSendNotif() {
        return sendNotif;
    }

    public void setSendNotif(boolean sendNotif) {
        this.sendNotif = sendNotif;
    }
}