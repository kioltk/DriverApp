package com.driverapp.android.core.utils;

import android.graphics.Bitmap;

/**
 * Created by Jesus Christ. Amen.
 */
public class ImageUtil {
    public static Bitmap cropByHeightAndCenter(Bitmap loadedImage) {
        if(loadedImage==null){
            return null;
        }
        int width = loadedImage.getWidth();
        int height = loadedImage.getHeight();
        return Bitmap.createBitmap(loadedImage, 0, height/4, width, height/2);
    }
}
