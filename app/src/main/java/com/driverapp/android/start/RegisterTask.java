package com.driverapp.android.start;

import com.driverapp.android.core.api.ApiTask;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class RegisterTask extends ApiTask<RegisterResult> {


    public RegisterTask(final String name, final String surname, final String source, final String profile, final String mail, final String authType, final String authToken, File imageFile) {
        super("create_user", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("name", name));
            add(new BasicNameValuePair("surname", surname));
            add(new BasicNameValuePair("source", source));
            add(new BasicNameValuePair("profile", profile));
            add(new BasicNameValuePair("mail", mail));
            add(new BasicNameValuePair("authType", authType));
            add(new BasicNameValuePair("authToken", authToken));
        }}, new BasicNameValuePair("avatar", imageFile.getAbsolutePath()));

    }

    public RegisterTask(final String givenName, final String surname, final String source, final String profile, final String email, final String authType, final String authToken, String imageUrl) {
        super("create_user", new ArrayList<NameValuePair>() {{
            add(new BasicNameValuePair("name", givenName));
            add(new BasicNameValuePair("surname", surname));
            add(new BasicNameValuePair("source", source));
            add(new BasicNameValuePair("profile", profile));
            add(new BasicNameValuePair("mail", email));
            add(new BasicNameValuePair("authType", authType));
            add(new BasicNameValuePair("authToken", authToken));
            add(new BasicNameValuePair("imageUrl", authToken));
        }}, true);;
    }

    @Override
    protected RegisterResult parse(String json) {
        return new Gson().fromJson(json, RegisterResult.class);
    }
}
class RegisterResult {

}