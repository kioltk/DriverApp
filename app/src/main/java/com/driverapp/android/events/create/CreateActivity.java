package com.driverapp.android.events.create;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.core.Core;
import com.driverapp.android.events.create.cropping.CropActivity;
import com.driverapp.android.models.EventCategory;

import java.io.File;
import java.io.FileOutputStream;


public class CreateActivity extends BaseActivity {

    private static final int PICK_IMAGE = 100;
    private static final int PICK_CATEGORY = 101;
    private static final int PICK_MAP = 102;
    private static final int PICK_PHOTO = 103;
    private static final int CROP_PICTURE = 104;
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
    private String selectedStreet;
    private String selectedCity;
    private EditText bodyView;
    private Bitmap selectedImageBitmap;
    private File selectedImageFile;

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
                startActivityForResult(new Intent(getBaseContext(), CategoryPickerActivity.class), PICK_CATEGORY);
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
            public void onClick(View v) {

                new AlertDialog.Builder(CreateActivity.this)
                        .setTitle(R.string.create_pick_image_title)
                        .setItems(new String[]{getString(R.string.create_pick_image_gallery), getString(R.string.create_pick_image_photo)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent getImageIntent = new Intent();
                                    getImageIntent.setType("image/*");
                                    getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                                    if (getImageIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(Intent.createChooser(getImageIntent, "Select Picture"), PICK_IMAGE);
                                    }
                                } else {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            startActivityForResult(takePictureIntent, PICK_PHOTO);
                                        }

                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            switch (requestCode) {
                case CROP_PICTURE:
                    if(resultCode == RESULT_OK) {
                        String path = data.getStringExtra(CropActivity.IMAGE_PATH_RESULT);

                        if (path == null) {
                            return;
                        }
                        selectedImageFile = new File(path);
                        selectedImageBitmap = BitmapFactory.decodeFile(path);
                        imageView.setImageBitmap(selectedImageBitmap);
                    }
                    break;
                case PICK_IMAGE:
                    if (resultCode == RESULT_OK) {
                        Uri imageUri = data.getData();

                        //User had pick an image.
                        Cursor cursor = getContentResolver().query(imageUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        cursor.moveToFirst();

                        final String imageFilePath = cursor.getString(0);

                        cursor.close();

                        crop(imageFilePath);
                    }
                    break;
                case PICK_PHOTO: {

                    // todo cropper?
                    if(resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap capturedPhotoBitmap = (Bitmap) extras.get("data");
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        File capturedPhotoFile = new  File(getCacheDir(),
                                "temp"
                                );
                        if(capturedPhotoFile.exists()){
                            capturedPhotoFile.delete();
                            capturedPhotoFile.createNewFile();
                        }
                        //selectedImageFile.mkdirs();

                        FileOutputStream fOut = new FileOutputStream(capturedPhotoFile);

                        capturedPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        crop(capturedPhotoFile.getPath());
                    }
                }
                break;
                case PICK_CATEGORY: {
                    if (resultCode == RESULT_OK) {
                        int categoryId = data.getExtras().getInt(CategoryPickerActivity.EXTRA_ID);
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
                        if(selectedStreet==null || selectedStreet.equals("")){
                            pickLocationTextView.setText(R.string.create_pick_map_unknown);
                        } else {
                            pickLocationTextView.setText(selectedStreet);
                        }
                    }
                }
                break;
            }

        }catch (Exception exp){
            bodyView.setText(exp.getLocalizedMessage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void crop(String imageFilePath) {
        Intent intent = new Intent(this, CropActivity.class);

        String filePath = imageFilePath;
        intent.putExtra(CropActivity.IMAGE_PATH, filePath);
        intent.putExtra(CropActivity.IMAGE_PATH_RESULT, getCacheDir()+"/pic"+System.currentTimeMillis()+".jpg");

        startActivityForResult(intent, CROP_PICTURE);

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
                if(selectedCategory==null){
                    Toast.makeText(this, "Сначала выберите категорию", Toast.LENGTH_SHORT).show();
                    return true;
                }
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
                        selectedImageFile
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
