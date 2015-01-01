package com.driverapp.android.events.feed;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.driverapp.android.MainActivity;
import com.driverapp.android.R;
import com.driverapp.android.models.Event;

import java.util.ArrayList;

/**
 * Created by d_great on 24.12.14.
 */
public class EventListFragment extends Fragment {

    private MainActivity activity;
    private View rootView;

    private ArrayList<Event> items;
    private TextView statusView;
    private View progressView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed_list, null);

        statusView = (TextView) rootView.findViewById(R.id.status);
        progressView = rootView.findViewById(R.id.progress);
        final RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(activity));

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
}
