package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListPreference extends CameraPreference
{
    private static final String TAG = "ListPreference";
//    private final CharSequence[] mDefaultValues;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;
//    private final String mKey;
    private CharSequence[] mLabels;
    private boolean mLoaded;
    private String mValue;

    public ListPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.mLoaded = false;
//        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ListPreference, 0, 0);
//        this.mKey = Util.checkNotNull(obtainStyledAttributes.getString(0));
//        final TypedValue peekValue = obtainStyledAttributes.peekValue(1);
//        if (peekValue != null && peekValue.type == 1) {
//            this.mDefaultValues = obtainStyledAttributes.getTextArray(1);
//        }
//        else {
//            (this.mDefaultValues = new CharSequence[1])[0] = obtainStyledAttributes.getString(1);
//        }
//        this.setEntries(obtainStyledAttributes.getTextArray(3));
//        this.setEntryValues(obtainStyledAttributes.getTextArray(2));
//        this.setLabels(obtainStyledAttributes.getTextArray(4));
//        obtainStyledAttributes.recycle();
    }

    private String findSupportedDefaultValue() {
//        for (int i = 0; i < this.mDefaultValues.length; ++i) {
//            for (int j = 0; j < this.mEntryValues.length; ++j) {
//                if (this.mEntryValues[j].equals(this.mDefaultValues[i])) {
//                    return this.mDefaultValues[i].toString();
//                }
//            }
//        }
        return null;
    }

    public void filterDuplicated() {
        final ArrayList<CharSequence> list = new ArrayList<CharSequence>();
        final ArrayList<CharSequence> list2 = new ArrayList<CharSequence>();
        for (int i = 0; i < this.mEntryValues.length; ++i) {
            if (!list.contains(this.mEntries[i])) {
                list.add(this.mEntries[i]);
                list2.add(this.mEntryValues[i]);
            }
        }
        final int size = list.size();
        this.mEntries = list.toArray(new CharSequence[size]);
        this.mEntryValues = list2.toArray(new CharSequence[size]);
    }

    public void filterUnsupported(List<String> list) {
        final ArrayList<CharSequence> list2 = new ArrayList<CharSequence>();
        final ArrayList<CharSequence> list3 = new ArrayList<CharSequence>();
        for (int i = 0; i < this.mEntryValues.length; ++i) {
            if (list.indexOf(this.mEntryValues[i].toString()) >= 0) {
                list2.add(this.mEntries[i]);
                list3.add(this.mEntryValues[i]);
            }
        }
        final int size = list2.size();
        this.mEntries = list2.toArray(new CharSequence[size]);
        this.mEntryValues = list3.toArray(new CharSequence[size]);
    }

    public int findIndexOfValue(final String s) {
        for (int i = 0; i < this.mEntryValues.length; ++i) {
            if (Util.equals(this.mEntryValues[i], s)) {
                return i;
            }
        }
        return -1;
    }

    public int getCurrentIndex() {
        return this.findIndexOfValue(this.getValue());
    }

    public CharSequence[] getEntries() {
        return this.mEntries;
    }

    public String getEntry() {
        return this.mEntries[this.findIndexOfValue(this.getValue())].toString();
    }

    public CharSequence[] getEntryValues() {
        return this.mEntryValues;
    }

    public String getKey() {
//        return this.mKey;
        return "";
    }

    public String getLabel() {
        return this.mLabels[this.findIndexOfValue(this.getValue())].toString();
    }

    public CharSequence[] getLabels() {
        return this.mLabels;
    }

    public String getValue() {
        if (!this.mLoaded) {
//            this.mValue = this.getSharedPreferences().getString(this.mKey, this.findSupportedDefaultValue());
            this.mLoaded = true;
        }
        return this.mValue;
    }

    protected void persistStringValue(final String s) {
        final SharedPreferences.Editor edit = this.getSharedPreferences().edit();
//        edit.putString(this.mKey, s);
        edit.apply();
    }

    public void print() {
        Log.v("ListPreference", "Preference key=" + this.getKey() + ". value=" + this.getValue());
        for (int i = 0; i < this.mEntryValues.length; ++i) {
            Log.v("ListPreference", "entryValues[" + i + "]=" + (Object)this.mEntryValues[i]);
        }
    }

    @Override
    public void reloadValue() {
        this.mLoaded = false;
    }

    public void setEntries(final CharSequence[] array) {
        CharSequence[] mEntries = array;
        if (array == null) {
            mEntries = new CharSequence[0];
        }
        this.mEntries = mEntries;
    }

    public void setEntryValues(final CharSequence[] array) {
        CharSequence[] mEntryValues = array;
        if (array == null) {
            mEntryValues = new CharSequence[0];
        }
        this.mEntryValues = mEntryValues;
    }

    public void setLabels(final CharSequence[] array) {
        CharSequence[] mLabels = array;
        if (array == null) {
            mLabels = new CharSequence[0];
        }
        this.mLabels = mLabels;
    }

    public void setValue(final String mValue) {
        if (this.findIndexOfValue(mValue) < 0) {
            throw new IllegalArgumentException();
        }
        this.persistStringValue(this.mValue = mValue);
    }

    public void setValueIndex(final int n) {
        this.setValue(this.mEntryValues[n].toString());
    }
}