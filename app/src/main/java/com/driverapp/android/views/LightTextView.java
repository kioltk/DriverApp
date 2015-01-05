package com.driverapp.android.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jesus Christ. Amen.
 */
public class LightTextView extends TextView {
    public LightTextView(Context context) {
        super(context);
        init();
    }

    public LightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LightTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tp = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoLight.ttf");
            setTypeface(tp);
        }
    }
}
