package com.driverapp.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.driverapp.android.models.Comment;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class CommentsAdapter extends BaseAdapter {

    private final ArrayList<Comment> comments;
    private final Context context;

    public CommentsAdapter(Context context, ArrayList<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);

        return itemView;
    }
}
