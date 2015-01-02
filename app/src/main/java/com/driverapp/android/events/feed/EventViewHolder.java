package com.driverapp.android.events.feed;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.ViewHolder;
import com.driverapp.android.events.EventActivity;
import com.driverapp.android.events.comments.EventCommentsActivity;
import com.driverapp.android.models.Event;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventViewHolder extends ViewHolder {
    private final TextView bodyView;
    private final TextView titleView;
    private final TextView addressView;
    private final TextView categoryView;
    private final ImageView imageView;
    private final View likeView;
    private final View commentView;
    private final View shareView;
    private TextView userNameView;
//    private final TextView ratingView;

    public EventViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        bodyView = (TextView) itemView.findViewById(R.id.body);
        addressView = (TextView) itemView.findViewById(R.id.address);
        userNameView = (TextView) itemView.findViewById(R.id.name);
        categoryView = (TextView) itemView.findViewById(R.id.category);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        likeView = itemView.findViewById(R.id.like);
        shareView = itemView.findViewById(R.id.share);
        commentView = itemView.findViewById(R.id.comment);
        //ratingView = (TextView) itemView.findViewById(R.id.rating);
    }

    public void setTitle(String title) {
        //titleView.setText(title);
    }
    public void setBody(String body){
        bodyView.setText(body);
    }
    public void setOnItemClick(final Event item){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(EventActivity.getActivityIntent(v.getContext(), item));
            }
        });
        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "like eventeid: " +item.id, Toast.LENGTH_SHORT).show();
            }
        });
        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(EventCommentsActivity.getActivityIntent(getContext(), item.id));
            }
        });
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://driverapp.ru/123");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "DriverApp event");
                shareIntent.setType("text/plain");
                getContext().startActivity(Intent.createChooser(shareIntent, "Поделиться"));
            }
        });
    }

    public void setRating(double rating) {
        //ratingView.setText("Rating: " + rating);
    }

    public void setAddress(String address) {
        addressView.setText(address);
    }

    public void setUserName(String userName) {
        userNameView.setText(userName);
    }

    public void setUserPhoto(String userPhoto) {

    }

    public void setCategoryName(String categoryName) {
        categoryView.setText(categoryName);
    }

    public void setPhoto(String photoPath) {
        ImageLoader.getInstance().displayImage(photoPath, imageView);
    }
}
