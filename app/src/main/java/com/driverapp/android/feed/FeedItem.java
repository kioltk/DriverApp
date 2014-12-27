package com.driverapp.android.feed;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class FeedItem {
    public int id;
    public String desc;
    public String photo_path;


    public float lat;
    public float lon;
    public String city;
    public String address;
    private LatLng geodata;
    public LatLng getGeodata(){
        if(geodata==null)
            geodata = new LatLng(lat,lon);
        return geodata;
    }

    public int status;

    public int user_id;
    public String user_name;
    public String user_surname;
    public String user_avatar_path;

    public int category_id;
    public String category_name;
    public String category_color;

    public ArrayList<FeedComment> feedComments = new ArrayList<>();

    public String getUserName() {
        return user_name + " " + user_surname;
    }
}
