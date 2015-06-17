package com.mapia.custom;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.mapia.util.FontUtils;

public class FontSettableButton extends Button
{
    public FontSettableButton(final Context context) {
        super(context);
    }

    public FontSettableButton(final Context context, final AttributeSet set) {
        super(context, set);
        this.setCustomFont(context, set);
    }

    public FontSettableButton(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setCustomFont(context, set);
    }

    private void setCustomFont(final Context context, final AttributeSet set) {
//        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FontSettableTextView);
//        this.setCustomFont(context, obtainStyledAttributes.getString(0));
//        obtainStyledAttributes.recycle();
    }

    public void setCustomFont(final Context context, final String s) {
        if ("NanumBold".equals(s)) {
            this.setTypeface(FontUtils.getNanumBold());
        }
        else if ("NanumRegular".equals(s)) {
            this.setTypeface(FontUtils.getNanumRegular());
        }
    }

    public void setNanumBold(final Context context, final boolean b) {
        if (b) {
            this.setTypeface(FontUtils.getNanumBold());
        }
    }

    public void setNanumRegular(final Context context, final boolean b) {
        if (b) {
            this.setTypeface(FontUtils.getNanumRegular());
        }
    }
}