package com.driverapp.android.start;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.driverapp.android.MainActivity;
import com.driverapp.android.R;
import com.driverapp.android.auth.AuthUtil;
import com.driverapp.android.auth.FacebookLoginUtil;
import com.driverapp.android.auth.GoogleLoginUtil;
import com.driverapp.android.core.utils.UserUtil;

import java.io.File;
import java.io.IOException;


public class StartActivity extends ActionBarActivity implements GoogleLoginUtil.GoogleLoginListener, FacebookLoginUtil.FacebookLoginListener {

    private static final String TAG = "StartActivity";
    private GoogleLoginUtil googleLoginUtil;
    private FacebookLoginUtil facebookLoginUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        AuthUtil.printSignature(this);
        AuthUtil.getCertificateFingerprint(this);

        SharedPreferences prefs = getSharedPreferences("start", MODE_MULTI_PROCESS);
        boolean firstStart = prefs.getBoolean("first", true);
        if (false) {
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
        /*findViewById(R.id.login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleLoginUtil.onStart();
                *//*startActivity(new Intent(StartActivity.this, AuthActivty.class));
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    mGoogleApiClient.onStart();
                }*//*
            }

        });*/

        // google
        googleLoginUtil = new GoogleLoginUtil(this, this, R.id.login_google);

        // facebook
        facebookLoginUtil = new FacebookLoginUtil(this, this, R.id.login_facebook);


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

    private void startMain() {

        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }



    private void register(String token, final String firstName, final String lastName, String link, String email, String source, final String photoUrl) {
        new RegisterTask(firstName, lastName, "android", link, email, source, token, photoUrl) {
            @Override
            protected void onSuccess(RegisterResult result) {
                UserUtil.setUserId(result.user_id);
                UserUtil.setUserName(lastName + " " + firstName);
                UserUtil.setUserPhoto(photoUrl);
                startMain();
            }

            @Override
            protected void onError(Exception exp) {
                Log.e("Registration error","", exp);
            }
        }.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleLoginUtil.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleLoginUtil.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,Intent intent) {
        if (!googleLoginUtil.onActivityResult(requestCode, responseCode, intent) &&
                !facebookLoginUtil.onActivityResult(requestCode, responseCode, intent)) {

        }
    }

    @Override
    public void facebookAuthorized() {
        register(facebookLoginUtil.token, facebookLoginUtil.firstName, facebookLoginUtil.lastName, facebookLoginUtil.link,facebookLoginUtil.email,"facebook", facebookLoginUtil.photoUrl);
    }

    @Override
    public void googleAuthorized() {
        register(googleLoginUtil.loginedAccessToken, googleLoginUtil.loginedPersonName, null, googleLoginUtil.loginedPersonGooglePlusProfile, googleLoginUtil.loginedEmail,"google", googleLoginUtil.loginedPersonPhotoUrl);

    }
}