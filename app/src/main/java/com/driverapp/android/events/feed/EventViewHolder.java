package com.driverapp.android.events.feed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.ViewHolder;
import com.driverapp.android.core.utils.DeviceUtil;
import com.driverapp.android.core.utils.ImageUtil;
import com.driverapp.android.events.EventActivity;
import com.driverapp.android.events.comments.EventCommentsActivity;
import com.driverapp.android.models.Event;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    private ImageView bigLikeView;
//    private final TextView ratingView;

    public EventViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        bodyView = (TextView) itemView.findViewById(R.id.body);
        addressView = (TextView) itemView.findViewById(R.id.address);
        userNameView = (TextView) itemView.findViewById(R.id.user_name);
        categoryView = (TextView) itemView.findViewById(R.id.category);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        likeView = itemView.findViewById(R.id.like_holder);
        shareView = itemView.findViewById(R.id.share_holder);
        commentView = itemView.findViewById(R.id.comment_holder);
        bigLikeView = (ImageView) itemView.findViewById(R.id.like_big);


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
        imageView.setOnClickListener(new View.OnClickListener() {
            public long lastTimeClick = 0;

            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() - lastTimeClick < 250){
                    bigLikeView.setVisibility(View.VISIBLE);
                    bigLikeView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           bigLikeView.setVisibility(View.GONE);
                        }
                    }, 2000);
                }else{
                    lastTimeClick = System.currentTimeMillis();
                }
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
        int width = DeviceUtil.getDisplayMetrics().widthPixels;
        imageView.getLayoutParams().height = width/2;
        imageView.requestLayout();

        imageView.setImageResource(R.drawable.event_item_placeholder);
        ImageLoader.getInstance().loadImage(photoPath, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Bitmap croppedImage = ImageUtil.cropByHeightAndCenter(loadedImage);
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
}