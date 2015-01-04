package com.driverapp.android.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.events.comments.EventCommentsActivity;
import com.driverapp.android.events.comments.EventCommentsAdapter;
import com.driverapp.android.models.Event;
import com.driverapp.android.models.EventComment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventActivity extends BaseActivity {


    private static final String EXTRA_ID = "extra_id";
    private TextView addressView;
    private TextView userNameView;
    private TextView categoryView;
    private TextView bodyView;
    private ImageView imageView;
    private ImageView userPhotoView;
    private TextView dateView;
    private ListView commentsList;
    private View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        commentsList = (ListView) findViewById(R.id.list);

        contentView = getLayoutInflater().inflate(R.layout.activity_event_content, null);

        commentsList.addHeaderView(contentView, null, false);
        commentsList.setAdapter(new EventCommentsAdapter(this, new ArrayList<EventComment>()));
        //commentsList.addFooterView(null);

        imageView = (ImageView) contentView.findViewById(R.id.image);
        bodyView = (TextView) contentView.findViewById(R.id.body);
        dateView = (TextView) contentView.findViewById(R.id.date);
        addressView = (TextView) contentView.findViewById(R.id.address);
        userNameView = (TextView) contentView.findViewById(R.id.user_name);
        userPhotoView = (ImageView) contentView.findViewById(R.id.user_photo);
        categoryView = (TextView) contentView.findViewById(R.id.category);
        View comments =  contentView.findViewById(R.id.comment_holder);
        View likeButton =  contentView.findViewById(R.id.like_holder);


        Bundle extras = getIntent().getExtras();
        final int id = extras.getInt(EXTRA_ID, 0);
        new EventTask(id) {
            @Override
            protected void onSuccess(Event result) {
                bodyView.setText(result.desc);
                dateView.setText(result.date_create);
                categoryView.setText(result.category_name);
                addressView.setText(result.address);
                userNameView.setText(result.getUserName());
                ImageLoader.getInstance().displayImage(result.user_avatar_path, userPhotoView);
                ImageLoader.getInstance().displayImage(result.photo_path, imageView);
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EventCommentsActivity.getActivityIntent(getBaseContext(), id));
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeTogglerTask likeTogglerTask = new LikeTogglerTask(id) {
                    @Override
                    protected void onSuccess(LikeToggleResult result) {
                        Toast.makeText(getBaseContext(), result.act,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onError(Exception exp) {
                        Toast.makeText(getBaseContext(), "Error " + exp.toString(),Toast.LENGTH_SHORT).show();
                    }
                };
                likeTogglerTask.start();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getActivityIntent(Context context, Event item) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, item.id);
        //String[] photosArray = new String[item.photos.size()];
        //item.photos.toArray(photosArray);
        //bundle.putStringArray(EXTRA_PHOTOS_ARRAY, photosArray);
        // bundle.putDouble(EXTRA_RATING, item.rating);
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}