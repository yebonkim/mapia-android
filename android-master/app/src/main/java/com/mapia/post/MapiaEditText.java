package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.mapia.custom.FontSettableEditText;


public class MapiaEditText extends FontSettableEditText
{
    private MapiaEditTextEvent mEventListener;
    private char mLastChar;
    private int mLastCharPos;
    private OnMentionKeyListener mMentionKeyListener;

    public MapiaEditText(final Context context) {
        super(context);
    }

    public MapiaEditText(final Context context, final AttributeSet set) {
        super(context, set, 16842862);
    }

    public MapiaEditText(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }

    public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
        if (n == 4 && keyEvent.getAction() == 0 && this.mEventListener != null) {
            return this.mEventListener.onBackKeyDownPreIme(n, keyEvent);
        }
        return super.onKeyPreIme(n, keyEvent);
    }

    protected void onSelectionChanged(final int n, final int n2) {
        if (this.mEventListener != null) {
            this.mEventListener.onCursorPositionChanged();
        }
        super.onSelectionChanged(n, n2);
        final Editable text = this.getText();
        if (text.length() == 0 || n2 <= 0) {
            this.mLastChar = '\0';
            this.mLastCharPos = 0;
            return;
        }
        this.mLastChar = text.charAt(n2 - 1);
        this.mLastCharPos = n2 - 1;
    }

    protected void onTextChanged(final CharSequence charSequence, final int mLastCharPos, final int n, final int n2) {
        super.onTextChanged(charSequence, mLastCharPos, n, n2);
        if (n < n2) {
            this.mLastChar = charSequence.charAt(mLastCharPos);
            this.mLastCharPos = mLastCharPos;
        }
        else if (this.mLastChar == '@' && this.mMentionKeyListener != null) {
            this.mMentionKeyListener.onMentionCharRemoved();
        }
    }

    public void setMentionKeyListener(final OnMentionKeyListener mMentionKeyListener) {
        this.mMentionKeyListener = mMentionKeyListener;
    }

    public void setMapiaEditTextEventListener(final MapiaEditTextEvent mEventListener) {
        this.mEventListener = mEventListener;
    }

    public interface OnMentionKeyListener
    {
        void onMentionCharRemoved();
    }

    public interface MapiaEditTextEvent
    {
        boolean onBackKeyDownPreIme(int p0, KeyEvent p1);

        void onCursorPositionChanged();
    }
}