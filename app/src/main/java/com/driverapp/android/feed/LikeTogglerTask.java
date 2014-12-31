package com.driverapp.android.feed;

import com.driverapp.android.core.api.ApiTask;

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class LikeTogglerTask extends ApiTask<LikeToggleResult> {
    public LikeTogglerTask(int eventId) {
        super("toggle_like",new ArrayList<NameValuePair>(),true);
    }
}
