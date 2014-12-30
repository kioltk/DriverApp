package com.driverapp.android.create;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.core.Core;
import com.driverapp.android.models.Category;
import com.nostra13.universalimageloader.core.ImageLoader;


public class CreateActivity extends ActionBarActivity {

    private static final int PICK_IMAGE = 100;
    private static final int PICK_CATEGORY = 101;
    private ImageView imageView;
    private Category selectedCategory;
    private View pickCategoryView;
    private TextView pickCategoryTextView;
    private ImageView pickCategoryImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        pickCategoryView =  findViewById(R.id.pick_category);
        pickCategoryTextView = (TextView) findViewById(R.id.pick_category_text);
        pickCategoryImageView = (ImageView) findViewById(R.id.pick_category_image);

        pickCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getBaseContext(), CategoryActivity.class), PICK_CATEGORY);
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
                if(resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();

                    //User had pick an image.
                    Cursor cursor = getContentResolver().query(imageUri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                    cursor.moveToFirst();

                    //Link to the image
                    final String imageFilePath = cursor.getString(0);
                    ImageLoader.getInstance().displayImage(imageUri.toString(), imageView);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    // todo cropper?
                    cursor.close();
                }
                break;
            case PICK_CATEGORY:
                if(resultCode== RESULT_OK) {
                    int categoryId = data.getExtras().getInt(CategoryActivity.EXTRA_ID);
                    for (Category category : Core.getCategories()) {
                        if(category.id==categoryId){
                            selectedCategory = category;
                            pickCategoryTextView.setText(selectedCategory.name);
                            pickCategoryImageView.setImageResource(selectedCategory.imgResId);
                            break;
                        }
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getActivityIntent(Context baseContext) {
        return new Intent(baseContext, CreateActivity.class);
    }
}
