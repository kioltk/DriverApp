package com.driverapp.android.feed;

import com.driverapp.android.core.api.ApiTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d_great on 25.12.14.
 */
public abstract class FeedTask  extends ApiTask<FeedItem>{
    public FeedTask(final int id) {
        super("get_event", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",""+id));
        }}, false);
    }

    @Override
    protected FeedItem parse(String json) {
        return new Gson().fromJson(json, FeedItem.class);
    }
}
