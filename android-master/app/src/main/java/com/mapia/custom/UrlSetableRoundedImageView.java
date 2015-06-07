package com.mapia.custom;

/**
 * Created by daehyun on 15. 6. 6..
 */

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.RoundedImageView;

public class UrlSetableRoundedImageView extends RoundedImageView
{
    private int animationResouceId;
    private String imageUrl;

    public UrlSetableRoundedImageView(final Context context) {
        super(context);
        this.animationResouceId = 0;
    }

    public UrlSetableRoundedImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.animationResouceId = 0;
    }

    public UrlSetableRoundedImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.animationResouceId = 0;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageAnimation(final int animationResouceId) {
        this.animationResouceId = animationResouceId;
    }

    public void setImageUrl(final Context context, final String s) {
        this.setImageUrl(context, s, false, 0);
    }

    public void setImageUrl(final Context context, final String imageUrl, final boolean b, final int n) {
//        if (StringUtils.isBlank(imageUrl) || !imageUrl.contains("http://")) {
//            return;
//        }
//        this.imageUrl = imageUrl;
//        Glide.with(this.getContext()).load(imageUrl).asBitmap().listener((RequestListenersuper Object, Bitmap >)new RequestListener<String, Bitmap>() {
//            @Override
//            public boolean onException(final Exception ex, final String s, final Target<Bitmap> target, final boolean b) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(final Bitmap bitmap, final String s, final Target<Bitmap> target, final boolean b, final boolean b2) {
//                if (bitmap != null) {
//                    if (b) {
//                        if (UrlSetableRoundedImageView.this.animationResouceId > 0) {
//                            UrlSetableRoundedImageView.this.setImageBitmap(BitmapUtils.blurBitmap(bitmap, n));
//                            UrlSetableRoundedImageView.this.startAnimation(AnimationUtils.loadAnimation(context, UrlSetableRoundedImageView.this.animationResouceId));
//                        }
//                        else {
//                            UrlSetableRoundedImageView.this.setImageBitmap(BitmapUtils.blurBitmap(bitmap, n));
//                        }
//                    }
//                    else if (UrlSetableRoundedImageView.this.animationResouceId > 0) {
//                        UrlSetableRoundedImageView.this.setImageBitmap(bitmap);
//                        UrlSetableRoundedImageView.this.startAnimation(AnimationUtils.loadAnimation(context, UrlSetableRoundedImageView.this.animationResouceId));
//                    }
//                    else {
//                        UrlSetableRoundedImageView.this.setImageBitmap(bitmap);
//                    }
//                }
//                return false;
//            }
//        }).preload();
    }
}