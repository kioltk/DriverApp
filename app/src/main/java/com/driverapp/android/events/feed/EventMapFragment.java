package com.driverapp.android.events.feed;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.core.Core;
import com.driverapp.android.events.EventActivity;
import com.driverapp.android.models.Event;
import com.driverapp.android.views.MaterialInterpolator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    //private TextView bodyView;
    //private TextView addressView;
    private TextView statsView;
    private ImageView imageView;
    private boolean showingItem = false;
    private TextView addressView;
    private TextView bodyView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed_map, container, false);
        setHasOptionsMenu(true);
        eventHolder = rootView.findViewById(R.id.event);
        imageView = (ImageView) eventHolder.findViewById(R.id.image);
        titleView = (TextView) rootView.findViewById(R.id.title);
        bodyView = (TextView) rootView.findViewById(R.id.body);
        addressView = (TextView) rootView.findViewById(R.id.address);
        statsView = (TextView) rootView.findViewById(R.id.stats);

        setUpMapIfNeeded();

        return rootView;
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
        switch (id) {
            case R.id.action_settings:
                return true;
            /*case R.id.action_refresh:
                update();
                return true;*/
            /*case R.id.action_profile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                return true;*/
        }


        return super.onOptionsItemSelected(item);
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

        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);


        new EventListTask() {
            @Override
            protected void onSuccess(ArrayList<Event> result) {
                for (Event event : result) {
                    int iconRes = Core.getCategories().get(event.category_id-1).pinResId;
                    MarkerOptions option = new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(iconRes))
                            .position(event.getGeodata())
                            .title(event.desc);
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
                final Event event = eventMarkersHash.get(marker.getId());
                if (!showingItem) {
                    eventHolder.animate()
                            .setInterpolator(new MaterialInterpolator())
                            .translationY(eventHolder.getHeight()).setDuration(0).start();
                    eventHolder.animate().translationY(0).setDuration(250).start();
                }
                titleView.setText(event.category_name);
                addressView.setText(event.address);
                bodyView.setText(event.desc);
                statsView.setText(getString(R.string.stats, event.count_comments, event.count_likes));
                imageView.setImageResource(R.drawable.event_item_placeholder);
                ImageLoader.getInstance().displayImage(event.photo_path, imageView);
                // todo likes and comments count?
                showingItem = true;
                eventHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(EventActivity.open(getActivity(), event.id));
                    }
                });
                return true;
            }
        });
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            public boolean focusedOnce;

            @Override
            public void onMyLocationChange(Location location) {
                if(focusedOnce) return;
                focusedOnce = true;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

            }
        });
    }

    public void update() {
        mMap.clear();
        eventMarkersHash.clear();
        setUpMap();
    }
}