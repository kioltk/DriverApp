package com.driverapp.android.events.create;

import com.driverapp.android.core.api.ApiTask;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class CreateEventTask extends ApiTask<CreateEventResult> {

    public CreateEventTask(final String body, final long categoryId, final double longitude, final double latitude, final String city, final String street, File photoFile) {
        super("create_event", new ArrayList<NameValuePair>() {{

            add(new BasicNameValuePair("lon", String.valueOf(longitude)));
            add(new BasicNameValuePair("lat", String.valueOf(latitude)));
            add(new BasicNameValuePair("city", String.valueOf(city)));
            add(new BasicNameValuePair("address", String.valueOf(street)));
            add(new BasicNameValuePair("desc", String.valueOf(body)));
            add(new BasicNameValuePair("category_id", String.valueOf(categoryId)));

        }}, photoFile==null?null:new BasicNameValuePair("photo", photoFile.getAbsolutePath()));

    }

    @Override
    protected CreateEventResult parse(String json) {
        return new Gson().fromJson(json, CreateEventResult.class);
    }
}