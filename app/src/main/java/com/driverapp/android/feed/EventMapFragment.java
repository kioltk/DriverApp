package com.driverapp.android.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.driverapp.android.R;
import com.driverapp.android.models.Event;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by d_great on 24.12.14.
 */
public class EventMapFragment extends Fragment {

    private View rootView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed_map, null);


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
                    mMap.addMarker(new MarkerOptions().position(event.getGeodata()).title(event.desc));
                }
            }

            @Override
            protected void onError(Exception exp) {

            }
        }.execute();
    }
}
