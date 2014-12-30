package com.driverapp.android.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.driverapp.android.R;
import com.driverapp.android.models.Category;

import java.util.ArrayList;

/**
 * Created by Jesus Christ. Amen.
 */
public class CategoryAdapter extends BaseAdapter {
    private final ArrayList<Category> categories;
    private Context context;

    public CategoryAdapter(Context context, ArrayList<Category> categories){
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_category, null);

        Category category = getItem(position);

        TextView nameView = (TextView) itemView.findViewById(R.id.text);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);

        nameView.setText(category.name);
        imageView.setImageResource(category.imgResId);

        return itemView;
    }
}
