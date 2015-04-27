package com.driverapp.android;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;
// import com.vk.sdk.VKSdk;

/**
 * Created by Jesus Christ. Amen.
 */
public class DriverApp extends Application  {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "qZy3nXpsNgruIARx3gZ1RO9n6";
    private static final String TWITTER_SECRET = "iMZItu6czGryyBMNMjr9NzcGLP0vrk8BvUXFdM8ik4af1lxb9j";


    private static DriverApp application;

    public static String getUserId() {
        return "1";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        application = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.MAX_PRIORITY)
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(40)
                .diskCacheSize(100 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);

        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analytics.newTracker(R.xml.global_tracker);
    }
    public static DriverApp app(){
        return application;
    }
}
