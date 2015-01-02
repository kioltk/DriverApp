package com.driverapp.android.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jesus Christ. Amen.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private final Context context;

    public ViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
    }

    public Context getContext() {
        return context;
    }
}
