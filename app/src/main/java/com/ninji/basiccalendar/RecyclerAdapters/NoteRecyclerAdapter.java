package com.ninji.basiccalendar.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ninji.basiccalendar.AddNoteActivity;
import com.ninji.basiccalendar.R;
import com.ninji.basiccalendar.database.AppDatabse;
import com.ninji.basiccalendar.model.Note;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    private List<Note> mList ;
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
//    private AppDatabse db ;

    public NoteRecyclerAdapter( Context context , List<Note> noteList) {
//        db = AppDatabse.getInstance(mContext);
//        mList = db.mDatabaseDao().getNotes();
        mList = noteList;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View itemView  = mLayoutInflater.inflate(R.layout.item_note_list , viewGroup , false);
       return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.bind(i);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Note> noteList) {
        mList = noteList ;
        notifyDataSetChanged();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {

        int mPositon ;
        private final TextView mDescription;
        private final TextView mTitle;
        private Note mNote;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.note_title_tv);
            mDescription = itemView.findViewById(R.id.note_desc_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = AddNoteActivity.newIntent(mContext , mNote.getID());
                    mContext.startActivity(intent);
                }
            });
        }

        private void bind ( int pos ){
            mPositon = pos ;
            mNote = mList.get(mPositon);
            mTitle.setText(mNote.getTitle());
            mDescription.setText(mNote.getNote());

        }
    }


}
