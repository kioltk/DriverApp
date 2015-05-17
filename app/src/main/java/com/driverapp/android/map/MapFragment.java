package com.driverapp.android.map;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.core.Core;
import com.driverapp.android.core.utils.ScreenUtil;
import com.driverapp.android.events.EventActivity;
import com.driverapp.android.events.feed.EventListTask;
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
public class MapFragment extends SupportMapFragment {

    private View rootView;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private HashMap<String, Event> eventMarkersHash = new HashMap();
    private View eventContainer;
    private TextView titleView;
    //private TextView bodyView;
    //private TextView addressView;
    private TextView statsView;
    private ImageView imageView;
    private boolean showingItem = false;
    private TextView addressView;
    private TextView bodyView;
    private ImageView locationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);
        RelativeLayout contentContainer = (RelativeLayout) mapView.findViewById(1).getParent();

        try {
            // Get the button view
            locationButton = (ImageView) contentContainer.findViewById(2);

            locationButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            locationButton.setImageResource(R.drawable.picker_map_location);
            locationButton.setBackgroundResource(R.drawable.picker_map_location_background);
            // and next place it, for exemple, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.width = ScreenUtil.dp(60);
            layoutParams.height = ScreenUtil.dp(60);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, ScreenUtil.dp(20), ScreenUtil.dp(20));
            locationButton.setLayoutParams(layoutParams);
        } catch (Exception exp){

        }

        eventContainer = inflater.inflate(R.layout.fragment_map_event_item, contentContainer, false);
        setHasOptionsMenu(true);
        imageView = (ImageView) eventContainer.findViewById(R.id.image);
        titleView = (TextView) eventContainer.findViewById(R.id.title);
        bodyView = (TextView) eventContainer.findViewById(R.id.body);
        addressView = (TextView) eventContainer.findViewById(R.id.address);
        statsView = (TextView) eventContainer.findViewById(R.id.stats);
        RelativeLayout.LayoutParams eventContainerParams = (RelativeLayout.LayoutParams) eventContainer.getLayoutParams();
        eventContainerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        contentContainer.addView(eventContainer, eventContainerParams);
        return mapView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        setUpMapIfNeeded();
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
            return true;*//*
            *//*case R.id.action_profile:
            startActivity(new Intent(getActivity(), ProfileActivity.class));
            return true;*/
        }


        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = getMap();
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
                    int iconRes = Core.categories().get(event.categoryId-1).pinResId;
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
                    eventContainer.animate()
                            .translationY(eventContainer.getHeight()).setDuration(0).start();
                    eventContainer.animate()
                            .setInterpolator(new MaterialInterpolator())
                            .translationY(0)
                            .setDuration(250)
                            .start();
                    locationButton.animate()
                            .translationY(-eventContainer.getHeight())
                            .setInterpolator(new MaterialInterpolator())
                            .setDuration(250)
                            .start();
                }
                titleView.setText(event.categoryName);
                addressView.setText(event.address);
                bodyView.setText(event.desc);
                statsView.setText(getString(R.string.stats, event.count_comments, event.count_likes));
                imageView.setImageResource(R.drawable.event_item_placeholder);
                ImageLoader.getInstance().displayImage(event.photo_path, imageView);
                // todo likes and comments count?
                showingItem = true;
                eventContainer.setOnClickListener(new View.OnClickListener() {
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
                if (focusedOnce) return;
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