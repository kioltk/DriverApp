package com.driverapp.android.events.create;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.Core;
import com.driverapp.android.models.EventCategory;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;


public class CreateActivity extends ActionBarActivity {

    private static final int PICK_IMAGE = 100;
    private static final int PICK_CATEGORY = 101;
    private static final int PICK_MAP = 102;
    private ImageView imageView;
    private EventCategory selectedCategory;
    private View pickCategoryView;
    private TextView pickCategoryTextView;
    private ImageView pickCategoryImageView;
    private View pickLocationView;
    private TextView pickLocationTextView;
    private double selectedLongitude;
    private double
            selectedLatitude;
    private File selectedPhoto;
    private String selectedStreet;
    private String selectedCity;
    private EditText bodyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        bodyView = (EditText) findViewById(R.id.text);

        pickCategoryView =  findViewById(R.id.pick_category);
        pickCategoryTextView = (TextView) findViewById(R.id.pick_category_text);
        pickCategoryImageView = (ImageView) findViewById(R.id.pick_category_image);

        pickCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), CategoryActivity.class), PICK_CATEGORY);
            }
        });

        pickLocationView = findViewById(R.id.pick_map);
        pickLocationTextView = (TextView) findViewById(R.id.pick_map_text);

        pickLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(),LocationPickerActivity.class), PICK_MAP);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();

                    //User had pick an image.
                    Cursor cursor = getContentResolver().query(imageUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    cursor.moveToFirst();

                    //Link to the image
                    final String imageFilePath = cursor.getString(0);
                    selectedPhoto = new File(imageFilePath);
                    ImageLoader.getInstance().displayImage(imageUri.toString(), imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    // todo cropper?
                    cursor.close();
                }
                break;
            case PICK_CATEGORY: {
                if (resultCode == RESULT_OK) {
                    int categoryId = data.getExtras().getInt(CategoryActivity.EXTRA_ID);
                    for (EventCategory category : Core.getCategories()) {
                        if (category.id == categoryId) {
                            selectedCategory = category;
                            pickCategoryTextView.setText(selectedCategory.name);
                            pickCategoryImageView.setImageResource(selectedCategory.imgResId);
                            break;
                        }
                    }
                }
            }
            break;
            case PICK_MAP: {
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    selectedStreet = bundle.getString(LocationPickerActivity.EXTRA_STREET);
                    selectedCity = bundle.getString(LocationPickerActivity.EXTRA_CITY);
                    selectedLongitude = bundle.getDouble(LocationPickerActivity.EXTRA_LONGITUDE);
                    selectedLatitude = bundle.getDouble(LocationPickerActivity.EXTRA_LAT);
                    pickLocationTextView.setText(selectedStreet);
                }
            }
            break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.create:
                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Отправка")
                        .setMessage("Терпение")
                        .setCancelable(false)
                        .show();
                String body = bodyView.getText().toString();
                CreateEventTask createEventTask = new CreateEventTask(
                        body, selectedCategory.id,
                        selectedLongitude,selectedLatitude,
                        selectedCity, selectedStreet,
                        selectedPhoto
                        ) {

                    @Override
                    protected void onSuccess(CreateEventResult result) {
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    protected void onError(Exception exp) {
                        dialog.dismiss();
                        Toast.makeText(getBaseContext(), "Ошибка создания", Toast.LENGTH_SHORT).show();
                    }
                };
                createEventTask.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getActivityIntent(Context baseContext) {
        return new Intent(baseContext, CreateActivity.class);
    }
}
