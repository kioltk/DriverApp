package com.driverapp.android.feed;

import com.driverapp.android.core.api.ApiTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class FeedListTask extends ApiTask<ArrayList<FeedItem>> {
    public FeedListTask() {
        super("get_feed_events", new ArrayList<NameValuePair>(), false);
    }
    protected ArrayList<FeedItem> parse(String json) {

        return new Gson().fromJson(json, new TypeToken<ArrayList<FeedItem>>() {
        }.getType());
    }
}
