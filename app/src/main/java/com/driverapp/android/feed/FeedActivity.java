package com.driverapp.android.feed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.CommentsActivity;
import com.driverapp.android.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedActivity extends ActionBarActivity {


    private static final String EXTRA_ID = "extra_id";
    private TextView addressView;
    private TextView userNameView;
    private TextView categoryView;
    private TextView bodyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ImageView image = (ImageView) findViewById(R.id.image);
        bodyView = (TextView) findViewById(R.id.body);
        addressView = (TextView) findViewById(R.id.address);
        userNameView = (TextView) findViewById(R.id.name);
        categoryView = (TextView) findViewById(R.id.category);
        ImageButton comments = (ImageButton) findViewById(R.id.comment);

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(EXTRA_ID, 0);
        new FeedTask(id) {
            @Override
            protected void onSuccess(FeedItem result) {

                bodyView.setText(result.desc);
                categoryView.setText(result.category_name);
                addressView.setText(result.address);
                userNameView.setText(result.getUserName());
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), CommentsActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getActivityIntent(Context context, FeedItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, item.id);
        //String[] photosArray = new String[item.photos.size()];
        //item.photos.toArray(photosArray);
        //bundle.putStringArray(EXTRA_PHOTOS_ARRAY, photosArray);
       // bundle.putDouble(EXTRA_RATING, item.rating);
        Intent intent = new Intent(context, FeedActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}
