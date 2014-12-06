package com.driverapp.android;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ArrayList<FeedItem> items = new ArrayList<FeedItem>(){{
        add(new FeedItem(){{
            body = "ДПС";
            id = 1;
            rating = 5.0;
            category = FeedItemType.POLICE;
            feedComments.add(new FeedComment(){{ body = "Hello world."; }});
        }});
        add(new FeedItem(){{
            body = "Авария";
            id = 2;
            rating = 5.0;
            category = FeedItemType.DISASTER;
            feedComments.add(new FeedComment(){{ body = "Hello world."; }});
        }});
        add(new FeedItem(){{
            body = "ДПС";
            id = 3;
            rating = 5.0;
            category = FeedItemType.POLICE;
            feedComments.add(new FeedComment(){{ body = "Hello world."; }});
        }});
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(new FeedAdapter(items, this));

        View addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateActivity.getActivityIntent(getBaseContext()));
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
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
