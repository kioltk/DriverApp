package com.driverapp.android.events.create.cropping;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.driverapp.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CropActivity extends ActionBarActivity {

    public static final String IMAGE_PATH = "path";
    public static final String IMAGE_PATH_RESULT = "path_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);

        final PhotoView photoView = (PhotoView) findViewById(R.id.crop_holder);
        View cropButton = findViewById(R.id.crop);
        View cancelButton = findViewById(R.id.cancel);

        final View frame = findViewById(R.id.cropper_frame);

        frame.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams frameParams = frame.getLayoutParams();
                frameParams.height = frame.getWidth();
                frame.setLayoutParams(frameParams);


                ViewGroup.LayoutParams cropParams = photoView.getLayoutParams();
                cropParams.height = photoView.getWidth();
                photoView.setLayoutParams(cropParams);

            }
        }, 1);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String path = extras.getString(IMAGE_PATH);
        final String pathResult = extras.getString(IMAGE_PATH_RESULT);

        ImageLoader.getInstance().displayImage("file:/"+path, photoView);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmapResult = photoView.getVisibleRectangleBitmap();
                try {
                    File outputFile = new File(pathResult);
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    outputFile.createNewFile();

                    FileOutputStream fOut = new FileOutputStream(outputFile);

                    bitmapResult.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();


                    Intent data = new Intent();
                    Bundle extras = new Bundle();
                    extras.putString(IMAGE_PATH_RESULT, pathResult);
                    data.putExtras(extras);
                    setResult(RESULT_OK, data);
                    finish();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
