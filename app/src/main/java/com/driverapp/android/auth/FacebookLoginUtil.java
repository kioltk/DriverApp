package com.driverapp.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.driverapp.android.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import static com.driverapp.android.DriverApp.app;

/**
 * Created by Jesus Christ. Amen.
 */
public class FacebookLoginUtil {


    private final CallbackManager manager;

    public FacebookLoginUtil(final Activity activity){
        FacebookSdk.sdkInitialize(app());


        View facebookLoginButton = activity.findViewById(R.id.login_facebook);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends"));
            }
        });

        // Callback registration
        manager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(manager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login", "success" + loginResult.toString());
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

    public boolean onActivityResult(int requestCode, int responseCode, Intent intent) {
        return manager.onActivityResult(requestCode, responseCode, intent);
    }
}
