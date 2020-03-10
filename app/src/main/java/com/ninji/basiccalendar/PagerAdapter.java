package com.ninji.basiccalendar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

public class PagerAdapter extends FragmentPagerAdapter {

    public final int  tabTitles [] = new int[] {R.string.memo , R.string.calendar, R.string.agenda} ;
    Context mContext ;
    public PagerAdapter(FragmentManager fm , Context context) {

        super(fm);
        mContext = context ;
    }

    @Override
    public Fragment getItem(int i) {

        switch ( i  ){
            case 0:
                return new NotesFragment();
            case 1 :
                return new CalanderFragment();
            case 2 :
                return new AlarmFragment();
        }
        return null ;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return mContext.getString(tabTitles[position]) ;
    }
}
