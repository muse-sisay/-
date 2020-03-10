package com.ninji.basiccalendar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class PrefsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
//        Drawable unwrapped = getDrawable(R.drawable.ic_star_black_24dp);
//        Drawable wrapped = DrawableCompat.wrap(unwrapped);
//        DrawableCompat.setTint(wrapped , Color.WHITE);
        actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_clear_black_24dp));
        setContentView(R.layout.activity_prefs);


        getFragmentManager()
                .beginTransaction()
                .add(R.id.prefs_content, new SettingFragment())
                .commit();

    }

    public static class SettingFragment extends PreferenceFragment {
        @Override
        public void onCreate( Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            findPreference("theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    getActivity().recreate();

                    return true;
                }
            });
        }
    }
}
