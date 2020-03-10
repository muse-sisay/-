package com.ninji.basiccalendar.RecyclerAdapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ninji.basiccalendar.AddEventDialogFragment;
import com.ninji.basiccalendar.R;
import com.ninji.basiccalendar.model.Event;

import java.util.List;

public class EventRecyclerAdapter  extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder>{

    List<Event> mEventsList ;
    private Context mContext;
    private  final LayoutInflater mLayoutInflater ;

    public EventRecyclerAdapter(Context context , List<Event> list) {
        mEventsList = list ;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  mLayoutInflater.inflate(R.layout.item_event_list,viewGroup , false );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return mEventsList.size();
    }

    public void setData( List<Event> event) {
        mEventsList = event;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        int mPositon ;
        private final TextView mDescription;
        private final TextView mTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.event_title_tv);
            mDescription = itemView.findViewById(R.id.event_desc_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext , mPositon + " " , Toast.LENGTH_LONG).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((AppCompatActivity) mContext ).getSupportFragmentManager();
                    Event event = mEventsList.get(mPositon);
                    AddEventDialogFragment dialog = AddEventDialogFragment.newInstance( event.getID() , event.getDate() );
                    dialog.setCancelable(false);
                    dialog.show(fm, "addEvent");
                }
            });

        }

        private void bind ( int pos ){
            mPositon = pos ;

            mTitle.setText(mEventsList.get(mPositon).getEventTitle());
            mDescription.setText(mEventsList.get(mPositon).getEventDescription());

        }
    }
}
