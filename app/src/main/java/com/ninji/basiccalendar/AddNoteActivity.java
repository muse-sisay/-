package com.ninji.basiccalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.database.DataManager;
import com.ninji.basiccalendar.model.Note;

import java.io.File;
import java.io.IOException;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class AddNoteActivity extends BaseActivity {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 12;
    private static final int REQUEST_CODE_ASK_PERMISSIONS2 = 13;

    private static final String KEY_NOTE_ID = "NOTE_KEY";
    private static final int KEY_CAMERA = 23;
    private static final int CAMERA_PERMISSION = 44;
    private Note mNote;
    private boolean mNewNote;
    private TextView mNoteTitle;
    private TextView mNoteMessage;
    private boolean mIsDeleting;
    boolean isRecording = false ;
    private boolean isPlaying = false;
    private AudioPlayer mAudioPlayer;
    private AppDatabse db ;
    private String mPathtoFile;
    private ImageView mImageView;

    public static Intent newIntent (Context context , String noteUUID){
        Intent intent = new Intent(context , AddNoteActivity.class);
        intent.putExtra(KEY_NOTE_ID , noteUUID);
        return intent ;

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabse.getInstance(this);
        getSupportActionBar().setElevation(0);
        checkPermssion();
//
//        ActionBar actionBar = getSupportActionBar();
////        Drawable unwrapped = getDrawable(R.drawable.ic_star_black_24dp);
////        Drawable wrapped = DrawableCompat.wrap(unwrapped);
////        DrawableCompat.setTint(wrapped , Color.WHITE);
//        actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_clear_black_24dp));
        setContentView(R.layout.activity_add_event1);

        getStateValues ();

        setUpAudioRecorder();

        mNoteTitle = findViewById(R.id.note_title);
        mNoteMessage = findViewById(R.id.note_message);


        if ( !mNewNote ){
            mNoteTitle.setText(mNote.getTitle());
            mNoteMessage.setText(mNote.getNote());
        }
        setUpImage();
    }

    private void setUpImage() {
        mImageView = findViewById(R.id.imageView2);
        if ( mNote.isImagesSet()){
            setImage();
        }
    }

    private void setUpAudioRecorder() {
        final Button btnRecord = findViewById(R.id.btnRecord);
        final Button btnPlay = findViewById(R.id.btnPlay);
        final Button btnDelete = findViewById(R.id.btnDelete);

        if ( !mNote.isAudioSet()) {
            btnPlay.setEnabled(false);
            btnDelete.setEnabled(false);
        } else {
            btnRecord.setEnabled(false);
        }


        mAudioPlayer = new AudioPlayer(this , mNote.getID());

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNote.setAudioSet(true);
                if ( !isRecording ){
                    isRecording = true ;
                    mAudioPlayer.record();
                    btnRecord.setText("Stop Recording");
                } else {
                    isRecording = false ;
                    mAudioPlayer.stopRecording();
                    btnRecord.setText("Press the Button to Record Audio");
                    btnPlay.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnRecord.setEnabled(false);
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !isPlaying ) {
                    isPlaying = true ;
                    mAudioPlayer.play();
                    btnPlay.setText("PAUSE");
                } else {
                    isPlaying = false ;
                    mAudioPlayer.pause();
                    btnPlay.setText("PLAY");
                }

            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNote.setAudioSet(false);
                mAudioPlayer.delete();
                btnPlay.setEnabled(false);
                btnDelete.setEnabled(false);
                btnRecord.setEnabled(true);
            }
        });


    }

    private void getStateValues() {
        Intent intent =  getIntent();
        String noteUUID = intent.getStringExtra(KEY_NOTE_ID);
        mNewNote = TextUtils.isEmpty(noteUUID);

        if (!mNewNote) {
            mNote = db.mDatabaseDao().getNoteByID(noteUUID);
        } else {
            mNote = new Note();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if ( id == R.id.action_undo) {
            mIsDeleting = true;
            finish();
        } else if ( id == R.id.action_delete) {
            if ( !mNewNote){
                db.mDatabaseDao().deleteNote(mNote);
                mAudioPlayer.delete();
                deletePhoto();
            }else {
                mIsDeleting = true;
            }
            finish();
        } else if (id == R.id.item_add_photo){
            dispatchPhtoACtion();
        } else if ( id == R.id.action_remove_image){
            if( mNote.isImagesSet() ){
                deletePhoto();
                mNote.setImagesSet(false);
                mImageView.setImageBitmap(null);
                mImageView.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Set up an image first", Toast.LENGTH_SHORT).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( ! mIsDeleting ){
            saveNote();

        }


    }

    private void saveNote() {
        mNote.setTitle(mNoteTitle.getText().toString());
        mNote.setNote(mNoteMessage.getText().toString());

        if ( mNewNote) {
//            DataManager.getInstance().addNote(mNote);
            db.mDatabaseDao().insertNote(mNote);

        }else {
            db.mDatabaseDao().updateNote(mNote);
//            db.mDatabaseDao().updateNote(mNote .getID()
//            ,mNote.getTitle()
//            ,mNote.getNote()
//            ,mNote.isImagesSet()
//            ,mNote.isAudioSet());
        }
    }





    public void checkPermssion (){
        if (Build.VERSION.SDK_INT >= 23){
                requestPermissions(new String [] {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ,  Manifest.permission.RECORD_AUDIO } , REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        AppDatabse.destroyInstance();
        super.onDestroy();
    }


    private void dispatchPhtoACtion() {

        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE} , CAMERA_PERMISSION);
        }

        Intent takePic  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ( takePic.resolveActivity(getPackageManager()) != null ){
            File photoFile = null ;
            photoFile = createPhoto ();

            if ( photoFile != null ){
                mNote.setImagePath(photoFile.getAbsolutePath());
                Uri photoURI = FileProvider.getUriForFile( this
                        , "com.ninji.basiccalendar"
                        ,photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT , photoURI);
                startActivityForResult(takePic , KEY_CAMERA);

            }

        }
    }

    private File createPhoto() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null ;

        try {
            image = File.createTempFile(
                    mNote.getID()
                    ,   ".jpg"
                    , storageDir) ;
        } catch (IOException e) {
            Log.d("<" , "Creating temp file " + e.toString());
        }
        return  image ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (  requestCode == RESULT_OK){
            if ( requestCode == KEY_CAMERA ){
                setImage();
                mNote.setImagesSet(true);
                // mNote.setImagePath(mPathtoFile);
            }
//        }

    }

    private void setImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(mNote.getImagePath());
        mImageView.setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(bitmap);
    }

    private void  deletePhoto () {
        if ( mNote.isImagesSet() ){
            File file = new File(mNote.getImagePath());
            file.delete();
        }

    }

}
