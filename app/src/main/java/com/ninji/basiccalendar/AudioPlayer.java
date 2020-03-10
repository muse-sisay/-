package com.ninji.basiccalendar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class AudioPlayer {

    Context mContext ;
    String filenName  ;
    String outputDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/noteAppNin";
    private boolean mIsRecording;
    private MediaRecorder mMyAudioRecorder;
    private MediaPlayer mMediaPlayer;

    public AudioPlayer( Context context , String ID ) {
        mContext = context ;
        filenName = outputDirectory + "/" + ID + ".3gp";
    }

    public void record () {
        mMyAudioRecorder = new MediaRecorder();
        mMyAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMyAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMyAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        mMyAudioRecorder.setOutputFile(filenName);

        try {
            mMyAudioRecorder.prepare();
            mMyAudioRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording (){
        mMyAudioRecorder.stop();
        mMyAudioRecorder.release();
        mMyAudioRecorder = null;
    }

    public void play (){
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(filenName);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause (){
            mMediaPlayer.pause();
    }

    public void delete () {
        File file = new File(filenName);
        file.delete();
    }




}
