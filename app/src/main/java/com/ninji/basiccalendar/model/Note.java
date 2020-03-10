package com.ninji.basiccalendar.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;
@Entity(tableName = "note")
public class Note {

    @PrimaryKey
    @NonNull
    private String ID ;
    @ColumnInfo
    private String title ;
    @ColumnInfo
    private String note;
    @ColumnInfo
    private boolean imagesSet;
    @ColumnInfo
    private boolean audioSet;
    @ColumnInfo
    private String imagePath;


    @Ignore
    public Note() {
      this (UUID.randomUUID().toString() );
    }
    @Ignore
    public Note ( String uuid ){
        ID = uuid ;
    }
    @Ignore
    public Note(@NonNull String ID, String title, String note, boolean imagesSet, boolean audioSet) {
        this.ID = ID;
        this.title = title;
        this.note = note;
        this.imagesSet = imagesSet;
        this.audioSet = audioSet;
        this.imagePath = null;
    }

    public Note(@NonNull String ID, String title, String note, boolean imagesSet, boolean audioSet, String imagePath) {
        this.ID = ID;
        this.title = title;
        this.note = note;
        this.imagesSet = imagesSet;
        this.audioSet = audioSet;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isImagesSet() {
        return imagesSet;
    }

    public void setImagesSet(boolean imagesSet) {
        this.imagesSet = imagesSet;
    }

    public boolean isAudioSet() {
        return audioSet;
    }

    public void setAudioSet(boolean audioSet) {
        this.audioSet = audioSet;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
