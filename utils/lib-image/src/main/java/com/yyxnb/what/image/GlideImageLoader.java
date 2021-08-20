package com.yyxnb.what.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Glide图片加载工具类
 */
public class GlideImageLoader implements IImageLoader {

    private final int mPlaceholderId = R.mipmap.ic_image_default;
    private final int mErrorId = R.mipmap.ic_image_error;

    @Override
    public void displayImage(Object url, ImageView imageView) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(mPlaceholderId) //占位图
                .error(mErrorId)       //错误图
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void displayImage(Object url, ImageView imageView, int placeholder, int error) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholder) //占位图
                .error(error)       //错误图
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @Override
    public void displayGif(Object url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @param imageView
     */
    @Override
    public void displayCircleImage(Object url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop());

        Glide.with(imageView.getContext())
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param url
     * @param imageView
     * @param radius    圆角大小
     */
    @Override
    public void displayRoundImage(Object url, ImageView imageView, int radius) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transforms(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(imageView.getContext())
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 加载图片指定大小
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    @Override
    public void displaySizeImage(Context context, Object url, ImageView imageView, int width, int height) {
        RequestOptions requestOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    /**
     * 为非view加载图片
     */
    @Override
    public void displayImageForTarget(Context context, Target target, Object url) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(withCrossFade())
                .into(target);
    }

    /**
     * 预加载图片
     *
     * @param context 上下文
     * @param url     地址
     */
    @Override
    public void preload(Context context, Object url) {
        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return true;
            }
        }).preload();
    }

    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.ic_image_default)
                .error(R.mipmap.ic_image_error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }

}