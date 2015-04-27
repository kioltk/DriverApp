package com.driverapp.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by Jesus Christ. Amen.
 */
public class TwitterLoginUtil {
    private static final String TAG = "TwitterLoginUtil";
    private Activity activity;
    private TwitterLoginListener loginListener;
    private int buttonResId;
    private TwitterLoginButton fakeButton;

    public String username;
    public long id;
    public String secret;
    public String token;

    public TwitterLoginUtil(final Activity activity, final TwitterLoginListener loginListener, int buttonResId) {
        this.activity = activity;
        this.loginListener = loginListener;
        this.buttonResId = buttonResId;

        activity.findViewById(buttonResId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeButton = new TwitterLoginButton(activity);
                fakeButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        Log.d("TwitterAuthUtil", twitterSessionResult.toString());
                        if (loginListener != null) {
                            username = twitterSessionResult.data.getUserName();
                            id = twitterSessionResult.data.getUserId();
                            secret = twitterSessionResult.data.getAuthToken().secret;
                            token = twitterSessionResult.data.getAuthToken().token;
                            loginListener.twitterAuthorized();
                            /*TwitterAuthClient authClient = new TwitterAuthClient();
                            authClient.requestEmail(twitterSessionResult.data, new Callback() {
                                @Override
                                public void success(Result result) {
                                    Log.d(TAG, result.data.toString());
                                }

                                @Override
                                public void failure(TwitterException exception) {
                                    Log.d(TAG, "auth error: " + exception);
                                }
                            });*/
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e("TwitterAuthUtil", "Auth error", e);
                    }
                });
                fakeButton.performClick();
            }
        });
    }

    public void setLoginListener(TwitterLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public boolean onActivityResult(int requestCode, int responseCode, Intent intent) {
        if(fakeButton==null){
            return false;
        }
        fakeButton.onActivityResult(requestCode, responseCode, intent);
        return requestCode == 140;
    }

    public interface TwitterLoginListener {
        void twitterAuthorized();
    }
}
