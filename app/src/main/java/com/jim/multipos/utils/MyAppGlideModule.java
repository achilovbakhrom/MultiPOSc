package com.jim.multipos.utils;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by developer on 06.09.2017.
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 512; // 100 MB
        builder.setDiskCache(
                new ExternalCacheDiskCacheFactory(context, "multipos", diskCacheSizeBytes));
    }
}
