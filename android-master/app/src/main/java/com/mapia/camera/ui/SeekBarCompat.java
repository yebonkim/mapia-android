package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AbsSeekBar;
import android.widget.SeekBar;

import java.lang.reflect.Field;


public class SeekBarCompat extends SeekBar
{
    public SeekBarCompat(final Context context) {
        super(context);
    }

    public SeekBarCompat(final Context context, final AttributeSet set) {
        super(context, set);
        this.setThumbOffsetFromAttributeSet(context, set);
    }

    public SeekBarCompat(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setThumbOffsetFromAttributeSet(context, set);
    }

    protected static void setSeekBarTouchProgressOffset(final SeekBar seekBar, final float n) {
        try {
            final Field declaredField = AbsSeekBar.class.getDeclaredField("mTouchProgressOffset");
            final boolean accessible = declaredField.isAccessible();
            if (!accessible) {
                declaredField.setAccessible(true);
            }
            try {
                declaredField.set(seekBar, n);
                if (!accessible) {
                    declaredField.setAccessible(accessible);
                }
            }
            catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            catch (IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
        catch (SecurityException ex4) {}
        catch (NoSuchFieldException ex3) {
            ex3.printStackTrace();
        }
    }

    private void setThumbOffsetFromAttributeSet(final Context context, final AttributeSet set) {
        if (Build.VERSION.SDK_INT <= 10) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, new int[] { 16843075 });
            final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(0, -1);
            obtainStyledAttributes.recycle();
            if (dimensionPixelOffset != -1) {
                this.setThumbOffset(dimensionPixelOffset);
            }
        }
    }
}