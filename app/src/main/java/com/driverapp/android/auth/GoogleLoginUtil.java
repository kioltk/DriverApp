package com.driverapp.android.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;

/**
 * Created by Jesus Christ. Amen.
 */
public class GoogleLoginUtil implements ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {
    private String TAG = "GooglePlusLoginUtils";
    /* Request code used to invoke sign in user interactions. */
    public static final int REQUEST_CODE_LOGIN = 414215;
    private static final int REQUEST_CODE_AUTH_TOKEN = 10;

    private static final int PROFILE_PIC_SIZE = 400;
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHOTO = "photo";
    public static final String PROFILE = "profile";

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    public boolean mSignInClicked;
    private ConnectionResult mConnectionResult;

    private View btnSignIn;
    private Activity activity;
    private GoogleLoginListener loginListener;
    public String loginedPersonName;
    public String loginedPersonSurname;
    public String loginedPersonPhotoUrl;
    public String loginedPersonGooglePlusProfile;
    public String loginedEmail;
    public String loginedAccessToken;

    private boolean authing = false;

    public boolean isAvailable() {
        boolean available = false;
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        switch (status) {
            case ConnectionResult.SERVICE_DISABLED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_MISSING:
                return false;
            case ConnectionResult.SERVICE_UPDATING:
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            default:
                return true;
        }
    }


    public interface GoogleLoginListener {
        public void googleAuthorized();
    }

    public GoogleLoginUtil(Activity activity, GoogleLoginListener loginListener, int btnRes) {
        Log.i(TAG, "Create");
        setLoginStatus(loginListener);
        this.activity = activity;
        this.btnSignIn =  activity.findViewById(btnRes);
        btnSignIn.setOnClickListener(this);
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE).build();

    }

    public void setLoginStatus(final GoogleLoginListener loginListener) {

        this.loginListener = new GoogleLoginListener() {
            @Override
            public void googleAuthorized() {
                if(loginListener!=null){
                    loginListener.googleAuthorized();
                }
            }
        };

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed");
        Log.i(TAG, "Error Code " + result.getErrorCode());
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), (Activity) activity, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    public void setSignInClicked(boolean value) {
        mSignInClicked = value;
    }

    public void setIntentInProgress(boolean value) {
        mIntentInProgress = value;
    }

    public void onStart() {
        if (isAvailable()) {
            mGoogleApiClient.connect();
        }
    }

    public void reconnect() {
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    public void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void signInWithGplus() {
        Log.i(TAG, "signInWithGplus");
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    private void resolveSignInError() {
        Log.i(TAG, "resolveSignInError");
        if (mConnectionResult!=null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult((Activity) activity, REQUEST_CODE_LOGIN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        Log.i(TAG, "onConnected");
        Toast.makeText(activity, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.i(TAG, "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    private void getProfileInformation() {
        Log.i(TAG, "getProfileInformation");
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                loginedPersonName = currentPerson.getName().getGivenName();
                loginedPersonSurname = currentPerson.getName().getFamilyName();
                loginedPersonPhotoUrl = currentPerson.getImage().getUrl();
                loginedPersonGooglePlusProfile = currentPerson.getUrl();
                loginedEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + loginedPersonName + ", plusProfile: "
                        + loginedPersonGooglePlusProfile + ", email: " + loginedEmail
                        + ", Image: " + loginedPersonPhotoUrl);


                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                loginedPersonPhotoUrl = loginedPersonPhotoUrl.substring(0,
                        loginedPersonPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                getAccess();

                //   new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(activity,
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        signInWithGplus();
    }

    void googleAuth() {
        if(authing || !mSignInClicked)
            return;
        authing = true;
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String scopes = "oauth2:server:client_id:910124595768-igk02c58ocgsn8s6vb0vf9i7pe32bdrf.apps.googleusercontent.com:api_scope:" + Scopes.PLUS_LOGIN;
                String code = null;
                String accountName = loginedEmail;
                Exception exp;
                try {
                    code = GoogleAuthUtil.getToken(
                            activity,                                              // Context activity
                            accountName,  // String accountName
                            scopes                                            // String scope
                    );
                    Log.d("code", code);
                    return code;
                } catch (IOException transientEx) {
                    // network or server error, the call is expected to succeed if you try again later.
                    // Don't attempt to call again immediately - the request is likely to
                    // fail, you'll hit quotas or back-off.
                    exp = transientEx;
                } catch (UserRecoverableAuthException e) {
                    // Requesting an authorization code will always throw
                    // UserRecoverableAuthException on the first call to GoogleAuthUtil.getToken
                    // because the user must consent to offline access to their data.  After
                    // consent is granted control is returned to your activity in onActivityResult
                    // and the second call to GoogleAuthUtil.getToken will succeed.
                    activity.startActivityForResult(e.getIntent(), REQUEST_CODE_AUTH_TOKEN);
                    exp = e;
                } catch (GoogleAuthException authEx) {
                    // Failure. The call is not expected to ever succeed so it should not be
                    // retried.
                    exp = authEx;
                } catch (Exception e) {
                    exp = e;
                }
                exp.printStackTrace();
                return null;
            }

            @Override
            protected void onPostExecute(String code) {
                authing = false;
                mSignInClicked = false;
                if(code!=null && !code.isEmpty()){
                    loginedAccessToken = code;
                     loginListener.googleAuthorized();
                }

            }
        }.execute();
    }
    public void getAccess() {
        if (authing) {
            return;
        }
        googleAuth();
    }

    public boolean onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (responseCode != ((Activity) activity).RESULT_OK) {
                setSignInClicked(false);
            }
            setIntentInProgress(false);
            reconnect();
            return true;
        } else{
            if (requestCode == REQUEST_CODE_AUTH_TOKEN) {
                loginedAccessToken = intent.getExtras().getString("authToken");
                loginListener.googleAuthorized();
            }
        }
        return false;
    }


}
