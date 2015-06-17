package com.mapia.camera.viewmodel;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Rect;

import com.mapia.util.ImageSizeUtil;


public class RatioManager
{
    public static final int RATIO_1_1 = 0;
    public static final int RATIO_3_4 = 1;
    public static final int RATIO_4_3 = 2;
    private Rect currentRect;
    private Rect rect_1_1;
    private Rect rect_3_4;
    private Rect rect_4_3;

    public RatioManager(final int n, final int n2) {
        super();
        final Rect rect = new Rect(0, 0, n, n2);
        this.rect_1_1 = ImageSizeUtil.fitCenter(new Rect(0, 0, 1, 1), rect);
        this.rect_3_4 = ImageSizeUtil.fitCenter(new Rect(0, 0, 3, 4), rect);
        this.rect_4_3 = ImageSizeUtil.fitCenter(new Rect(0, 0, 4, 3), rect);
    }

    public String getCurrentRatio() {
        if (this.currentRect.equals((Object)this.rect_3_4)) {
            return "3:4";
        }
        if (this.currentRect.equals((Object)this.rect_4_3)) {
            return "4:3";
        }
        return "1:1";
    }

    public Rect getCurrentRect() {
        return this.currentRect;
    }

    public Rect getRect_3_4() {
        return this.rect_3_4;
    }

    public boolean is11Ratio() {
        return this.currentRect.equals((Object)this.rect_1_1);
    }

    public boolean is34Ratio() {
        return this.currentRect.equals((Object)this.rect_3_4);
    }

    public boolean is43Ratio() {
        return this.currentRect.equals((Object)this.rect_4_3);
    }

    public void setCurrentRect(final int n) {
        switch (n) {
            default: {}
            case 0: {
                this.currentRect = this.rect_1_1;
            }
            case 1: {
                this.currentRect = this.rect_3_4;
            }
            case 2: {
                this.currentRect = this.rect_4_3;
            }
        }
    }
}