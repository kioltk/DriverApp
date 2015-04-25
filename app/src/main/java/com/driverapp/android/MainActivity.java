package com.driverapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.events.create.CreateActivity;
import com.driverapp.android.events.feed.EventListFragment;
import com.driverapp.android.events.feed.EventMapFragment;
import com.driverapp.android.profile.MyEventsFragment;
import com.driverapp.android.profile.ProfileActivity;
import com.driverapp.android.settings.SettingsActivity;
import com.facebook.appevents.AppEventsLogger;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //toolbar.setLogo(R.drawable.ic_logo_ab_compat);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new EventListFragment())
                        .commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new EventMapFragment())
                        .commit();
                break;
            case 4:
                startActivity(new Intent(this, CreateActivity.class));
                break;
            case 5:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new MyEventsFragment())
                        .commit();
                break;
            case 7:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new Fragment())
                        .commit();
        }
    }


    public static class MainFragment extends Fragment{

        private static final int VIEW_LIST = 0;
        private static final int VIEW_MAP = 1;

        private int currentView = 0;
        private Fragment updatableFragment;
        private View rootView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);


            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            updatableFragment = new EventListFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.feedContainer, updatableFragment)
                    .commit();

            View addButton = rootView.findViewById(R.id.create);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(CreateActivity.getActivityIntent(getActivity()));
                }
            });

            final FloatingActionButton viewToggler = (FloatingActionButton) rootView.findViewById(R.id.toggler);
            viewToggler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentView == VIEW_MAP) {
                        currentView = VIEW_LIST;
                        viewToggler.setImageResource(R.drawable.ic_map);
                    } else {
                        viewToggler.setImageResource(R.drawable.ic_cards);
                    }
                }
            });
        }

        void update(){

        }



    }
}
