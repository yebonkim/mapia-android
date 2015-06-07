package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by daehyun on 15. 6. 2..
 */
public class RectangleLinearLayout extends LinearLayout {
    public RectangleLinearLayout(Context paramContext){super(paramContext);}
    public RectangleLinearLayout(Context paramContext, AttributeSet paramAttributeSet){
        super(paramContext, paramAttributeSet);
    }
    protected void onMeasure(int paramInt1, int paramInt2){
        paramInt1 = View.MeasureSpec.getSize(paramInt1);
//        super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, ), View.MeasureSpec.makeMeasureSpec(paramInt1 / 3, ))
    }
}
