package com.driverapp.android.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class Event {
    public int id;
    public String desc;
    public String photo_path;


    public float lat;
    public float lon;
    public String city;
    public String address;
    private LatLng geodata;
    @SerializedName("date_create")
    public int date;

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

    @SerializedName("category_id")
    public int categoryId;
    @SerializedName("category_name")
    public String categoryName;
    @SerializedName("category_color")
    public String categoryColor;

    public int count_likes;
    public int count_comments;


    public String getUserFullName() {
        return user_name + " " + user_surname;
    }
}
