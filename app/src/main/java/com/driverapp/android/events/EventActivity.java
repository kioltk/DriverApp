package com.driverapp.android.events;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.core.utils.DeviceUtil;
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
    private View progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        commentsList = (ListView) findViewById(R.id.list);

        contentView = getLayoutInflater().inflate(R.layout.activity_event_content, null);

        commentsList.addHeaderView(contentView, null, false);
        commentsList.setAdapter(new EventCommentsAdapter(this, new ArrayList<EventComment>()));
        //commentsList.addFooterView(null);


        progress = findViewById(R.id.progress);

        imageView = (ImageView) contentView.findViewById(R.id.image);
        bodyView = (TextView) contentView.findViewById(R.id.body);
        dateView = (TextView) contentView.findViewById(R.id.date);
        addressView = (TextView) contentView.findViewById(R.id.address);
        userNameView = (TextView) contentView.findViewById(R.id.user_name);
        userPhotoView = (ImageView) contentView.findViewById(R.id.user_photo);
        categoryView = (TextView) contentView.findViewById(R.id.category);
        View likeButton =  contentView.findViewById(R.id.like_holder);


        Bundle extras = getIntent().getExtras();
        final int id = extras.getInt(EXTRA_ID, 0);
        new EventTask(id) {
            @Override
            protected void onSuccess(Event result) {
                progress.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
                bodyView.setText(result.desc);
                dateView.setText(result.date_create);
                categoryView.setText(result.category_name);
                addressView.setText(result.address);
                userNameView.setText(result.getUserName());
                ImageLoader.getInstance().displayImage(result.user_avatar_path, userPhotoView);
                if (result.photo_path == null) {
                    imageView.setVisibility(View.INVISIBLE);
                    final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                            new int[] { android.R.attr.actionBarSize });
                    int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
                    styledAttributes.recycle();
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    params.height = mActionBarSize;
                    imageView.setLayoutParams(params);


                } else {
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    params.height = DeviceUtil.getDisplayMetrics().widthPixels;
                    imageView.setLayoutParams(params);
                    ImageLoader.getInstance().displayImage(result.photo_path, imageView);
                }
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();



        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*if(UserUtil.user_id==0){
                    new AlertDialog.Builder(EventActivity.this)
                            .setTitle("Сначала нужно зайти")
                            .setMessage("Пока не сделано")
                            .setPositiveButton("Зайти", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("Ладно", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    return;
                }*/

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
        setBackButtonEnabled();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
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

    public static Intent getActivityIntent(Context context, int eventid) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, eventid);
        //String[] photosArray = new String[item.photos.size()];
        //item.photos.toArray(photosArray);
        //bundle.putStringArray(EXTRA_PHOTOS_ARRAY, photosArray);
        // bundle.putDouble(EXTRA_RATING, item.rating);
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}