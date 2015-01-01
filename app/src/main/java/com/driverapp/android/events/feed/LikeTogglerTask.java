package com.driverapp.android.events.feed;

import com.driverapp.android.core.api.ApiTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class LikeTogglerTask extends ApiTask<LikeToggleResult> {
    public LikeTogglerTask(final int eventId) {
        super("toggle_like",new ArrayList<NameValuePair>(){{ add(new BasicNameValuePair("event_id", String.valueOf(eventId))); }},true);
    }
}
