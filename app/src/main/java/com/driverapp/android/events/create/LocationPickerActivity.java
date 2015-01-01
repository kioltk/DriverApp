package com.driverapp.android.events.create;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.driverapp.android.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationPickerActivity extends FragmentActivity {

    public static final String EXTRA_LAT = "extra_lat";
    public static final String EXTRA_LONGITUDE = "extra_lon";
    public static final String EXTRA_STREET = "extra_street";
    public static final java.lang.String EXTRA_CITY = "extra_city";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private View select;
    private Marker currentPick;
    private Address currentPickedAddress;
    private View currentPickView;
    private TextView currentPickTitleView;
    private TextView currentPickSubtitleView;
    private boolean myLocationFocused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();



        currentPickView = findViewById(R.id.current_pick);
        currentPickTitleView = (TextView) findViewById(R.id.current_pick_title);
        currentPickSubtitleView = (TextView) findViewById(R.id.current_pick_subtitle);

        View myLocationButton = findViewById(R.id.my_location);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLocationFocused = !myLocationFocused;
            }
        });

        select = findViewById(R.id.select);
        select.setEnabled(false);
        // findViewById(R.id.select_text).setEnabled(false);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                LatLng geoData = currentPick.getPosition();
                Bundle bundle = new Bundle();
                bundle.putDouble(EXTRA_LAT, geoData.latitude);
                bundle.putDouble(EXTRA_LONGITUDE, geoData.longitude);
                if(currentPickedAddress!=null) {
                    if (currentPickedAddress.getMaxAddressLineIndex() > 0)
                        bundle.putString(EXTRA_STREET, currentPickedAddress.getAddressLine(0));
                    if (currentPickedAddress.getLocality() != null) {
                        bundle.putString(EXTRA_CITY, currentPickedAddress.getLocality());
                    }
                }
                returnIntent.putExtras(bundle);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
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
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (myLocationFocused) {
                    mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location.getLongitude()),
                                    15
                            )
                    );
                }
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (currentPick == null) {
                    mMap.clear();
                    MarkerOptions currentPickOptions = new MarkerOptions()
                            .draggable(true)
                            .position(latLng);
                    currentPick = mMap.addMarker(currentPickOptions);
                } else {
                    currentPick.setPosition(latLng);
                }

                showMapCurrentPin();
            }
        });
    }
    public void showMapCurrentPin(){

        // todo animate


        currentPickView.setVisibility(View.VISIBLE);
        currentPickTitleView.setText("Подождите");
        currentPickSubtitleView.setText("Загрузка адреса");
        AddressTask task = new AddressTask(this, currentPick.getPosition()) {
            @Override
            protected void onPostExecute(Address s) {

                currentPickedAddress = s;
                if(s!=null) {
                    if(s.getMaxAddressLineIndex()>0){
                        currentPickTitleView.setText(s.getAddressLine(0));
                    }
                    if(s.getLocality()!=null){
                        currentPickSubtitleView.setText(s.getLocality() + ", " + s.getCountryName());
                    }else{
                        if (s.getCountryName() != null) {
                            currentPickSubtitleView.setText(s.getCountryName());
                        }
                    }
                }else{
                    currentPickedAddress = null;
                    currentPickTitleView.setText("Адрес не установлен");
                    currentPickSubtitleView.setText("");
                }
                select.setEnabled(true);
            }
        };
        task.execute();

    }
}
