package com.driverapp.android.events;

import com.driverapp.android.DriverApp;
import com.driverapp.android.core.api.ApiTask;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class LikeTogglerTask extends ApiTask<LikeToggleResult> {
    public LikeTogglerTask(final int eventId) {
        super("toggle_like",new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("user_id", DriverApp.getUserId()));
            add(new BasicNameValuePair("event_id", String.valueOf(eventId)));
        }}, true);
    }

    @Override
    protected LikeToggleResult parse(String json) {
        return new Gson().fromJson(json, LikeToggleResult.class);
    }
}
