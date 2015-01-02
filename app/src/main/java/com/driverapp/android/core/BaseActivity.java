package com.driverapp.android.core;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.driverapp.android.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class BaseActivity extends ActionBarActivity {


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }

    }
}
