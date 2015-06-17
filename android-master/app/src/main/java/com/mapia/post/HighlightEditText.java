package com.mapia.post;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.mapia.R;
import com.mapia.myfeed.NonUnderlineClickableSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightEditText extends EditText
{
    private final int HIGHLIGHTING_COLOR;
    private final int MAX_TAG_LEN;
    private final int MAX_TEXT_LEN;
    private final int ORIGINAL_COLOR;
    private char mLastChar;
    private int mLastCharPos;
    private int mLastSelEnd;
    private int mLastSelStart;
    private int mLastStart;
    private OnMentionKeyListener mMentionKeyListener;
    private OnBackKeyDownPreIme mOnBackeyDownPreIme;
    private boolean mOnTaging;
    private final Pattern mPattern;
    private OnTagModeChangeListener mTagModeChangedListener;
    private ValidationChecker mValidationChecker;

    public HighlightEditText(final Context context) {
        this(context, null, 16842862);
    }

    public HighlightEditText(final Context context, final AttributeSet set) {
        this(context, set, 16842862);
    }

    public HighlightEditText(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.MAX_TAG_LEN = 20;
        this.MAX_TEXT_LEN = 500;
        this.ORIGINAL_COLOR = this.getResources().getColor(R.color.point_color_post_text);
        this.HIGHLIGHTING_COLOR = this.getResources().getColor(R.color.point_color_post);
        this.mPattern = Pattern.compile("#[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{0,20}");
        this.mLastSelStart = 0;
        this.mLastSelEnd = 0;
        this.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500) });
        this.setFilters(new InputFilter[] { new InputFilter.LengthFilter(500) {
            public CharSequence filter(CharSequence filter, final int n, final int n2, final Spanned spanned, final int n3, final int n4) {
                filter = super.filter(filter, n, n2, spanned, n3, n4);
                if (filter != null) {
                    HighlightEditText.this.mValidationChecker.onMaxTextLen();
                }
                return filter;
            }
        } });
    }

    private void applyStyle(final SpannableString spannableString, final int n, final int n2, final int n3, final int n4, final OnHighlightTextClick onHighlightTextClick, final int n5) {
        spannableString.setSpan((Object)new ForegroundColorSpan(n3), n, n2, n5);
        spannableString.setSpan((Object)new StyleSpan(n4), n, n2, n5);
        if (onHighlightTextClick != null) {
            spannableString.setSpan((Object)new NonUnderlineClickableSpan() {
                @Override
                public void onClick(final View view) {
                    onHighlightTextClick.onClick(spannableString.subSequence(n, n2).toString(), n, n2);
                }
            }, n, n2, n5);
        }
    }

    private void changeTagMode(final boolean mOnTaging) {
        this.mOnTaging = mOnTaging;
    }

    private boolean checkLeft(final Editable editable, final int n) {
        int n2;
        if (n >= editable.length()) {
            n2 = editable.length() - 1;
        }
        else {
            n2 = n;
        }
        while (n2 >= 0 && n2 != n - 20 - 1) {
            final char char1 = editable.charAt(n2);
            if (Character.isWhitespace(char1) || this.isSpecialChar(char1)) {
                break;
            }
            if (char1 == '#') {
                return true;
            }
            --n2;
        }
        return false;
    }

    private boolean checkLeft(final CharSequence charSequence, final int n) {
        int n2;
        if (n >= charSequence.length()) {
            n2 = charSequence.length() - 1;
        }
        else {
            n2 = n;
        }
        while (n2 >= 0 && n2 != n - 20) {
            final char char1 = charSequence.charAt(n2);
            if (Character.isWhitespace(char1) || this.isSpecialChar(char1)) {
                break;
            }
            if (char1 == '#') {
                return true;
            }
            --n2;
        }
        return false;
    }

    private void checkTagMode(final int n) {
        this.getLeft(this.getText().toString(), n);
    }

    private String getCurrentParagraph(final int n, final int n2) {
        final String string = this.getText().toString();
        return string.substring(this.getLeft(string, n), this.getRight(string, n2));
    }

    private int getLeft(final Editable editable, int n) {
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n <= 0) {
                break;
            }
            n3 = n;
            if (n2 >= 20) {
                break;
            }
            final char char1 = editable.charAt(n - 1);
            if (Character.isWhitespace(char1) || char1 == '#') {
                n3 = n - 1;
                break;
            }
            if (this.isSpecialChar(char1)) {
                return n - 1;
            }
            --n;
            ++n2;
        }
        return n3;
    }

    private int getLeft(final SpannableString spannableString, int n) {
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n <= 0) {
                break;
            }
            n3 = n;
            if (n2 >= 20) {
                break;
            }
            final char char1 = spannableString.charAt(n - 1);
            if (Character.isWhitespace(char1) || char1 == '#') {
                n3 = n - 1;
                break;
            }
            if (this.isSpecialChar(char1)) {
                return n - 1;
            }
            --n;
            ++n2;
        }
        return n3;
    }

    private int getLeft(final String s, int n) {
        int n2 = 0;
        int n3;
        while (true) {
            n3 = n;
            if (n <= 0) {
                break;
            }
            n3 = n;
            if (n2 >= 20) {
                break;
            }
            final char char1 = s.charAt(n - 1);
            if (Character.isWhitespace(char1) || char1 == '#') {
                n3 = n - 1;
                break;
            }
            if (this.isSpecialChar(char1)) {
                return n - 1;
            }
            --n;
            ++n2;
        }
        return n3;
    }

    private int getRight(final Editable editable, int i) {
        while (i < editable.length()) {
            final char char1 = editable.charAt(i);
            if (Character.isWhitespace(char1) || char1 == '#' || this.isSpecialChar(char1)) {
                return i;
            }
            ++i;
        }
        return editable.length();
    }

    private int getRight(final SpannableString spannableString, int i) {
        while (i < spannableString.length()) {
            final char char1 = spannableString.charAt(i);
            if (Character.isWhitespace(char1) || char1 == '#' || this.isSpecialChar(char1)) {
                return i;
            }
            ++i;
        }
        return spannableString.length();
    }

    private int getRight(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (Character.isWhitespace(char1) || char1 == '#' || this.isSpecialChar(char1)) {
                return i;
            }
            ++i;
        }
        return s.length();
    }

    private boolean isSpecialChar(final char c) {
        return !Character.isLetterOrDigit(c) && c != '_' && c != '#';
    }

    private void notifyInsertingTag() {
        if (this.mOnTaging && this.mTagModeChangedListener != null) {
            this.mTagModeChangedListener.onTagInserting(this.getCurrentParagraph());
        }
    }

    private void notifyTagModeChanged(final boolean mOnTaging) {
        if (this.mOnTaging != mOnTaging) {
            this.mOnTaging = mOnTaging;
            if (this.mTagModeChangedListener != null) {
                this.mTagModeChangedListener.onTagModeChanged(this.mOnTaging);
            }
        }
    }

    private void stopHighlighting(int i) {
        final Editable text = this.getText();
        final int left = this.getLeft(text, i);
        final CharacterStyle[] array = (CharacterStyle[])text.getSpans(left, i, (Class)ForegroundColorSpan.class);
        for (int j = 0; j < array.length; ++j) {
            final int spanStart = text.getSpanStart((Object)array[j]);
            final int spanEnd = text.getSpanEnd((Object)array[j]);
            text.removeSpan((Object)array[j]);
            text.setSpan((Object)new ForegroundColorSpan(this.HIGHLIGHTING_COLOR), spanStart, spanEnd - 1, 33);
        }
        CharacterStyle[] array2;
        int spanStart2;
        int spanEnd2;
        for (array2 = (CharacterStyle[])text.getSpans(left, i, (Class)StyleSpan.class), i = 0; i < array2.length; ++i) {
            spanStart2 = text.getSpanStart((Object)array2[i]);
            spanEnd2 = text.getSpanEnd((Object)array2[i]);
            text.removeSpan((Object)array2[i]);
            text.setSpan((Object)new StyleSpan(1), spanStart2, spanEnd2 - 1, 33);
        }
    }

    public void addText(final String s) {
        this.getText().replace(this.getSelectionStart(), this.getSelectionEnd(), (CharSequence)s);
    }

    public int getCurrentLine() {
        return this.getLayout().getLineForOffset(this.getSelectionStart());
    }

    public String getCurrentParagraph() {
        return this.getCurrentParagraph(this.getSelectionStart(), this.getSelectionEnd());
    }

    public char getLastChar() {
        return this.mLastChar;
    }

    public int getLeftTag() {
        int selectionStart = this.getSelectionStart();
        final Editable text = this.getText();
        int n = 0;
        int n2;
        while (true) {
            n2 = selectionStart;
            if (selectionStart <= 0) {
                break;
            }
            n2 = selectionStart;
            if (n >= 20) {
                break;
            }
            final char char1 = text.charAt(selectionStart - 1);
            if (Character.isWhitespace(char1) || char1 == '#') {
                n2 = selectionStart - 1;
                break;
            }
            if (this.isSpecialChar(char1)) {
                return selectionStart - 1;
            }
            --selectionStart;
            ++n;
        }
        return n2;
    }

    public boolean getOnTagging() {
        return this.mOnTaging;
    }

    public boolean getTagMode() {
        return this.mOnTaging;
    }

    public void highlightAll() {
        final SpannableString spannableString = new SpannableString((CharSequence)this.getText());
        if (spannableString == null) {
            return;
        }
        int end = 0;
        final Matcher matcher = Pattern.compile("#[a-zA-Z0-9\uac00-\ud7a3\u3131-\u314e\u314f-\u3163\u00e1\u00e9\u00ed\u00f3\u00fa\u00fc\u00f1\u00c1\u00c9\u00cd\u00d3\u00da\u00dc\u00d1\u00e4\u00f6\u00df\u00c4\u00d6\u00df\u3041-\u3093\u30fc\u30a1-\u30f4\u30fc\u4e00-\u9fa0_]{1,20}").matcher((CharSequence)spannableString);
        while (matcher.find()) {
            final int start = matcher.start();
            end = matcher.end();
            this.applyStyle(spannableString, start, end, this.HIGHLIGHTING_COLOR, 1, null, 34);
            this.getText().replace(start, end, spannableString.subSequence(start, end));
        }
        if (end == this.getSelectionEnd() && this.getSelectionEnd() != 0) {
            this.mOnTaging = true;
            return;
        }
        this.mOnTaging = false;
    }

    public void highlightTag(int i) {
        final SpannableString spannableString = new SpannableString((CharSequence)this.getText());
        if (spannableString != null) {
            final int left = this.getLeft(spannableString, i);
            final int right = this.getRight(spannableString, this.getSelectionEnd());
            if (left == right) {
                this.notifyTagModeChanged(true);
                return;
            }
            if (this.mOnTaging) {
                if (right - left == 21) {
                    final Editable text = this.getText();
                    CharacterStyle[] array;
                    for (array = (CharacterStyle[])text.getSpans(left, right, (Class)ForegroundColorSpan.class), i = 0; i < array.length; ++i) {
                        text.removeSpan((Object)array[i]);
                        text.setSpan((Object)new ForegroundColorSpan(this.HIGHLIGHTING_COLOR), left, right, 33);
                    }
                    CharacterStyle[] array2;
                    for (array2 = (CharacterStyle[])text.getSpans(left, right, (Class)StyleSpan.class), i = 0; i < array2.length; ++i) {
                        text.removeSpan((Object)array2[i]);
                        text.setSpan((Object)new StyleSpan(1), left, right, 33);
                    }
                    this.notifyTagModeChanged(false);
                }
            }
            else {
                final Matcher matcher = this.mPattern.matcher(spannableString.subSequence(left, right));
                if (matcher.find()) {
                    i = left + matcher.start();
                    final int n = left + matcher.end();
                    this.applyStyle(spannableString, i, n, this.HIGHLIGHTING_COLOR, 1, null, 34);
                    this.getText().replace(i, n, spannableString.subSequence(i, n));
                    this.notifyTagModeChanged(true);
                }
            }
        }
    }

    public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
        if (n == 4 && keyEvent.getAction() == 0 && this.mOnBackeyDownPreIme != null) {
            return this.mOnBackeyDownPreIme.onBackKeyDownPreIme(n, keyEvent);
        }
        return super.onKeyPreIme(n, keyEvent);
    }

    protected void onSelectionChanged(final int n, final int n2) {
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
            if (this.mLastChar == '@') {
                char char1;
                if (mLastCharPos > 0) {
                    char1 = charSequence.charAt(mLastCharPos - 1);
                }
                else {
                    char1 = ' ';
                }
                if (this.mMentionKeyListener != null && Character.isWhitespace(char1)) {
                    this.mMentionKeyListener.onMentionKey();
                }
            }
            if (Character.isWhitespace(this.mLastChar) && this.mMentionKeyListener != null) {
                this.mMentionKeyListener.onWhitespaceKey();
            }
            if (this.isSpecialChar(this.mLastChar)) {
                if (this.checkLeft(charSequence, mLastCharPos - 1)) {
                    this.stopHighlighting(mLastCharPos);
                }
                this.notifyTagModeChanged(false);
            }
            else {
                this.highlightTag(mLastCharPos);
            }
        }
        else if (n > n2) {
            if (this.mLastChar == '@' && this.mMentionKeyListener != null) {
                this.mMentionKeyListener.onMentionCharRemoved();
            }
            if (this.mLastChar == '#') {
                if (this.mLastCharPos <= 0 || charSequence.charAt(this.mLastCharPos - 1) != '#') {
                    this.unhighlightTag(mLastCharPos);
                }
            }
            else if (this.isSpecialChar(this.mLastChar) && mLastCharPos != 0) {
                this.highlightTag(mLastCharPos);
            }
            else {
                this.notifyTagModeChanged(this.checkLeft(this.getText(), mLastCharPos));
            }
        }
        this.notifyInsertingTag();
    }

    public void setMentionKeyListener(final OnMentionKeyListener mMentionKeyListener) {
        this.mMentionKeyListener = mMentionKeyListener;
    }

    public void setOnBackKeyDownPreIme(final OnBackKeyDownPreIme mOnBackeyDownPreIme) {
        this.mOnBackeyDownPreIme = mOnBackeyDownPreIme;
    }

    public void setOnTagModeChangeListener(final OnTagModeChangeListener mTagModeChangedListener) {
        this.mTagModeChangedListener = mTagModeChangedListener;
    }

    public void setOnTagging(final boolean mOnTaging) {
        this.mOnTaging = mOnTaging;
    }

    public void setValidationChecker(final ValidationChecker mValidationChecker) {
        this.mValidationChecker = mValidationChecker;
    }

    public void unhighlightTag(int i) {
        final Editable text = this.getText();
        if (this.mOnTaging) {
            if (text.length() <= i) {
                this.notifyTagModeChanged(false);
                return;
            }
            final char char1 = text.charAt(i);
            boolean b = false;
            if (this.isSpecialChar(char1)) {
                b = true;
            }
            if (!this.checkLeft(text, i) || b) {
                final int right = this.getRight(text, i + 1);
                if (i == right) {
                    this.notifyTagModeChanged(false);
                    return;
                }
                final CharacterStyle[] array = (CharacterStyle[])text.getSpans(i, right, (Class)ForegroundColorSpan.class);
                for (int j = 0; j < array.length; ++j) {
                    text.removeSpan((Object)array[j]);
                }
                CharacterStyle[] array2;
                for (array2 = (CharacterStyle[])text.getSpans(i, right, (Class)StyleSpan.class), i = 0; i < array2.length; ++i) {
                    text.removeSpan((Object)array2[i]);
                }
                this.notifyTagModeChanged(false);
            }
        }
    }

    public interface OnBackKeyDownPreIme
    {
        boolean onBackKeyDownPreIme(int p0, KeyEvent p1);
    }

    public interface OnHighlightTextClick
    {
        void onClick(String p0, int p1, int p2);
    }

    public interface OnMentionKeyListener
    {
        void onMentionCharRemoved();

        void onMentionKey();

        void onWhitespaceKey();
    }

    public interface OnTagModeChangeListener
    {
        void onTagInserting(String p0);

        void onTagModeChanged(boolean p0);
    }

    public interface ValidationChecker
    {
        void onMaxTagLen();

        void onMaxTextLen();
    }
}