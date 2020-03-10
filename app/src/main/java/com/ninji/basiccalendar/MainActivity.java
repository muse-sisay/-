package com.ninji.basiccalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.database.DataManager;
import com.ninji.basiccalendar.model.Note;

import java.util.List;

public class MainActivity extends BaseActivity {

    private GridView mCalanderGridView;
    private ViewPager mViewPager;
    private AppDatabse db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
//        CustomCalendarView calendarView = findViewById(R.id.calendar_view);

        db = AppDatabse.getInstance(this);

        int itemCounts =  db.mDatabaseDao().countNotes();
        if ( itemCounts == 0){
            List<Note> notes = DataManager.getInstance().getNoteList();
            db.mDatabaseDao().insertNote(notes);
            Log.d("On Create", "Data Inserted");
        } else {
            Log.d("On Create", "Data Already Exists  ");
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager() , getBaseContext()) ;
        mViewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;

            @Override
            public void transformPage(@NonNull View view, float position) {
                view.setTranslationX(view.getWidth()*position);

                if (  position <=- 1.0f  || position >=1.0f )  { // [-Infinity,-1)
                    view.setAlpha(0.0f);
                    view.setTranslationX(1);
                    view.setVisibility(View.GONE);

                } else if (position == 0.0f) { // [-1,1]
                    view.setAlpha(1.0f);
                    view.setVisibility(View.VISIBLE);

                } else { // (1,+Infinity]
                    view.setAlpha(1.0f - Math.abs(position));
                    view.setVisibility(View.VISIBLE);
                    view.setTranslationX(1);
                }
            }
        });

        DataManager dm = DataManager.getInstance();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
//        final SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Log.d("Calendar", "QueryTextSubmit: " + s);
//                getQuery(s);
//                return true;
//            }
//
//            private void getQuery(String s) {
//                String searchText = "%"+s+"%";
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Log.d("Calendar", "QueryTextChange: " + s);
//                return false;
//            }
//        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_convert :
                FragmentManager fm = getSupportFragmentManager();
                DateConverterDialogFragment dialog = new DateConverterDialogFragment();
                dialog.show(fm, "converter");

                return true;
            case R.id.menu_item_setting:
                Intent settingIntent = new Intent(this , PrefsActivity.class);
                startActivity(settingIntent);
                return true ;
            case R.id.item_to_today:
                mViewPager.setCurrentItem(1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        AppDatabse.destroyInstance();
        super.onDestroy();
    }
}
