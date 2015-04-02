package com.driverapp.android.events.create;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.driverapp.android.events.EventActivity;
import com.driverapp.android.events.create.cropping.CropActivity;
import com.driverapp.android.models.EventCategory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CreateActivity extends BaseActivity {

    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_NEW_API = 105;
    private static final int PICK_CATEGORY = 101;
    private static final int PICK_MAP = 102;
    private static final int PICK_PHOTO = 103;
    private static final int CROP_PICTURE = 104;
    private TextView imagePickView;
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
    private ImageView imagePickBackgroundView;
    private View createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        bodyView = (EditText) findViewById(R.id.text);

        pickCategoryView = findViewById(R.id.pick_category);
        pickCategoryTextView = (TextView) findViewById(R.id.pick_category_text);
        pickCategoryImageView = (ImageView) findViewById(R.id.pick_category_image);
        createButton = findViewById(R.id.create);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create();
            }
        });

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
                startActivityForResult(new Intent(getBaseContext(), LocationPickerActivity.class), PICK_MAP);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagePickView = (TextView) findViewById(R.id.image_pick);
        imagePickBackgroundView = (ImageView) findViewById(R.id.image_pick_background);
        imagePickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(CreateActivity.this)
                        .setTitle(R.string.create_pick_image_title)
                        .setItems(new String[]{getString(R.string.create_pick_image_gallery), getString(R.string.create_pick_image_photo)}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            if (Build.VERSION.SDK_INT < 19) {
                                                Intent getImageIntent = new Intent();
                                                getImageIntent.setType("image/*");
                                                getImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                                                if (getImageIntent.resolveActivity(getPackageManager()) != null) {
                                                    startActivityForResult(Intent.createChooser(getImageIntent, "Select Picture"), PICK_IMAGE);
                                                }
                                            } else {
                                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                                intent.setType("image/jpeg");
                                                startActivityForResult(intent, PICK_IMAGE_NEW_API);
                                            }
                                        } else {
                                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                selectedImageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/DriverApp","picture"+ System.currentTimeMillis()/1000L+".jpg");
                                                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/DriverApp").mkdirs();
                                                try {
                                                    selectedImageFile.createNewFile();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                String uripath = selectedImageFile.getPath();
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://"+uripath));
                                                startActivityForResult(takePictureIntent, PICK_PHOTO);
                                            }

                                        }
                                    }
                                }

                            )
                                    .

                            setCancelable(true)

                            .

                            show();
                        }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            switch (requestCode) {
                case CROP_PICTURE:
                    if (resultCode == RESULT_OK) {
                        String path = data.getStringExtra(CropActivity.IMAGE_PATH_RESULT);

                        if (path == null) {
                            return;
                        }
                        selectedImageFile = new File(path);
                        selectedImageBitmap = BitmapFactory.decodeFile(path);
                        imagePickBackgroundView.setImageBitmap(selectedImageBitmap);
                        imagePickView.setTextColor(0xffffffff);
                    }
                    break;
                case PICK_IMAGE_NEW_API:
                    if(resultCode==RESULT_OK) {
                            Uri originalUri = data.getData();
                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            // Check for the freshest data.
                            getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

                            Bitmap capturedPhotoBitmap = null;

                            capturedPhotoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(originalUri));
                            File capturedPhotoFile = new File(getCacheDir(),
                                    "temp"
                            );
                            if (capturedPhotoFile.exists()) {
                                capturedPhotoFile.delete();
                                capturedPhotoFile.createNewFile();
                            }
                            //selectedImageFile.mkdirs();

                            FileOutputStream fOut = new FileOutputStream(capturedPhotoFile);

                            capturedPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();

                            crop(new File(getCacheDir(),
                                    "temp"
                            ).getPath());

                    }
                    return;
                case PICK_IMAGE:
                    if (resultCode == RESULT_OK) {
                        Uri imageUri = data.getData();

                        //User had pick an image.
                        Cursor cursor = getContentResolver().query(imageUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        cursor.moveToFirst();

                        final String imageFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));

                        cursor.close();

                        crop(imageFilePath);
                    }
                    break;
                case PICK_PHOTO: {

                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            Bundle extras = data.getExtras();
                            Bitmap capturedPhotoBitmap = (Bitmap) extras.get("data");
                            File capturedPhotoFile = new File(getCacheDir(),
                                    "temp"
                            );
                            if (capturedPhotoFile.exists()) {
                                capturedPhotoFile.delete();
                                capturedPhotoFile.createNewFile();
                            }
                            //selectedImageFile.mkdirs();

                            FileOutputStream fOut = new FileOutputStream(capturedPhotoFile);

                            capturedPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                            fOut.flush();
                            fOut.close();
                            crop(capturedPhotoFile.getPath());
                        } else {
                            crop(selectedImageFile.getPath());
                        }
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
                        if (selectedStreet == null || selectedStreet.equals("")) {
                            pickLocationTextView.setText(R.string.create_pick_map_unknown);
                        } else {
                            pickLocationTextView.setText(selectedStreet + ", " + selectedCity);
                        }
                    }
                }
                break;
            }

        } catch (Exception exp) {
            bodyView.setText(exp.getLocalizedMessage());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void crop(String imageFilePath) {
        Intent intent = new Intent(this, CropActivity.class);

        String filePath = imageFilePath;
        intent.putExtra(CropActivity.IMAGE_PATH, filePath);
        intent.putExtra(CropActivity.IMAGE_PATH_RESULT, getCacheDir() + "/pic" + System.currentTimeMillis() + ".jpg");

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
                create();

        }
        return super.onOptionsItemSelected(item);
    }

    private void create() {
        if (selectedCategory == null) {
            Toast.makeText(this, "Сначала выберите категорию", Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.create_sending)
                .setMessage(R.string.create_sending_message)
                .setCancelable(false)
                .show();
        String body = bodyView.getText().toString();

        CreateEventTask createEventTask = new CreateEventTask(
                body, selectedCategory.id,
                selectedLongitude, selectedLatitude,
                selectedCity, selectedStreet,
                selectedImageFile
        ) {

            @Override
            protected void onSuccess(CreateEventResult result) {
                dialog.dismiss();
                startActivity(EventActivity.getActivityIntent(getBaseContext(), result.event_id));
                finish();
            }

            @Override
            protected void onError(Exception exp) {
                dialog.dismiss();
                Toast.makeText(getBaseContext(), "Ошибка создания", Toast.LENGTH_SHORT).show();
            }
        };
        createEventTask.execute();
    }

    public static Intent getActivityIntent(Context baseContext) {
        return new Intent(baseContext, CreateActivity.class);
    }
}