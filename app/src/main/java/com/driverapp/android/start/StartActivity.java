package com.driverapp.android.start;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.driverapp.android.auth.AuthActivty;
import com.driverapp.android.auth.AuthUtil;
import com.driverapp.android.auth.GooglePlusLoginUtils;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.File;
import java.io.IOException;


public class StartActivity extends ActionBarActivity implements GooglePlusLoginUtils.GPlusLoginStatus {

    private static final int AUTH_CODE_REQUEST_CODE = 10;
    private static final String TAG = "StartActivity";
    private GooglePlusLoginUtils gLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        AuthUtil.printSignature(this);
        AuthUtil.getCertificateFingerprint(this);

        SharedPreferences prefs = getSharedPreferences("start", MODE_MULTI_PROCESS);
        boolean firstStart = true;// prefs.getBoolean("first", true);
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
        /*findViewById(R.id.login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gLogin.connect();
                *//*startActivity(new Intent(StartActivity.this, AuthActivty.class));
                if (!mGoogleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    mGoogleApiClient.connect();
                }*//*
            }

        });*/
        gLogin = new GooglePlusLoginUtils(this, R.id.login_google);
        gLogin.setLoginStatus(this);


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

    void auth(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String scopes = "oauth2:server:client_id:910124595768-igk02c58ocgsn8s6vb0vf9i7pe32bdrf.apps.googleusercontent.com:api_scope:" + Scopes.PLUS_LOGIN;
                String code = null;
                String accountName = gLogin.loginedEmail;
                Exception exp;
                try {
                    code = GoogleAuthUtil.getToken(
                            StartActivity.this,                                              // Context context
                            accountName,  // String accountName
                            scopes                                            // String scope
                    );
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
                    startActivityForResult(e.getIntent(), AUTH_CODE_REQUEST_CODE);
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

                Toast.makeText(getBaseContext(), code, Toast.LENGTH_SHORT).show();
            }
        }.execute();

    }
    @Override
    protected void onStart() {
        super.onStart();
        gLogin.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        gLogin.disconnect();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        gLogin.onActivityResult(requestCode, responseCode, intent);

    }

    @Override
    public void OnSuccessGPlusLogin() {
        auth();
    }
}