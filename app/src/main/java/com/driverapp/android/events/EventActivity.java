package com.driverapp.android.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.events.comments.EventCommentsAdapter;
import com.driverapp.android.events.create.CreateActivity;
import com.driverapp.android.models.Event;
import com.driverapp.android.models.EventComment;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventActivity extends BaseActivity {


    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_SCROLL_TO_COMMENT = "extra_scroll_to_comment";
    private RecyclerView contentRecycler;
    private View contentView;
    private View progress;
    private EventCommentsAdapter adapter;
    private int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        contentRecycler = (RecyclerView) findViewById(R.id.recycler);
        contentRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventCommentsAdapter(this, new ArrayList<EventComment>());
        contentRecycler.setAdapter(adapter);
        //contentRecycler.addFooterView(null);


        progress = findViewById(R.id.progress);



        Bundle extras = getIntent().getExtras();
        id = extras.getInt(EXTRA_ID, 0);
        new EventTask(id) {
            @Override
            protected void onSuccess(Event result) {
                progress.setVisibility(View.GONE);
                adapter.setEvent(result);
                fetchComments();
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();




        setBackButtonEnabled();

    }

    private void fetchComments() {
        new CommentsListTask(id) {
            @Override
            protected void onSuccess(ArrayList<EventComment> result) {
                adapter.setComments(result);
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();
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
        } else if (id==R.id.action_create){
            startActivity(CreateActivity.getActivityIntent(this));
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getActivityIntent(Context context, int eventid, boolean scrollToComment) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, eventid);
        bundle.putBoolean(EXTRA_SCROLL_TO_COMMENT, scrollToComment);
        //String[] photosArray = new String[item.photos.size()];
        //item.photos.toArray(photosArray);
        //bundle.putStringArray(EXTRA_PHOTOS_ARRAY, photosArray);
        // bundle.putDouble(EXTRA_RATING, item.rating);
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}