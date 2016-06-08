package com.weibo.skynet.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016/5/6.
 */
public class ImageLoader {

    public static void load(Context context, String url, int error, ImageView imageView) {
        Glide.with(context).load(url).error(error).into(imageView);
    }

    public static void load(Context context, Uri uri, int error, ImageView imageView) {
        Glide.with(context).load(uri).error(error).into(imageView);
    }

    public static void load(Activity activity, String url, int error, ImageView imageView) {
        Glide.with(activity).load(url).error(error).into(imageView);
    }

    public static void load(Fragment fragment, String url, int error, ImageView imageView) {
        Glide.with(fragment).load(url).error(error).into(imageView);
    }

    public static void load(Fragment fragment, int res, int error, ImageView imageView) {
        Glide.with(fragment).load(res).error(error).into(imageView);
    }

    public static void load(Fragment fragment, Uri uri, int error, ImageView imageView) {
        Glide.with(fragment).load(uri).error(error).into(imageView);
    }

    public static void clearCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
        Glide.get(context).clearMemory();
    }

}
