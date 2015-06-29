package com.mapia.custom;

/**
 * Created by daehyun on 15. 6. 21..
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class ResizableImageView extends ImageView
{
    public ResizableImageView(final Context context) {
        super(context);
    }

    public ResizableImageView(final Context context, final AttributeSet set) {
        super(context, set);
    }

    public ResizableImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }

    protected void onMeasure(int size, int size2) {
        final Drawable drawable = this.getDrawable();
        if (drawable != null) {
            if (View.MeasureSpec.getSize(size2) / View.MeasureSpec.getSize(size) > drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth()) {
                size2 = View.MeasureSpec.getSize(size);
                size = (int)Math.ceil(size2 * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth());
            }
            else {
                size = View.MeasureSpec.getSize(size2);
                size2 = (int)Math.ceil(size * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight());
            }
            this.setMeasuredDimension(size2, size);
            return;
        }
        super.onMeasure(size, size2);
    }
}