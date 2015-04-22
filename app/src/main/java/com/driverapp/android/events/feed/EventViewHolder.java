package com.driverapp.android.events.feed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseViewHolder;
import com.driverapp.android.core.utils.ImageUtil;
import com.driverapp.android.core.utils.ScreenUtil;
import com.driverapp.android.core.utils.TimeUtils;
import com.driverapp.android.events.EventActivity;
import com.driverapp.android.models.Event;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventViewHolder extends BaseViewHolder {
    protected final TextView bodyView;
    protected final TextView titleView;
    protected final TextView addressView;
    protected final TextView categoryView;
    protected final ImageView imageView;
    protected final View likeView;
    protected final View commentView;
    protected final View shareView;
    protected final View backgroundView;
    protected final View dividerView;
    protected final TextView dateView;
    protected final TextView likesCounterView;
    protected final ImageView userPhotoView;
    protected TextView userNameView;
    protected ImageView bigLikeView;
    protected TextView commentsCounterView;
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
        likesCounterView = (TextView) itemView.findViewById(R.id.likes_counter);
        commentsCounterView = (TextView) itemView.findViewById(R.id.comments_counter);
        shareView = itemView.findViewById(R.id.share_holder);
        commentView = itemView.findViewById(R.id.comment_holder);
        bigLikeView = (ImageView) itemView.findViewById(R.id.like_big);
        backgroundView = itemView.findViewById(R.id.background);
        dividerView = itemView.findViewById(R.id.divider);
        dateView = (TextView) itemView.findViewById(R.id.date);
        userPhotoView = (ImageView) itemView.findViewById(R.id.userPhoto);


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
                v.getContext().startActivity(EventActivity.getActivityIntent(v.getContext(), item.id, false));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            public long lastTimeClick = 0;
            public boolean doubleClicked;

            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() - lastTimeClick < 250){
                    lastTimeClick = 0;
                    doubleClicked = true;
                    bigLikeView.setVisibility(View.VISIBLE);
                    bigLikeView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           bigLikeView.setVisibility(View.GONE);
                        }
                    }, 1200);
                }else{
                    lastTimeClick = System.currentTimeMillis();
                    imageView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!doubleClicked)
                                itemView.performClick();
                        }
                    },300);
                }
            }
        });
        likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "like eventeid: " +item.id, Toast.LENGTH_SHORT).show();
            }
        });
        if(commentView!=null)
        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(EventActivity.getActivityIntent(getContext(), item.id, true));
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
        userPhotoView.setImageResource(R.drawable.photo_placeholder);
        ImageLoader.getInstance().loadImage(userPhoto, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Bitmap croppedImage = ImageUtil.circle(loadedImage);
                userPhotoView.setImageBitmap(croppedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public void setCategoryName(String categoryName) {
        categoryView.setText(categoryName);
    }

    public void setPhoto(String photoPath) {
        if(photoPath == null || photoPath.equals("")){
            imageView.setVisibility(View.GONE);
            dividerView.setVisibility(View.VISIBLE);
            return;
        }
        imageView.setVisibility(View.VISIBLE);
        if(dividerView!=null)
        dividerView.setVisibility(View.GONE);
        int width = ScreenUtil.getWidth()/(ScreenUtil.isTablet()?3:1);
        imageView.getLayoutParams().height = width;
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

    public void setColor(String categoryColor) {
        if(backgroundView!=null) {
            Drawable background = getResources().getDrawable(R.drawable.card_item_background);
            ColorFilter filter = new LightingColorFilter(0xFFFFFFFF, Color.parseColor("#" + categoryColor));
            background.setColorFilter(filter);
            backgroundView.setBackgroundDrawable(background);
        }
    }

    public void setDate(int date) {
        dateView.setText(TimeUtils.getTime(date));
    }

    public void bind(Event event) {

        setTitle("id: " + event.id);
        setBody(event.desc);
        setAddress(event.city + ", " + event.address);
        setBody(event.desc);
        setUserName(event.getUserName());
        setUserPhoto(event.user_avatar_path);
        setCategoryName(event.category_name);
        setColor(event.category_color);
        setPhoto(event.photo_path);
        setDate(event.date_create);

        likesCounterView.setText("" + event.count_likes);
        if(commentsCounterView!=null)
            commentsCounterView.setText("" + event.count_comments);

    }
}
