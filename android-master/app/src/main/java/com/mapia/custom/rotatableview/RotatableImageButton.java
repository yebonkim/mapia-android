package com.mapia.custom.rotatableview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class RotatableImageButton extends ImageButton
        implements Rotatable
{

    private RotatableImageViewHelper rotatableHelper;

    public RotatableImageButton(Context context)
    {
        super(context);
        rotatableHelper = new RotatableImageViewHelper(this);
    }

    public RotatableImageButton(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        rotatableHelper = new RotatableImageViewHelper(this);
    }

    protected void onDraw(Canvas canvas)
    {
        rotatableHelper.onDraw(canvas);
    }

    public void setOrientation(int i, boolean flag)
    {
        rotatableHelper.setOrientation(i, flag);
    }

    public void setRotateEndListener(OnRotateEndListener onrotateendlistener)
    {
        rotatableHelper.setRotateEndListener(onrotateendlistener);
    }
}
