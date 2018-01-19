package com.feeling.mybaseapp.config;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;


/**
 * Created by 123 on 2017/11/9.
 */

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 128*1024*1024;
        String diskCachePath="images";
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,diskCachePath,diskCacheSizeBytes));
    }
}
