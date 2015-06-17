package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class PieItem
{
    private static final float DISABLED_ALPHA = 0.3f;
    private static final float ENABLED_ALPHA = 1.0f;
    private int level;
    private float mAlpha;
    private boolean mChangeAlphaWhenDisabled;
    private Drawable mDrawable;
    private boolean mEnabled;
    private List<PieItem> mItems;
    private CharSequence mLabel;
    private OnClickListener mOnClickListener;
    private Path mPath;
    private boolean mSelected;

    public PieItem(final Drawable mDrawable, final int level) {
        super();
        this.mChangeAlphaWhenDisabled = true;
        this.mDrawable = mDrawable;
        this.level = level;
        if (mDrawable != null) {
            this.setAlpha(1.0f);
        }
        this.mEnabled = true;
    }

    public void addItem(final PieItem pieItem) {
        if (this.mItems == null) {
            this.mItems = new ArrayList<PieItem>();
        }
        this.mItems.add(pieItem);
    }

    public void clearItems() {
        this.mItems = null;
    }

    public void draw(final Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    public List<PieItem> getItems() {
        return this.mItems;
    }

    public CharSequence getLabel() {
        return this.mLabel;
    }

    public int getLevel() {
        return this.level;
    }

    public Path getPath() {
        return this.mPath;
    }

    public boolean hasItems() {
        return this.mItems != null;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isSelected() {
        return this.mSelected;
    }

    public void performClick() {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onClick(this);
        }
    }

    public void setAlpha(final float mAlpha) {
        this.mAlpha = mAlpha;
        this.mDrawable.setAlpha((int)(255.0f * mAlpha));
    }

    public void setBounds(final int n, final int n2, final int n3, final int n4) {
        this.mDrawable.setBounds(n, n2, n3, n4);
    }

    public void setChangeAlphaWhenDisabled(final boolean mChangeAlphaWhenDisabled) {
        this.mChangeAlphaWhenDisabled = mChangeAlphaWhenDisabled;
    }

    public void setEnabled(final boolean mEnabled) {
        this.mEnabled = mEnabled;
        if (this.mChangeAlphaWhenDisabled) {
            if (!this.mEnabled) {
                this.setAlpha(0.3f);
                return;
            }
            this.setAlpha(1.0f);
        }
    }

    public void setImageResource(final Context context, final int n) {
        final Drawable mutate = context.getResources().getDrawable(n).mutate();
        mutate.setBounds(this.mDrawable.getBounds());
        this.mDrawable = mutate;
        this.setAlpha(this.mAlpha);
    }

    public void setLabel(final CharSequence mLabel) {
        this.mLabel = mLabel;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public void setOnClickListener(final OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public void setPath(final Path mPath) {
        this.mPath = mPath;
    }

    public void setSelected(final boolean mSelected) {
        this.mSelected = mSelected;
    }

    public interface OnClickListener
    {
        void onClick(PieItem p0);
    }
}