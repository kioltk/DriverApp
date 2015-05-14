package com.driverapp.android.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.driverapp.android.R;

/**
 * Created by Jesus Christ. Amen.
 */
public class AboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        WebView webview = (WebView) rootView.findViewById(R.id.webview);
        webview.loadUrl("http://driverapp.ru/about");
        return rootView;
    }
}
