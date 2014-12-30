package com.driverapp.android.create;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.driverapp.android.R;
import com.driverapp.android.core.Core;
import com.driverapp.android.models.Category;

import java.util.ArrayList;


public class CategoryActivity extends ActionBarActivity {

    public static final java.lang.String EXTRA_ID = "extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ListView list = (ListView) findViewById(R.id.list);
        ArrayList<Category> categories = Core.getCategories();
        list.setAdapter(new CategoryAdapter(this, categories));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_ID, (int) id);
                data.putExtras(bundle);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
