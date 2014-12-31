package com.driverapp.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.models.Event;
import com.driverapp.android.models.EventComment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class CommentsAdapter extends BaseAdapter {

    private final ArrayList<EventComment> comments;
    private final Context context;

    public CommentsAdapter(Context context, ArrayList<EventComment> comments){
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public EventComment getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return comments.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);

        ImageView userPhoto = (ImageView) itemView.findViewById(R.id.user_photo);
        TextView dateView = (TextView) itemView.findViewById(R.id.date);
        TextView bodyView = (TextView) itemView.findViewById(R.id.body);

        EventComment comment = getItem(position);

        bodyView.setText( comment.getUserName() + " " + comment.comment);
        dateView.setText(comment.date_create);
        ImageLoader.getInstance().displayImage(comment.user_photo_path, userPhoto);

        return itemView;
    }
}
