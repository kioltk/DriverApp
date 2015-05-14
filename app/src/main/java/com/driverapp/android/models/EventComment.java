package com.driverapp.android.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Christ. Amen.
 */
public class EventComment {
    public int id;
    public String comment;
    @SerializedName("date_create")
    public int date;

    public int user_id;
    public String user_name;
    public String user_surname;
    public String user_photo_path;

    public String getUserName(){
        return user_name + " " + user_surname;
    }

    public int event_id;

}
