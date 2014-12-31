package com.driverapp.android.feed;

import com.driverapp.android.core.api.ApiTask;
import com.driverapp.android.models.Event;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by d_great on 25.12.14.
 */
public abstract class EventTask extends ApiTask<Event>{
    public EventTask(final int id) {
        super("get_event", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",""+id));
        }}, false);
    }

    @Override
    protected Event parse(String json) {
        return new Gson().fromJson(json, Event.class);
    }
}
