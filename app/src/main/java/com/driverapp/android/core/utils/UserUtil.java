package com.driverapp.android.core.utils;

import static android.content.Context.MODE_MULTI_PROCESS;
import static com.driverapp.android.DriverApp.app;

/**
 * Created by Jesus Christ. Amen.
 */
public class UserUtil {
    public static int id = 0;
    public static String name;
    public static String photo;

    public static void setUserId(int id) {
        UserUtil.id = id;
        app().getSharedPreferences("user", MODE_MULTI_PROCESS)
                .edit()
                .putInt("id",id)
                .apply();
    }

    public static void setUserName(String name) {

        UserUtil.name = name;
        app().getSharedPreferences("user", MODE_MULTI_PROCESS)
                .edit()
                .putString("name", name)
                .apply();
    }

    public static void setUserPhoto(String photo) {

        UserUtil.photo = photo;
        app().getSharedPreferences("user", MODE_MULTI_PROCESS)
                .edit()
                .putString("photo", photo)
                .apply();
    }

    public static String getPhoto() {
        if(photo==null)
            photo = app().getSharedPreferences("user", MODE_MULTI_PROCESS).getString("photo",null);
        return photo;
    }

    public static String getName() {
        if(name==null)
            name = app().getSharedPreferences("user", MODE_MULTI_PROCESS).getString("name", null);
        return name;
    }

    public static int getId() {
        return 1;
        /*if(id==0)
            id = app().getSharedPreferences("user", MODE_MULTI_PROCESS).getInt("id", 0);
        return id;*/
    }
}
