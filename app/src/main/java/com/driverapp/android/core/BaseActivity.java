package com.driverapp.android.core;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.driverapp.android.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class BaseActivity extends ActionBarActivity {


    protected Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
        }

    }

    public void setBackButtonEnabled(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
