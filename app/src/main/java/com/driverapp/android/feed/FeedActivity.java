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
    private static final String EXTRA_BODY = "extra_body";
    private static final String EXTRA_COMMENTS_COUNT = "extra_comments_count";
    private static final String EXTRA_GEODATA_LAT = "extra_geodata_lat";
    private static final String EXTRA_GEODATA_LONG = "extra_geodata_long";
    private static final String EXTRA_RATING = "extra_rating";
    private static final String EXTRA_PHOTOS_ARRAY = "extra_photos_array";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ImageView image = (ImageView) findViewById(R.id.image);
        TextView bodyView = (TextView) findViewById(R.id.body);
        ImageButton comments = (ImageButton) findViewById(R.id.comment);

        Bundle extras = getIntent().getExtras();
        String body = extras.getString(EXTRA_BODY,"");

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),CommentsActivity.class));
            }
        });
        bodyView.setText(body);
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
        bundle.putString(EXTRA_BODY, item.body);
        bundle.putInt(EXTRA_COMMENTS_COUNT, item.feedComments.size());
        bundle.putDouble(EXTRA_GEODATA_LAT, item.geodata.latitude);
        bundle.putDouble(EXTRA_GEODATA_LONG, item.geodata.longitude);
        String[] photosArray = new String[item.photos.size()];
        item.photos.toArray(photosArray);
        bundle.putStringArray(EXTRA_PHOTOS_ARRAY, photosArray);
        bundle.putDouble(EXTRA_RATING, item.rating);
        Intent intent = new Intent(context, FeedActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}
