package com.driverapp.android.events.comments;

import com.driverapp.android.core.api.ApiTask;
import com.driverapp.android.models.EventComment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class EventCommentsTask extends ApiTask<ArrayList<EventComment>> {
    public EventCommentsTask(final int eventId) {
        super("get_comments", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("event_id", String.valueOf(eventId)));
        }}, false);
    }

    @Override
    protected ArrayList<EventComment> parse(String json) {
        return new Gson().fromJson(json, new TypeToken<ArrayList<EventComment>>() {
        }.getType());
    }
}
