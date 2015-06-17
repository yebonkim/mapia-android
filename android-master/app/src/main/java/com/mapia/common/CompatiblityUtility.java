package com.mapia.common;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.makeramen.RoundedImageView;


public class CompatiblityUtility
{
    public static void setBackground(final View view, final Drawable drawable) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
            return;
        }
        else {
            view.setBackground(drawable);
        }
    }

    public static void setRoundedImageView(final RoundedImageView roundedImageView, final String s) {
        if (roundedImageView != null && s != null && s.contains("http://")) {
//            Glide.with(roundedImageView.getContext()).load(s).into((ImageView)roundedImageView);
        }
    }
}