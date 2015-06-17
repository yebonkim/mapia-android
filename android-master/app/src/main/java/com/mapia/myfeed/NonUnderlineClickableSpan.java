package com.mapia.myfeed;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by daehyun on 15. 6. 8..
 */
public class NonUnderlineClickableSpan extends ClickableSpan {

        public void onClick(final View view) {
    }

    public void updateDrawState(final TextPaint textPaint) {
        textPaint.setUnderlineText(false);
    }
}

