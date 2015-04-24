package com.driverapp.android.views;

import android.animation.TimeInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Jesus Christ. Amen.
 */
public class MaterialInterpolator implements Interpolator {

    private static final MaterialInterpolator INSTANCE = new MaterialInterpolator();

    public static MaterialInterpolator getInstance() {
        return INSTANCE;
    }

    @Override
    public float getInterpolation(float x) {
        return (float) (6 * Math.pow(x, 2) - 8 * Math.pow(x, 3) + 3 * Math.pow(x, 4));
    }
}