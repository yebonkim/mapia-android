package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.mapia.camera.util.IntArray;

import java.util.List;


public class IconListPreference extends com.mapia.camera.ListPreference
{
    private int[] mIconIds;
    private int[] mImageIds;
    private int[] mLargeIconIds;
    private int mSingleIconId;
    private boolean mUseSingleIcon;

    public IconListPreference(final Context context, final AttributeSet set) {
        super(context, set);
//        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.IconListPreference, 0, 0);
//        final Resources resources = context.getResources();
//        this.mSingleIconId = obtainStyledAttributes.getResourceId(1, 0);
//        this.mIconIds = this.getIds(resources, obtainStyledAttributes.getResourceId(0, 0));
//        this.mLargeIconIds = this.getIds(resources, obtainStyledAttributes.getResourceId(2, 0));
//        this.mImageIds = this.getIds(resources, obtainStyledAttributes.getResourceId(3, 0));
//        obtainStyledAttributes.recycle();
    }

    private int[] getIds(final Resources resources, int i) {
        if (i == 0) {
            return null;
        }
        final TypedArray obtainTypedArray = resources.obtainTypedArray(i);
        final int length = obtainTypedArray.length();
        final int[] array = new int[length];
        for (i = 0; i < length; ++i) {
            array[i] = obtainTypedArray.getResourceId(i, 0);
        }
        obtainTypedArray.recycle();
        return array;
    }

    @Override
    public void filterUnsupported(List<String> list) {
        final CharSequence[] entryValues = this.getEntryValues();
        final IntArray intArray = new IntArray();
        final IntArray intArray2 = new IntArray();
        final IntArray intArray3 = new IntArray();
        for (int i = 0; i < entryValues.length; ++i) {
            if (list.indexOf(entryValues[i].toString()) >= 0) {
                if (this.mIconIds != null) {
                    intArray.add(this.mIconIds[i]);
                }
                if (this.mLargeIconIds != null) {
                    intArray2.add(this.mLargeIconIds[i]);
                }
                if (this.mImageIds != null) {
                    intArray3.add(this.mImageIds[i]);
                }
            }
        }
        if (this.mIconIds != null) {
            this.mIconIds = intArray.toArray(new int[intArray.size()]);
        }
        if (this.mLargeIconIds != null) {
            this.mLargeIconIds = intArray2.toArray(new int[intArray2.size()]);
        }
        if (this.mImageIds != null) {
            this.mImageIds = intArray3.toArray(new int[intArray3.size()]);
        }

        super.filterUnsupported(list);
    }

    public int[] getIconIds() {
        return this.mIconIds;
    }

    public int[] getImageIds() {
        return this.mImageIds;
    }

    public int[] getLargeIconIds() {
        return this.mLargeIconIds;
    }

    public int getSingleIcon() {
        return this.mSingleIconId;
    }

    public boolean getUseSingleIcon() {
        return this.mUseSingleIcon;
    }

    public void setIconIds(final int[] mIconIds) {
        this.mIconIds = mIconIds;
    }

    public void setLargeIconIds(final int[] mLargeIconIds) {
        this.mLargeIconIds = mLargeIconIds;
    }

    public void setUseSingleIcon(final boolean mUseSingleIcon) {
        this.mUseSingleIcon = mUseSingleIcon;
    }
}