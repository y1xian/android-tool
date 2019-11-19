package com.yyxnb.widget.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yyxnb.view.proxy.imageloader.ILoaderProxy;
import com.yyxnb.view.proxy.imageloader.LoaderOptions;


public class GlideLoader implements ILoaderProxy {

    @Override
    public void loadImage(LoaderOptions options) {

        if (options.getUrl() != null){
            GlideApp.with(options.getTargetView())
                    .asBitmap()
                    .load(options.getUrl())
                    .placeholder(options.getPlaceholderResId())
                    .error(options.getErrorResId())
                    .into(options.getTargetView());
        }
        if (options.getUri() != null){
            GlideApp.with(options.getTargetView())
                    .asBitmap()
                    .load(options.getUri())
                    .placeholder(options.getPlaceholderResId())
                    .error(options.getErrorResId())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (options.getCallBack() != null){
                                options.getCallBack().onBitmapLoaded(resource);
                            }
                        }
                    });
        }



    }

    @Override
    public void clearMemoryCache() {

    }

    @Override
    public void clearDiskCache() {

    }
}
