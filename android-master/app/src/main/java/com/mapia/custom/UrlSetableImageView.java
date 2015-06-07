package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestListener;
//import com.bumptech.glide.request.target.Target;
//import com.mapia.util.BitmapUtils;

//import org.apache.commons.lang3.StringUtils;

public class UrlSetableImageView extends ImageView
{
    private int animationResouceId;
    private String imageUrl;

    public UrlSetableImageView(final Context context) {
        super(context);
        this.animationResouceId = 0;
    }

    public UrlSetableImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.animationResouceId = 0;
    }

    public UrlSetableImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.animationResouceId = 0;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageAnimation(final int animationResouceId) {
        this.animationResouceId = animationResouceId;
    }

//    public void setImageUrl(final String s) {
//        this.setImageUrl(s, false, 0);
//    }
//    public void setImageUrl(final String imageUrl, final boolean b, final int n) {
////        if (StringUtils.isBlank(imageUrl) || !imageUrl.contains("http://")) {
////            return;
////        } else {
//            this.imageUrl = imageUrl;
////            Glide.with(getContext()).load(imageUrl).asBitmap().listener(new RequestListener<String, Bitmap>() {
//                public boolean onException(final Exception ex, final String s, final Target<Bitmap> target, final boolean b) {
//                    return false;
//                }
//                public boolean onResourceReady(final Bitmap bitmap, final String s, final Target<Bitmap> target, final boolean b, final boolean b2) {
//                    if (bitmap != null) {
//                        if (b) {
//                            if (UrlSetableImageView.this.animationResouceId > 0) {
////                                UrlSetableImageView.this.setImageBitmap(BitmapUtils.blurBitmap(bitmap, n));
//                                UrlSetableImageView.this.startAnimation(AnimationUtils.loadAnimation(UrlSetableImageView.this.getContext(), UrlSetableImageView.this.animationResouceId));
//                            } else {
////                                UrlSetableImageView.this.setImageBitmap(BitmapUtils.blurBitmap(bitmap, n));
//                            }
//                        } else if (UrlSetableImageView.this.animationResouceId > 0) {
//                            UrlSetableImageView.this.setImageBitmap(bitmap);
//                            UrlSetableImageView.this.startAnimation(AnimationUtils.loadAnimation(UrlSetableImageView.this.getContext(), UrlSetableImageView.this.animationResouceId));
//                        } else {
//                            UrlSetableImageView.this.setImageBitmap(bitmap);
//                        }
//                    }
//                    return false;
//                }
//            });//.preload();
////        }
//    }
}