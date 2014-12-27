package com.driverapp.android;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.driverapp.android.create.CreateActivity;
import com.driverapp.android.feed.FeedAdapter;
import com.driverapp.android.feed.FeedComment;
import com.driverapp.android.feed.FeedItem;
import com.driverapp.android.feed.FeedItemType;
import com.driverapp.android.feed.FeedListFragment;
import com.driverapp.android.feed.FeedListTask;
import com.driverapp.android.feed.FeedMapFragment;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    private static final int VIEW_LIST = 0;
    private static final int VIEW_MAP = 1;

    private int currentView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FeedListFragment())
                .commit();

        View addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateActivity.getActivityIntent(getBaseContext()));
            }
        });

        View mapButton = findViewById(R.id.map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentView == VIEW_MAP) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FeedListFragment())
                            .commit();
                    currentView = VIEW_LIST;
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FeedMapFragment())
                            .commit();
                    currentView = VIEW_MAP;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.refresh:
                update();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    void update(){

    }
}
