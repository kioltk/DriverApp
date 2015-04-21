package com.driverapp.android.events.feed;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.driverapp.android.MainActivity;
import com.driverapp.android.R;
import com.driverapp.android.core.utils.ScreenUtil;
import com.driverapp.android.events.create.CreateActivity;
import com.driverapp.android.models.Event;

import java.util.ArrayList;

/**
 * Created by d_great on 24.12.14.
 */
public class EventListFragment extends Fragment  {

    private MainActivity activity;
    private View rootView;

    private ArrayList<Event> items;
    private TextView statusView;
    private View progressView;
    private RecyclerView recycler;
    private View createButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed_list, null);

        setHasOptionsMenu(true);

        statusView = (TextView) rootView.findViewById(R.id.status);
        progressView = rootView.findViewById(R.id.progress);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        createButton = rootView.findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateActivity.getActivityIntent(getActivity()));
            }
        });

        if(ScreenUtil.isTablet())
            recycler.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        else
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        new EventListTask(){


            @Override
            protected void onSuccess(ArrayList<Event> result) {
                progressView.setVisibility(View.GONE);
                items = result;
                recycler.setAdapter(new EventListAdapter(items, activity));
            }

            @Override
            protected void onError(Exception exp) {
                progressView.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                statusView.setText(exp.getMessage());
            }
        }.execute();
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_feed, menu);
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
                return true;
            case R.id.action_refresh:
                update();
                return true;
            /*case R.id.action_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                return true;*/
        }


        return super.onOptionsItemSelected(item);
    }

    public void update() {
        progressView.setVisibility(View.VISIBLE);
        recycler.setVisibility(View.GONE);
        new EventListTask(){


            @Override
            protected void onSuccess(ArrayList<Event> result) {
                progressView.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
                items = result;
                recycler.setAdapter(new EventListAdapter(items, activity));
            }

            @Override
            protected void onError(Exception exp) {
                progressView.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                statusView.setText(exp.getMessage());
            }
        }.execute();
    }
}
