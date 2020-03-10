package com.ninji.basiccalendar.database;

import android.util.Log;

import com.ninji.basiccalendar.model.Alarm;
import com.ninji.basiccalendar.model.Event;
import com.ninji.basiccalendar.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DataManager {

    List<Note> mNoteList ;
    List<Event> mEventList ;
    List<Alarm> mAlarmList;
    private static DataManager DM ;

    public static DataManager getInstance (){
        if ( DM == null){
            DM =  new DataManager();
        }
        return DM ;
    }

    public DataManager() {
        intializeNotes();
        intializeEvents();
        intializeAlarms();

    }

    private void intializeAlarms() {

        mAlarmList = new ArrayList<>();
        int id = mAlarmList.size();

        Alarm alarm ;
        alarm = new Alarm( id ,"Finish Android Project" , new Date() , false, "2:30" );
        mAlarmList.add(alarm);
        id++ ;

        alarm = new Alarm( id ,"Go To Library" , new Date () ,true, "10:30" );
        mAlarmList.add(alarm);

    }

    private void intializeEvents() {
        SimpleDateFormat dateFormater = new SimpleDateFormat ("ddMMyyyy");

        mEventList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        Date date ;

        for ( int i = 0 ; i < 10 ; i ++){
            cal.set(Calendar.DAY_OF_MONTH , cal.get(Calendar.DAY_OF_MONTH) + 2 );
            date = cal.getTime();

            String dataString = dateFormater.format(date);
            Event e = new Event();
            e.setDate( dataString );
            e.setEventTitle("Event " + (i+1));
            e.setEventDescription("This is a description");
            mEventList.add(e);

            Log.d("EVENT" , e.getEventTitle() +e.getID());

            if (  i%2 ==0 ){
                // make double events
                Event ee = new Event();
                ee.setDate( dataString );
                ee.setEventTitle("Event " + (i+1) + "++");
                ee.setEventDescription("This is a description");
                mEventList.add(ee);

                Log.d("EVENT" , ee.getEventTitle() +  ee.getID());
            }
        }

    }

    private void intializeNotes() {
        mNoteList = new ArrayList<>();
        for ( int i = 0 ; i < 6 ; i ++){

            UUID id= UUID.randomUUID();
            String strID = id.toString();
            Note note = new Note(strID , "Note " + i ,"This is a simple Note" ,
                    false , false );
            mNoteList.add(note);
        }
    }

    public List<Note> getNoteList() {
        return mNoteList;
    }

    public Note getNote ( String UUID){

        for ( Note note : mNoteList){
            if ( note.getID().equals(UUID)){
                return note;
            }
        }
        return null ;
    }

    public List<Alarm> getAlarmList() {
        return mAlarmList;
    }

    public List<Event> getEvents (String date ){
        List<Event> events = new ArrayList<>();
        for ( Event e : mEventList ) {
            if ( e.getDate().equals(date)){
                events.add(e);
//                Log.d("EVENT" , "Match " + e.getEventTitle() + e.getID());
            }
        }
        return events ;
    }



    public Event getEv (String ID){
        Event event = null;
        for ( Event e : mEventList ) {
            if ( e.getID().equals(ID)){
                event = e ;
            }
        }
        return event ;
    }


    public void addEvent(Event event) {
        mEventList.add(event);
    }
    public void addNote ( Note note) {
        mNoteList.add(note);
    }


    public void addAlarm ( Alarm alarm ){
       int size =  mAlarmList.size() ;
       size++ ;
       alarm.setID(size);
       mAlarmList.add(alarm);
    }

    public Alarm getAlarm ( int id ){
        Alarm alarm = null ;
        for (Alarm a : mAlarmList) {
            if ( a.getID() ==  id){
                alarm = a ;
            }
        }
        return alarm ;
    }
}
