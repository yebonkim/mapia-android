package com.mapia.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mapia.util.FontUtils;

public class FontSettableTextView extends TextView
{

    public FontSettableTextView(Context context)
    {
        super(context);
    }

    public FontSettableTextView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        setCustomFont(context, attributeset);
    }

    public FontSettableTextView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        setCustomFont(context, attributeset);
    }

    private void setCustomFont(Context context, AttributeSet attributeset)
    {
//        attributeset = context.obtainStyledAttributes(attributeset, FontSettableTextView);
//        setCustomFont(context, attributeset.getString(0));
//        attributeset.recycle();
    }

    public void setCustomFont(Context context, String s)
    {
        if ("NanumBold".equals(s))
        {
            setTypeface(FontUtils.getNanumBold());
        } else
        if ("NanumRegular".equals(s))
        {
            setTypeface(FontUtils.getNanumRegular());
            return;
        }
    }

    public void setNanumBold(Context context, boolean flag)
    {
        if (flag)
        {
            setTypeface(FontUtils.getNanumBold());
        }
    }

    public void setNanumRegular(Context context, boolean flag)
    {
        if (flag)
        {
            setTypeface(FontUtils.getNanumRegular());
        }
    }
}
