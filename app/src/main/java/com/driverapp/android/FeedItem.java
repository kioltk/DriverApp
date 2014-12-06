package com.driverapp.android;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedItem {
    public int id;
    public String body;
    public ArrayList<String> photos = new ArrayList<>();
    public LatLng geodata = new LatLng(0,0);
    public double rating;
    public int category;
    public ArrayList<FeedComment> feedComments = new ArrayList<>();
}
