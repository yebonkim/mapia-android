package com.mapia.myfeed;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;

import com.mapia.custom.FontSettableTextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MapiaTextView extends FontSettableTextView
{
    private static Pattern mTagPattern;

    static {
        MapiaTextView.mTagPattern = Pattern.compile("#[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,20}");
    }

    public MapiaTextView(final Context context) {
        this(context, null, 0);
    }

    public MapiaTextView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }

    public MapiaTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.setHighlightColor(0);
    }

    private void applyStyle(final SpannableString spannableString, final int n, final int n2, final int n3) {
        spannableString.setSpan((Object)new ForegroundColorSpan(n3), n, n2, 33);
    }

    private void applyStyle(final SpannableString spannableString, final int n, final int n2, final int n3, final int n4, final OnMapiaTextViewTagClick onMapiaTextViewTagClick) {
        spannableString.setSpan((Object)new ForegroundColorSpan(n3), n, n2, 33);
        spannableString.setSpan((Object)new StyleSpan(n4), n, n2, 33);
        if (onMapiaTextViewTagClick != null) {
            spannableString.setSpan((Object)new NonUnderlineClickableSpan() {
                @Override
                public void onClick(final View view) {
                    onMapiaTextViewTagClick.onClick(spannableString.subSequence(n, n2).toString());
                }
            }, n, n2, 33);
            this.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private boolean isUnderbarTag(final CharSequence charSequence) {
        if (charSequence != null) {
            for (int i = 0; i < charSequence.length(); ++i) {
                if (charSequence.charAt(i) != '_') {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public SpannableString getHighlightedTag(final int n, final int n2, final OnMapiaTextViewTagClick onMapiaTextViewTagClick) {
        final SpannableString spannableString = new SpannableString(this.getText());
        SpannableString spannableString2;
        if (spannableString == null) {
            spannableString2 = null;
        }
        else {
            final Matcher matcher = MapiaTextView.mTagPattern.matcher((CharSequence)spannableString);
            while (true) {
                spannableString2 = spannableString;
                if (!matcher.find()) {
                    break;
                }
                if (this.isUnderbarTag(spannableString.subSequence(matcher.start() + 1, matcher.end()))) {
                    continue;
                }
                this.applyStyle(spannableString, matcher.start(), matcher.end(), n, n2, onMapiaTextViewTagClick);
            }
        }
        return spannableString2;
    }

    public void highlightTag(final int n, final int n2, final OnMapiaTextViewTagClick onMapiaTextViewTagClick) {
        this.setText((CharSequence)this.getHighlightedTag(n, n2, onMapiaTextViewTagClick));
        this.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void highlightTag(final int n, final String s) {
        final SpannableString text = new SpannableString(this.getText());
        if (text == null) {
            return;
        }
        final Matcher matcher = Pattern.compile(s).matcher((CharSequence)text);
        while (matcher.find()) {
            this.applyStyle(text, matcher.start(), matcher.end(), n);
        }
        this.setText((CharSequence)text);
    }

    public interface OnMapiaTextViewTagClick
    {
        void onClick(String p0);
    }
}