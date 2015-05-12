package com.driverapp.android.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.driverapp.android.R;
import com.driverapp.android.core.BaseActivity;
import com.driverapp.android.core.utils.IOUtils;
import com.driverapp.android.core.utils.UserUtil;
import com.driverapp.android.events.create.cropping.CropActivity;

import java.io.File;
import java.io.IOException;

import static com.driverapp.android.DriverApp.app;

public class ProfileActivity extends BaseActivity {

    private static final int REQUEST_GALLERY = 1;
    private static final int CROP_PICTURE = 2;
    private UpdateUserPictureTask updateUserPictureTask;
    private ImageView imageView;
    private EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setBackButtonEnabled();

        nameView = (EditText) findViewById(R.id.user_name);
        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });
        nameView.setText(UserUtil.getName());
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
        try {
            switch (requestCode) {
                case CROP_PICTURE:
                    if (resultCode == RESULT_OK) {
                        final String path = data.getStringExtra(CropActivity.IMAGE_PATH_RESULT);

                        if (path == null) {
                            return;
                        }
                        final File selectedImageFile = new File(path);
                        final AlertDialog alert = new ProgressDialog.Builder(this)
                                .setMessage(R.string.cropping)
                                .setCancelable(false).show();
                        new UpdateUserPictureTask(selectedImageFile) {
                            @Override
                            protected void onSuccess(UpdateUserPictureTaskResult result) {
                                imageView.setImageURI(Uri.fromFile(selectedImageFile));
                                alert.dismiss();
                            }

                            @Override
                            protected void onError(Exception exp) {
                                alert.dismiss();
                            }
                        }.execute();
                    }
                    break;
                case REQUEST_GALLERY:
                    if (data.getData() != null) {
                        sendUri(data.getData());
                    }
                    break;
            }
        } catch (Exception exp) {

        }
    }

    private void sendUri(final Uri uri) {
        new AsyncTask<Void, Void, Void>() {

            private ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setMessage(getString(R.string.profile_avatar_update_loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA, MediaStore.Video.Media.MIME_TYPE,
                        MediaStore.Video.Media.TITLE};
                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
                String mimeType = cursor.getString(cursor.getColumnIndex(filePathColumn[1]));
                String fileName = cursor.getString(cursor.getColumnIndex(filePathColumn[2]));
                if (mimeType == null) {
                    mimeType = "?/?";
                }
                cursor.close();

                if (picturePath == null || !uri.getScheme().equals("file")) {
                    File externalFile = app().getExternalFilesDir(null);
                    if (externalFile == null) {
                        return null;
                    }
                    String externalPath = externalFile.getAbsolutePath();

                    File dest = new File(externalPath + "/Actor/");
                    dest.mkdirs();

                    File outputFile = new File(dest, "avatar.jpg");
                    picturePath = outputFile.getAbsolutePath();

                    try {
                        IOUtils.copy(getContentResolver().openInputStream(uri), new File(picturePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                if (fileName == null) {
                    fileName = picturePath;
                }

                startActivityForResult(CropActivity.crop(getBaseContext(), picturePath), CROP_PICTURE);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                progressDialog.dismiss();
            }
        }.execute();
    }

}
