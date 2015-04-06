package com.driverapp.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.core.utils.Updatable;
import com.driverapp.android.events.create.CreateActivity;
import com.driverapp.android.events.feed.EventListFragment;
import com.driverapp.android.events.feed.EventMapFragment;
import com.driverapp.android.profile.ProfileActivity;
import com.driverapp.android.settings.SettingsActivity;
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
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new MainFragment())
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

            View addButton = rootView.findViewById(R.id.add);
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
                        updatableFragment = new EventListFragment();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.feedContainer, updatableFragment)
                                .commit();
                        currentView = VIEW_LIST;
                        viewToggler.setImageResource(R.drawable.ic_map);
                    } else {
                        updatableFragment = new EventMapFragment();
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.feedContainer, updatableFragment)
                                .commit();
                        currentView = VIEW_MAP;
                        viewToggler.setImageResource(R.drawable.ic_cards);
                    }
                }
            });
        }

        void update(){
            if(updatableFragment instanceof Updatable){
                ((Updatable) updatableFragment).update();
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
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
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    return true;
                case R.id.action_refresh:
                    update();
                    return true;
                case R.id.action_profile:
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                    return true;
            }


            return super.onOptionsItemSelected(item);
        }
    }
}
