package com.driverapp.android.profile;

import com.driverapp.android.core.api.ApiTask;
import com.google.gson.Gson;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class UpdateUserPictureTask extends ApiTask<UpdateUserPictureTaskResult> {
    public UpdateUserPictureTask(File userPictureFile) {
        super("profile/update_picture", null, new BasicNameValuePair("pic", userPictureFile.getAbsolutePath()));
    }

    @Override
    protected UpdateUserPictureTaskResult parse(String json) {
        return new Gson().fromJson(json, UpdateUserPictureTaskResult.class);
    }
}
class UpdateUserPictureTaskResult{
    public String photo;
    public int result;
}