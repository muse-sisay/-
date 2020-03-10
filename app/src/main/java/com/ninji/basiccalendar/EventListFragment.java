package com.ninji.basiccalendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninji.basiccalendar.RecyclerAdapters.EventRecyclerAdapter;
import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.database.DataManager;
import com.ninji.basiccalendar.model.Event;

import java.util.Date;
import java.util.List;

public class EventListFragment  extends Fragment {

    private static final String KEY_DATE = "key_date";


    private TextView mCurrentDate;
    private EventRecyclerAdapter mAdapter;
    private String mDateString;
    private AppDatabse mDb;
    private List<Event> mEvent;

    public static EventListFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATE , date);
//        args.putString(KEY_DATE , date);
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // depending whether there is an event 2 layouts
        // get The bundle check for events
        Bundle b = getArguments();

        View view ;

        if ( b == null){
            view = inflater.inflate(R.layout.layout_no_event_start, container, false);
        } else {

            final Date date = (Date) b.getSerializable(KEY_DATE);
            mDateString = Helper.simpleDateFormat(date);

//            mEvent = DataManager.getInstance().getEvents(mDateString);
            mDb = AppDatabse.getInstance(getContext());
            mEvent = mDb.mDatabaseDao().getEvents(mDateString);

            if (mEvent.isEmpty()) {

                view = inflater.inflate(R.layout.layout_no_event, container, false);
                mCurrentDate = view.findViewById(R.id.current_date);
                mCurrentDate.setText(Helper.converEthiopicDateString(getContext(), date));

            } else {

                view = inflater.inflate(R.layout.fragment_event, container, false);

                mCurrentDate = view.findViewById(R.id.current_date);
                mCurrentDate.setText(Helper.converEthiopicDateString(getContext(), date));

                mAdapter = new EventRecyclerAdapter(getActivity(), mEvent);
                RecyclerView rv = view.findViewById(R.id.event_recycler);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(mAdapter);

//            TypedValue typedValue = new TypedValue();
//            getContext().getTheme().resolveAttribute(R.attr.cellTextColorToday , typedValue , true);
//            int color = typedValue.data;
//
//            ImageView imgCal = (ImageView) view.findViewById(R.id.image_no_event);
//            imgCal.setColorFilter(getResources().getColor(color));

            }
                FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                Snackbar.make(view, "Start an activity to add an event passing the date of today", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                startActivity( new Intent(getContext() , AddNoteActivity.class));
//
                        FragmentManager fm = getChildFragmentManager();
                        AddEventDialogFragment dialog = AddEventDialogFragment.newInstance(null, mDateString );
                        dialog.setCancelable(false);
                        dialog.show(fm, "addEvent");

                    }
                });


        }

//        mCurrentDate = view.findViewById(R.id.current_date);
//        converEthiopicDate(date);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!= null){
            mEvent = mDb.mDatabaseDao().getEvents(mDateString);
            mAdapter.setData(mEvent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppDatabse.destroyInstance();
    }
}
