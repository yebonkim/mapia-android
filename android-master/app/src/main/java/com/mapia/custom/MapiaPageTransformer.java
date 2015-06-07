package com.mapia.custom;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by daehyun on 15. 6. 6..
 */
public class MapiaPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9f;

    public MapiaPageTransformer(){

    }

    @Override
    public void transformPage(View view, final float n){
        final int width = view.getWidth();
        final int height = view.getHeight();
        if (n >= -1.0f && n <= 1.0f) {
            final float max = Math.max(0.9f, 1.0f - Math.abs(n));
            final float n2 = height * (1.0f - max) / 4.0f;
            final float n3 = width * (1.0f - max) / 4.0f;
            if (n < 0.0f) {
                view.setTranslationX(n3 - n2 / 2.0f);
            }
            else {
                view.setTranslationX(-n3 + n2 / 2.0f);
            }
            view.setScaleX(1.0f - Math.abs(n) / 15.0f);
            view.setScaleY(1.0f - Math.abs(n) / 15.0f);
        }
    }
}
