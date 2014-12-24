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
import com.driverapp.android.feed.FeedListTask;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

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

        ActionBar actionBar = getSupportActionBar();


        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        new FeedListTask(){


            @Override
            protected void onSuccess(ArrayList<FeedItem> result) {
                Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onError(Exception exp) {
                Toast.makeText(getBaseContext(), exp.toString(), Toast.LENGTH_SHORT).show();

            }
        }.execute();
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
