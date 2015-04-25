package com.driverapp.android.auth;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.driverapp.android.R;

public class AuthActivty extends ActionBarActivity{

    private static final String TAG = "auth_activity";
    private GoogleLoginUtil gLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_actiivty);




    }


}
