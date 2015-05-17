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

    @SerializedName("user_id")
    public int userId;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_surname")
    public String userLastname;
    @SerializedName("user_photo_path")
    public String userPhoto;

    public String getUserName(){
        return userName + " " + userLastname;
    }

    public int event_id;

}
