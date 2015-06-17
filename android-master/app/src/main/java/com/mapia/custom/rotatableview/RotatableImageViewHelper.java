package com.mapia.custom.rotatableview;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;


public class RotatableImageViewHelper implements Rotatable
{
    static final float SQRT_2 = 1.4142f;
    private boolean fitToScreenFlag;
    RotatableHelper helper;
    private final ImageView imageView;

    public RotatableImageViewHelper(final ImageView imageView) {
        super();
        this.fitToScreenFlag = false;
        this.imageView = imageView;
        this.helper = new RotatableHelper((View)imageView);
    }

    public void onDraw(final Canvas canvas) {
        final Drawable drawable = this.imageView.getDrawable();
        if (drawable != null) {
            final Rect bounds = drawable.getBounds();
            final int n = bounds.right - bounds.left;
            final int n2 = bounds.bottom - bounds.top;
            if (n != 0 && n2 != 0) {
                final int updateCurrentDegreeAndView = this.helper.updateCurrentDegreeAndView();
                final int paddingLeft = this.imageView.getPaddingLeft();
                final int paddingTop = this.imageView.getPaddingTop();
                final int paddingRight = this.imageView.getPaddingRight();
                final int paddingBottom = this.imageView.getPaddingBottom();
                final int n3 = this.imageView.getWidth() - paddingLeft - paddingRight;
                final int n4 = this.imageView.getHeight() - paddingTop - paddingBottom;
                final int saveCount = canvas.getSaveCount();
                if (this.imageView.getScaleType() == ImageView.ScaleType.FIT_CENTER) {
                    float min;
                    final float n5 = min = Math.min(n3 / n, n4 / n2);
                    if (this.fitToScreenFlag) {
                        min = n5;
                        if (updateCurrentDegreeAndView % 90 != 0) {
                            float n7;
                            final float n6 = n7 = Math.abs(updateCurrentDegreeAndView % 90);
                            if (n6 > 45.0f) {
                                n7 = 90.0f - n6;
                            }
                            min = n5 * (-0.0065085874f * n7 + 1.0f);
                        }
                    }
                    canvas.scale(min, min, n3 / 2.0f, n4 / 2.0f);
                }
                canvas.translate((float)(n3 / 2 + paddingLeft), (float)(n4 / 2 + paddingTop));
                canvas.rotate((float)(-updateCurrentDegreeAndView));
                canvas.translate((float)(-n / 2), (float)(-n2 / 2));
                drawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }
    }

    public void setFitToScreenFlag(final boolean fitToScreenFlag) {
        this.fitToScreenFlag = fitToScreenFlag;
    }

    @Override
    public void setOrientation(final int n, final boolean b) {
        this.helper.setOrientation(n, b);
    }

    public void setRotateEndListener(final OnRotateEndListener rotateEndListener) {
        this.helper.setRotateEndListener(rotateEndListener);
    }
}