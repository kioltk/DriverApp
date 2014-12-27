package com.driverapp.android.feed;


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
import android.widget.Toast;

import com.driverapp.android.MainActivity;
import com.driverapp.android.R;

import java.util.ArrayList;

/**
 * Created by d_great on 24.12.14.
 */
public class FeedListFragment extends Fragment {

    private MainActivity activity;
    private View rootView;

    private ArrayList<FeedItem> items;
    private TextView statusView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_feed_list, null);

        statusView = (TextView) rootView.findViewById(R.id.status);

        final RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(activity));

        new FeedListTask(){


            @Override
            protected void onSuccess(ArrayList<FeedItem> result) {
                items = result;
                recycler.setAdapter(new FeedAdapter(items, activity));
            }

            @Override
            protected void onError(Exception exp) {
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
