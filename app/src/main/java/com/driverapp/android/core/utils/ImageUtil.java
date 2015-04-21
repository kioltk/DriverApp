package com.driverapp.android.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import static com.driverapp.android.DriverApp.app;

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


    public static Bitmap circle(Bitmap bitmap) {
        if(bitmap==null){
            return null;
        }

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
        return circleBitmap;
    }

    public static Bitmap circle(int resId){
        return circle(BitmapFactory.decodeResource(app().getResources(),
                resId));
    }

    public static Bitmap circle(Drawable drawable){
        return circle(fromDrawable(drawable));
    }

    public static Bitmap fromDrawable(Drawable drawable){
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}
