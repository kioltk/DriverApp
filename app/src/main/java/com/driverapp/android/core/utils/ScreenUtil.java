package com.driverapp.android.core.utils;

import android.util.DisplayMetrics;

import static com.driverapp.android.DriverApp.app;

/**
 * Created by kioltk on 4/6/15.
 */
public class ScreenUtil {
    private static float density;
    private static float scaledDensity;
    private static DisplayMetrics metrics;

    public static DisplayMetrics displayMetrics(){
        if(metrics==null){
            metrics = app().getResources().getDisplayMetrics();
        }
        return metrics;
    }

    public static int dp(float dp) {
        if (density == 0f)
            density = app().getResources().getDisplayMetrics().density;

        return (int) (dp * density + .5f);
    }

    public static int sp(float sp) {
        if (scaledDensity == 0f)
            scaledDensity = app().getResources().getDisplayMetrics().scaledDensity;

        return (int) (sp * scaledDensity + .5f);
    }

    public static int getWidth() {
        return app().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight() {
        return app().getResources().getDisplayMetrics().heightPixels;
    }

}
