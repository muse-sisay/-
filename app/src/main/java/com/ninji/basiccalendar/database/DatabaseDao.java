package com.ninji.basiccalendar.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ninji.basiccalendar.model.Alarm;
import com.ninji.basiccalendar.model.Event;
import com.ninji.basiccalendar.model.Note;

import java.util.List;

@Dao
public interface DatabaseDao {

    // notes table
    @Insert
    void insertNote (List<Note> notes);

    @Insert
    void insertNote (Note... notes);

    @Query("SELECT COUNT(*) FROM note" )
    int countNotes();


    @Query("SELECT * FROM note" )
    List<Note> getNotes ();

    @Query("SELECT * FROM note WHERE ID = :ID" )
    Note getNoteByID ( String ID);

    @Delete
    void deleteNote ( Note note);

    @Update
    int updateNote ( Note note);

    @Query("SELECT * FROM note WHERE title LIKE :txt OR note LIKE :txt")
    List<Note> searchNote( String txt );


    // events table

    @Insert
    void insertEvent (List<Event> events);

    @Insert
    void insertEvent (Event... events);

    @Query("SELECT * FROM event" )
    List<Event> getEvents ();

    @Query("SELECT * FROM event WHERE mDate = :date" )
    List<Event> getEvents ( String date);

    @Query("SELECT * FROM event WHERE ID = :id" )
    Event getEventWithID ( String id);

    @Update
    int updateEvent ( Event event);

    @Delete
    int deleteEvent ( Event event);


    // Alarm table

    @Insert
    void insertAlarms(List<Alarm> alarms);

    @Insert
    void insertAlarms ( Alarm... alarms);

    @Query("SELECT * FROM alarm")
    List<Alarm> getAlarms ();

    @Query("SELECT * FROM alarm WHERE ID = :id" )
    Alarm getAlarmWithID ( int id);

    @Update
    void updateAlarm (Alarm alarm);

    @Delete
    int deleteAlarm ( Alarm alarm);


}
