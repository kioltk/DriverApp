package com.driverapp.android.events.comments;

import com.driverapp.android.core.api.ApiTask;
import com.driverapp.android.core.utils.UserUtil;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class AddCommentTask extends ApiTask<AddCommentResult> {
    public AddCommentTask(final int eventId, final String commentBody) {
        super("add_comment", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("event_id", String.valueOf(eventId)));
            add(new BasicNameValuePair("event_id", String.valueOf(UserUtil.getId())));
            add(new BasicNameValuePair("comment", commentBody));
        }}, true);

    }

    @Override
    protected AddCommentResult parse(String json) {
        return new Gson().fromJson(json, AddCommentResult.class);

    }
}
class AddCommentResult {
    public int result;
    public int id;
}