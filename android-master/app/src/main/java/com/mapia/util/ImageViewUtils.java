package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class ImageViewUtils
{
    public static void setImageFromUrl(final ImageView imageView, final String s) {
        setImageFromUrl(imageView, s, null);
    }

    public static void setImageFromUrl(final ImageView imageView, final String s, final ImageViewUtilEvent imageViewUtilEvent) {
        if (imageView == null || s == null || !s.trim().contains("http://")) {
            return;
        }
        Glide.with(imageView.getContext()).load(s).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(final Exception ex, final String s, final Target<Bitmap> target, final boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(final Bitmap imageBitmap, final String s, final Target<Bitmap> target, final boolean b, final boolean b2) {
                if (imageBitmap != null) {
                    imageView.setImageBitmap(imageBitmap);
                    if (imageViewUtilEvent != null) {
                        imageViewUtilEvent.onImageDownCompleted();
                    }
                }
                return false;
            }
        });
    }

    public interface ImageViewUtilEvent
    {
        void onImageDownCompleted();
    }
}