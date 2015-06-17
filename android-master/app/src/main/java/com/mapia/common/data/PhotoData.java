package com.mapia.common.data;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Bitmap;

import com.mapia.util.BitmapUtils;

public class PhotoData
{
    private double mLatitude;
    private double mLongitude;
    private Bitmap mPhoto;

    public PhotoData() {
        super();
        this.init();
    }

    public PhotoData(Bitmap mPhoto, final int n, final boolean b) {
        super();
        if (mPhoto != null) {
            final Bitmap bitmap = mPhoto = this.resizeBitmap(mPhoto);
            if (b) {
                mPhoto = BitmapUtils.makeSquareSizeBitmap(bitmap);
            }
            if (n == 0) {
                this.mPhoto = mPhoto;
            }
            else {
                this.mPhoto = BitmapUtils.rotate(mPhoto, n);
            }
        }
        this.init();
    }

    private void init() {
        this.mLatitude = 200.0;
        this.mLongitude = 200.0;
    }

    private Bitmap resizeBitmap(final Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        if (1080 < bitmap.getWidth() || 1080 < bitmap.getHeight()) {
            int n;
            int n2;
            if (width > height) {
                n = 1080;
                n2 = 810;
            }
            else if (width < height) {
                n2 = 1080;
                n = 810;
            }
            else {
                n = 1080;
                n2 = 1080;
            }
            return BitmapUtils.lanczosResizeBitmap(bitmap, n, n2);
        }
        return bitmap;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public Bitmap getPhoto() {
        return this.mPhoto;
    }

    public void setLocation(final double mLatitude, final double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public void setPhoto(final Bitmap mPhoto) {
        this.mPhoto = mPhoto;
    }
}