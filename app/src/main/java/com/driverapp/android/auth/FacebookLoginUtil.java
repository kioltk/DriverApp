package com.driverapp.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.driverapp.android.DriverApp.app;

/**
 * Created by Jesus Christ. Amen.
 */
public class FacebookLoginUtil {


    public String firstName;
    public String lastName;
    public String email;
    public String link;
    public String token;
    public String photoUrl;

    private final CallbackManager manager;
    private FacebookLoginListener facebookLoginListener;

    public FacebookLoginUtil(final Activity activity, FacebookLoginListener facebookLoginListener, int buttonResId){
        FacebookSdk.sdkInitialize(app());
        this.facebookLoginListener = facebookLoginListener;


        View facebookLoginButton = activity.findViewById(buttonResId);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_friends"));
            }
        });

        // Callback registration
        manager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login", "success" + loginResult.toString());
                auth(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("Facebook Login", "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Facebook Login", "error", e);
            }
        });

    }

    public void setFacebookLoginListener(FacebookLoginListener facebookLoginListener) {
        this.facebookLoginListener = facebookLoginListener;
    }

    private void auth(final LoginResult loginResult) {
        GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                Log.d("Facebook Login", jsonObject.toString());
                try {
                    firstName = jsonObject.getString("first_name");
                    lastName = jsonObject.getString("last_name");
                    email = jsonObject.getString("email");
                    link = jsonObject.getString("link");
                    photoUrl = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                    token = loginResult.getAccessToken().getToken();
                    if(facebookLoginListener !=null)
                        facebookLoginListener.facebookAuthorized();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        request.getParameters().putString("fields","email,first_name,last_name,link,picture");
        new GraphRequestAsyncTask(request).execute();
    }


    public boolean onActivityResult(int requestCode, int responseCode, Intent intent) {
        return manager.onActivityResult(requestCode, responseCode, intent);
    }

    /**
     * Created by Jesus Christ. Amen.
     */
    public static interface FacebookLoginListener {
        void facebookAuthorized();
    }
}
