package com.driverapp.android.feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedViewHolder extends RecyclerView.ViewHolder {
    private final TextView bodyView;
    private final TextView titleView;
    private final TextView addressView;
    private final TextView categoryView;
    private final ImageView imageView;
    private TextView userNameView;
//    private final TextView ratingView;

    public FeedViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        bodyView = (TextView) itemView.findViewById(R.id.body);
        addressView = (TextView) itemView.findViewById(R.id.address);
        userNameView = (TextView) itemView.findViewById(R.id.name);
        categoryView = (TextView) itemView.findViewById(R.id.category);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        //ratingView = (TextView) itemView.findViewById(R.id.rating);
    }

    public void setTitle(String title) {
        //titleView.setText(title);
    }
    public void setBody(String body){
        bodyView.setText(body);
    }
    public void setOnItemClick(final FeedItem item){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(FeedActivity.getActivityIntent(v.getContext(), item));
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
