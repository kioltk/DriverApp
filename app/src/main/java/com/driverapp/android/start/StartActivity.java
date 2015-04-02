package com.driverapp.android.start;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.driverapp.android.MainActivity;
import com.driverapp.android.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.File;
import java.io.IOException;


public class StartActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_VK_AUTH = 1;
    private static final int REQUEST_GOOGLE_AUTH = 5;
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE);

        mGoogleApiClient = builder.build();
        //builder.addApi(new Api<Api.ApiOptions.NotRequiredOptions>(new))
        //        .build();

        SharedPreferences prefs = getSharedPreferences("start", MODE_MULTI_PROCESS);
        boolean firstStart = prefs.getBoolean("first", true);
        if (!firstStart) {
            LinearLayout container = (LinearLayout) findViewById(R.id.logins_holder);
            container.setGravity(Gravity.CENTER);
            /*findViewById(R.id.login_fb).setVisibility(View.GONE);
            findViewById(R.id.login_vk).setVisibility(View.GONE);
            findViewById(R.id.login_tw).setVisibility(View.GONE);*/

            findViewById(R.id.login_google).setVisibility(View.GONE);
            findViewById(R.id.login_force).setVisibility(View.GONE);
            findViewById(R.id.skip).setVisibility(View.GONE);
            container.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMain();
                }
            }, 1000);
        }
        prefs.edit().putBoolean("first", false).apply();
        findViewById(R.id.login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                }
            }
        });


        findViewById(R.id.login_force).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View loginView = View.inflate(getBaseContext(), R.layout.dialog_login_force, null);
                new AlertDialog.Builder(StartActivity.this).setView(loginView)
                        .setTitle("Secret login")
                        .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                EditText nameView = (EditText) loginView.findViewById(R.id.user_name);
                                EditText surnameView = (EditText) loginView.findViewById(R.id.surname);
                                EditText sourceView = (EditText) loginView.findViewById(R.id.source);
                                EditText profileView = (EditText) loginView.findViewById(R.id.profile);
                                EditText mainView = (EditText) loginView.findViewById(R.id.mail);
                                EditText authTypeView = (EditText) loginView.findViewById(R.id.auth_type);
                                EditText authTokenView = (EditText) loginView.findViewById(R.id.auth_token);

                                File imageFile = new File(getCacheDir(), "tempfile.png");
                                try {
                                    imageFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                RegisterTask registerTask = new RegisterTask(
                                        nameView.getText().toString(),
                                        surnameView.getText().toString(),
                                        sourceView.getText().toString(),
                                        profileView.getText().toString(),
                                        mainView.getText().toString(),
                                        authTypeView.getText().toString(),
                                        authTokenView.getText().toString(),
                                        imageFile
                                ) {
                                    @Override
                                    protected void onSuccess(RegisterResult result) {
                                        //progressDialog.dismiss();
                                        Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    protected void onError(Exception exp) {
                                        Toast.makeText(getBaseContext(), exp.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                };
                                registerTask.start();
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });


        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void startMain() {

        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // VKUIHelper.onActivityResult(this,requestCode,resultCode,data);
        if (requestCode == REQUEST_GOOGLE_AUTH) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    /* Track whether the sign-in button has been clicked so that we know to resolve
 * all issues preventing sign-in without waiting.
 */
    private boolean mSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if ( mConnectionResult!=null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        REQUEST_GOOGLE_AUTH, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
        if (!mIntentInProgress){
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(bundle!=null)
            Toast.makeText(this, bundle.toString(), Toast.LENGTH_SHORT).show();

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            Person.Image personPhoto = currentPerson.getImage();
            String personGooglePlusProfile = currentPerson.getUrl();
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            new RegisterTask(currentPerson.getName().getGivenName(), currentPerson.getName().getGivenName(), "android", currentPerson.getUrl(),
                    email, "gplus", "null", personPhoto.getUrl()) {
                @Override
                protected void onSuccess(RegisterResult result) {

                }

                @Override
                protected void onError(Exception exp) {

                }
            }.start();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "" + i, Toast.LENGTH_SHORT).show();
    }
}