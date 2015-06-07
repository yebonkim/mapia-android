package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by daehyun on 15. 6. 3..
 */
public class ListenableScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;
    public ListenableScrollView(Context paramContext){
        super(paramContext);
    }
    public ListenableScrollView(Context paramContext, AttributeSet paramAttributeSet){
        super(paramContext, paramAttributeSet);
    }

    public ListenableScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt){
        super(paramContext, paramAttributeSet, paramInt);
    }

    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4){
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        if(this.scrollViewListener != null){
            this.scrollViewListener.onScrollChanged(this, paramInt1, paramInt2, paramInt3, paramInt4);
        }
    }

    public void setOnScrollViewListener(ScrollViewListener paramScrollViewListener){
        this.scrollViewListener = paramScrollViewListener;
    }

}
