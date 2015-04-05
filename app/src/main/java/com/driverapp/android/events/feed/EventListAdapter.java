package com.driverapp.android.events.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.driverapp.android.R;
import com.driverapp.android.models.Event;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventListAdapter extends RecyclerView.Adapter {

    private final Context context;
    private ArrayList<Event> items;

    public EventListAdapter(ArrayList<Event> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = View.inflate(context, R.layout.item_event, null);
        EventViewHolder eventViewHolder = new EventViewHolder(itemView);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Event item = items.get(i);
        if (viewHolder instanceof EventViewHolder) {
            EventViewHolder eventViewHolder = (EventViewHolder) viewHolder;
            eventViewHolder.setTitle("id: " + item.id);
            eventViewHolder.setBody(item.desc);
            eventViewHolder.setAddress(item.city + ", " + item.address);
            eventViewHolder.setBody(item.desc);
            eventViewHolder.setUserName(item.getUserName());
            eventViewHolder.setUserPhoto(item.user_avatar_path);
            eventViewHolder.setCategoryName(item.category_name);
            eventViewHolder.setColor(item.category_color);
            eventViewHolder.setPhoto(item.photo_path);
            eventViewHolder.setDate(item.date_create);
            eventViewHolder.setOnItemClick(item);
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
