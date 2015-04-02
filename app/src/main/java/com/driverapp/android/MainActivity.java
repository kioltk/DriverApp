package com.driverapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.core.utils.Updatable;
import com.driverapp.android.events.create.CreateActivity;
import com.driverapp.android.events.feed.EventListFragment;
import com.driverapp.android.events.feed.EventMapFragment;
import com.driverapp.android.profile.ProfileActivity;
import com.driverapp.android.settings.SettingsActivity;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends BaseActivity {


    private static final int VIEW_LIST = 0;
    private static final int VIEW_MAP = 1;

    private int currentView = 0;
    private Fragment updatableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar.setLogo(R.drawable.ic_logo_ab_compat);
        updatableFragment = new EventListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, updatableFragment)
                .commit();

        View addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateActivity.getActivityIntent(getBaseContext()));
            }
        });

        final FloatingActionButton viewToggler = (FloatingActionButton) findViewById(R.id.toggler);
        viewToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentView == VIEW_MAP) {
                    updatableFragment = new EventListFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, updatableFragment)
                            .commit();
                    currentView = VIEW_LIST;
                    viewToggler.setImageResource(R.drawable.ic_map);
                } else {
                    updatableFragment = new EventMapFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, updatableFragment)
                            .commit();
                    currentView = VIEW_MAP;
                    viewToggler.setImageResource(R.drawable.ic_cards);
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
            case R.id.action_refresh:
                update();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
    void update(){
        if(updatableFragment instanceof Updatable){
            ((Updatable) updatableFragment).update();
        }
    }
}
