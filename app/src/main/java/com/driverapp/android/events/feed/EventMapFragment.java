package com.driverapp.android.events.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.models.Event;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventMapFragment extends Fragment {

    private View rootView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<String, Event> eventMarkersHash = new HashMap();
    private View eventHolder;
    private TextView titleView;
    private TextView bodyView;
    private TextView addressView;
    private TextView statsView;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed_map, null);
        eventHolder = rootView.findViewById(R.id.event_holder);
        imageView = (ImageView) eventHolder.findViewById(R.id.image);
        titleView = (TextView) rootView.findViewById(R.id.title);
        bodyView = (TextView) rootView.findViewById(R.id.body);
        addressView = (TextView) rootView.findViewById(R.id.address);
        statsView = (TextView) rootView.findViewById(R.id.stats);
        setUpMapIfNeeded();

        return rootView;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        new EventListTask() {
            @Override
            protected void onSuccess(ArrayList<Event> result) {
                for (Event event : result) {
                    MarkerOptions option = new MarkerOptions().position(event.getGeodata()).title(event.desc);
                    eventMarkersHash.put(mMap.addMarker(option).getId(), event);
                }
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event event = eventMarkersHash.get(marker.getId());
                eventHolder.animate().translationY(-eventHolder.getHeight()).setDuration(0).start();
                eventHolder.animate().translationY(0).setDuration(250).start();

                titleView.setText(event.category_name);
                addressView.setText(event.address);
                bodyView.setText(event.desc);
                statsView.setText(getString(R.string.stats, 0, 0));
                // todo likes and comments count?

                return true;
            }
        });
    }
}