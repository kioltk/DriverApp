package com.driverapp.android.feed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.driverapp.android.R;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedAdapter extends RecyclerView.Adapter {

    private final Context context;
    private ArrayList<FeedItem> items;

    public FeedAdapter(ArrayList<FeedItem> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = View.inflate(context, R.layout.item_feed, null);
        FeedViewHolder feedViewHolder = new FeedViewHolder(itemView);
        return feedViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        FeedItem item = items.get(i);
        if(viewHolder instanceof FeedViewHolder) {
            FeedViewHolder feedViewHolder = (FeedViewHolder) viewHolder;
            feedViewHolder.setTitle("id: " + item.id);
            feedViewHolder.setBody(item.body);
            feedViewHolder.setRating(item.rating);
            feedViewHolder.setOnItemClick(item);
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
