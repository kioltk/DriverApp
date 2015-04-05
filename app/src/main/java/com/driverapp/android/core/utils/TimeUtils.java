package com.driverapp.android.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jesus Christ. Amen.
 */
public class TimeUtils {
    public static String getTime(int date) {
        // todo improve time formatting?

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(date*1000L);
        return SimpleDateFormat.getDateInstance().format(cal.getTime());
    }
}
