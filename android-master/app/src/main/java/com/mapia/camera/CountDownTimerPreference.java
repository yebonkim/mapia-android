package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;

import com.mapia.R;

/* CountDownTimerPreference generates entries (i.e. what users see in the UI),
 * and entry values (the actual value recorded in preference) in
 * initCountDownTimeChoices(Context context), rather than reading the entries
 * from a predefined list. When the entry values are a continuous list of numbers,
 * (e.g. 0-60), it is more efficient to auto generate the list than to predefine it.*/
public class CountDownTimerPreference extends ListPreference {
    private final static int MAX_DURATION = 60;
    public CountDownTimerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCountDownDurationChoices(context);
    }

    private void initCountDownDurationChoices(Context context) {
        CharSequence[] entryValues = new CharSequence[MAX_DURATION + 1];
        CharSequence[] entries = new CharSequence[MAX_DURATION + 1];
        for (int i = 0; i <= MAX_DURATION; i++) {
            entryValues[i] = Integer.toString(i);
            if (i == 0) {
                entries[0] = context.getString(R.string.setting_off); // Off
            } else {
                entries[i] = context.getResources()
                        .getQuantityString(R.plurals.pref_camera_timer_entry, i, i);
            }
        }
        setEntries(entries);
        setEntryValues(entryValues);
    }
}
