package com.ninji.basiccalendar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ninji.basiccalendar.R;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    protected SharedPreferences mSettings;
    private String mCurrentTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mCurrentTheme = mSettings.getString("theme" , "Theme1" );
        setAppTheme(mCurrentTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String theme = mSettings.getString("theme" , "Theme1" );
        String lan = mSettings.getString("language" ,"lan1");
        if( ! theme.equals(mCurrentTheme))
            recreate();



    }

    private void setAppTheme( String appTheme) {
        if ( appTheme.equals("Theme1")) {
            setTheme(R.style.AppTheme);
        } else if ( appTheme.equals("Theme2")){
            setTheme(R.style.Lime);
        } else if (appTheme.equals("Theme3"))  {
            setTheme(R.style.brown);
        } else if ( appTheme.equals("Theme4")) {
            setTheme(R.style.orange);
        }  else if ( appTheme.endsWith("Theme5")) {
            setTheme(R.style.green);
        }   else if ( appTheme.equals("Theme6")) {
            setTheme(R.style.lightgreen);
        }
    }

    private void setLocal(String s)
    {
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString("My_lang",s);
        editor.apply();
    }

    public void loadlocal()
    {
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_lang","");
        setLocal(language);
    }

}
