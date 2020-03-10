package com.ninji.basiccalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.ninji.basiccalendar.RecyclerAdapters.NoteRecyclerAdapter;
import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.database.DataManager;
import com.ninji.basiccalendar.model.Note;

import java.util.List;

public class NotesFragment extends Fragment {

    private NoteRecyclerAdapter mAdapter;
    private AppDatabse mDb;
    private List<Note> mNoteList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes , container , false);

        DataManager dataManager = DataManager.getInstance();
        RecyclerView noteRecyler = view.findViewById(R.id.note_recycler);

        mDb = AppDatabse.getInstance(getContext());

        mNoteList = mDb.mDatabaseDao().getNotes();
        mAdapter = new NoteRecyclerAdapter(getActivity() , mNoteList);
//        mAdapter = new NoteRecyclerAdapter(getActivity() );

        noteRecyler.setLayoutManager(new LinearLayoutManager(getContext()));
        noteRecyler.setAdapter(mAdapter);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , AddNoteActivity.class);
                startActivity(intent);
            }
        });
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        mNoteList = null;
        mNoteList = mDb.mDatabaseDao().getNotes();
        mAdapter.setData( mNoteList );
    }


    @Override
    public void onDestroy() {
        AppDatabse.destroyInstance();
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Calendar", "QueryTextSubmit: " + s);
                s = "%"+s+"%";
                mNoteList = mDb.mDatabaseDao().searchNote(s);
                mAdapter.setData( mNoteList );

//                searchView.setQuery("", false);

                return true;
            }


            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("Calendar", "QueryTextChange: " + s);
                if ( s.equals("")){
                    mNoteList = mDb.mDatabaseDao().getNotes();
                    mAdapter.setData( mNoteList );
                }
                return false;
            }


        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if ( item.getItemId() == R.id.menu_item_search)
//            itme
        return false;
    }
}




























