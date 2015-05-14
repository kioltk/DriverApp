package com.driverapp.android.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.driverapp.android.R;
import com.driverapp.android.core.utils.ScreenUtil;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by Jesus Christ. Amen.
 */
public class MapFragment extends SupportMapFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = super.onCreateView(inflater, container, savedInstanceState);

        try {
            // Get the button view
            ImageView locationButton = (ImageView) ((View) mapView.findViewById(1).getParent()).findViewById(2);

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
        return mapView;
    }
}