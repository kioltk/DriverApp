package com.driverapp.android.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.driverapp.android.DriverApp;

/**
 * Created by Jesus Christ. Amen.
 */
public class DeviceUtil {
    private static DisplayMetrics metrics;

    public static DisplayMetrics getDisplayMetrics() {
        return metrics;
    }
    public static void init(Context context){

        metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

    }

}
