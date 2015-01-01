package com.driverapp.android.events.feed;

import com.driverapp.android.core.api.ApiTask;
import com.driverapp.android.models.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class EventListTask extends ApiTask<ArrayList<Event>> {
    public EventListTask() {
        super("get_feed_events", new ArrayList<NameValuePair>(), false);
    }
    protected ArrayList<Event> parse(String json) {

        return new Gson().fromJson(json, new TypeToken<ArrayList<Event>>() {
        }.getType());
    }
}
