package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;

import com.mapia.R;

public class CountDownTimerPreference extends ListPreference
{
    private static final int[] DURATIONS;

    static {
        DURATIONS = new int[] { 0, 2, 5 };
    }

    public CountDownTimerPreference(final Context context, final AttributeSet set) {
        super(context, set);
        this.initCountDownDurationChoices(context);
    }

    private void initCountDownDurationChoices(final Context context) {
        final CharSequence[] entryValues = new CharSequence[CountDownTimerPreference.DURATIONS.length];
        final CharSequence[] entries = new CharSequence[CountDownTimerPreference.DURATIONS.length];
        for (int i = 0; i < CountDownTimerPreference.DURATIONS.length; ++i) {
            entryValues[i] = Integer.toString(CountDownTimerPreference.DURATIONS[i]);
            if (i == 0) {
                entries[0] = context.getString(R.string.setting_off);
            }
            else {
                entries[i] = context.getResources().getQuantityString(R.plurals.pref_camera_timer_entry, i, new Object[] { i });
            }
        }
        this.setEntries(entries);
        this.setEntryValues(entryValues);
    }
}