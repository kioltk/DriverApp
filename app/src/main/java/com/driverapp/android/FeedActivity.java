package com.driverapp.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        Bundle extras = getIntent().getExtras();
        ImageView image = (ImageView) findViewById(R.id.image);

        TextView bodyView = (TextView) findViewById(R.id.body);
        String body = extras.getString(EXTRA_BODY,"");
        bodyView.setText(body);

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
