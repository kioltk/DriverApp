package com.driverapp.android.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;

public class ProfileActivity extends BaseActivity {

    private UpdateUserPictureTask updateUserPictureTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setBackButtonEnabled();

        View imageView = findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.login_google);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*ImageProcessor.onActivityResult(requestCode, resultCode, data);
        updateUserPictureTask = new UpdateUserPictureTask() {
            @Override
            protected void onSuccess(UpdateUserPictureTaskResult result) {

            }

            @Override
            protected void onError(Exception exp) {

            }
        };*/
    }
}
