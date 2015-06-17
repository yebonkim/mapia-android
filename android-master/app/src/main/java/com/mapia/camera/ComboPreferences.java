package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mapia.camera.util.UsageStatistics;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class ComboPreferences implements SharedPreferences, SharedPreferences.OnSharedPreferenceChangeListener
{
    private static WeakHashMap<Context, ComboPreferences> sMap;
    private CopyOnWriteArrayList<OnSharedPreferenceChangeListener> mListeners;
    private String mPackageName;
    private SharedPreferences mPrefGlobal;
    private SharedPreferences mPrefLocal;

    static {
        ComboPreferences.sMap = new WeakHashMap<Context, ComboPreferences>();
    }

    public ComboPreferences(final Context context) {
        super();
        this.mPackageName = context.getPackageName();
        (this.mPrefGlobal = context.getSharedPreferences(getGlobalSharedPreferencesName(context), 0)).registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener)this);
        synchronized (ComboPreferences.sMap) {
            ComboPreferences.sMap.put(context, this);
            // monitorexit(ComboPreferences.sMap)
            this.mListeners = new CopyOnWriteArrayList<SharedPreferences.OnSharedPreferenceChangeListener>();
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (!this.mPrefGlobal.contains("pref_version_key") && defaultSharedPreferences.contains("pref_version_key")) {
                this.moveGlobalPrefsFrom(defaultSharedPreferences);
            }
        }
    }

    public static ComboPreferences get(final Context context) {
        synchronized (ComboPreferences.sMap) {
            return ComboPreferences.sMap.get(context);
        }
    }

    private static String getGlobalSharedPreferencesName(final Context context) {
        return context.getPackageName() + "_preferences_camera";
    }

    private static String getLocalSharedPreferencesName(final Context context, final int n) {
        return context.getPackageName() + "_preferences_" + n;
    }

    public static String[] getSharedPreferencesNames(final Context context) {
        final int numberOfCameras = CameraHolder.instance().getNumberOfCameras();
        final String[] array = new String[numberOfCameras + 1];
        array[0] = getGlobalSharedPreferencesName(context);
        for (int i = 0; i < numberOfCameras; ++i) {
            array[i + 1] = getLocalSharedPreferencesName(context, i);
        }
        return array;
    }

    private static boolean isGlobal(final String s) {
        return s.equals("pref_video_time_lapse_frame_interval_key") || s.equals("pref_camera_id_key") || s.equals("pref_camera_recordlocation_key") || s.equals("pref_camera_first_use_hint_shown_key") || s.equals("pref_video_first_use_hint_shown_key") || s.equals("pref_camera_timer_key") || s.equals("pref_camera_timer_sound_key") || s.equals("pref_photosphere_picturesize_key");
    }

    private void moveGlobalPrefsFrom(final SharedPreferences sharedPreferences) {
        final Map all = sharedPreferences.getAll();
        this.movePrefFrom(all, "pref_version_key", sharedPreferences);
        this.movePrefFrom(all, "pref_video_time_lapse_frame_interval_key", sharedPreferences);
        this.movePrefFrom(all, "pref_camera_id_key", sharedPreferences);
        this.movePrefFrom(all, "pref_camera_recordlocation_key", sharedPreferences);
        this.movePrefFrom(all, "pref_camera_first_use_hint_shown_key", sharedPreferences);
        this.movePrefFrom(all, "pref_video_first_use_hint_shown_key", sharedPreferences);
    }

    private void movePrefFrom(final Map<String, ?> map, final String s, final SharedPreferences sharedPreferences) {
        if (map.containsKey(s)) {
            final Object value = map.get(s);
            if (value instanceof String) {
                this.mPrefGlobal.edit().putString(s, (String)value).apply();
            }
            else if (value instanceof Integer) {
                this.mPrefGlobal.edit().putInt(s, (int)value).apply();
            }
            else if (value instanceof Long) {
                this.mPrefGlobal.edit().putLong(s, (long)value).apply();
            }
            else if (value instanceof Float) {
                this.mPrefGlobal.edit().putFloat(s, (float)value).apply();
            }
            else if (value instanceof Boolean) {
                this.mPrefGlobal.edit().putBoolean(s, (boolean)value).apply();
            }
            sharedPreferences.edit().remove(s).apply();
        }
    }

    public boolean contains(final String s) {
        return this.mPrefLocal.contains(s) || this.mPrefGlobal.contains(s);
    }

    public SharedPreferences.Editor edit() {
        return (SharedPreferences.Editor)new MyEditor();
    }

    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }

    public boolean getBoolean(final String s, final boolean b) {
        if (isGlobal(s) || !this.mPrefLocal.contains(s)) {
            return this.mPrefGlobal.getBoolean(s, b);
        }
        return this.mPrefLocal.getBoolean(s, b);
    }

    public float getFloat(final String s, final float n) {
        if (isGlobal(s) || !this.mPrefLocal.contains(s)) {
            return this.mPrefGlobal.getFloat(s, n);
        }
        return this.mPrefLocal.getFloat(s, n);
    }

    public SharedPreferences getGlobal() {
        return this.mPrefGlobal;
    }

    public int getInt(final String s, final int n) {
        if (isGlobal(s) || !this.mPrefLocal.contains(s)) {
            return this.mPrefGlobal.getInt(s, n);
        }
        return this.mPrefLocal.getInt(s, n);
    }

    public SharedPreferences getLocal() {
        return this.mPrefLocal;
    }

    public long getLong(final String s, final long n) {
        if (isGlobal(s) || !this.mPrefLocal.contains(s)) {
            return this.mPrefGlobal.getLong(s, n);
        }
        return this.mPrefLocal.getLong(s, n);
    }

    public String getString(final String s, final String s2) {
        if (isGlobal(s) || !this.mPrefLocal.contains(s)) {
            return this.mPrefGlobal.getString(s, s2);
        }
        return this.mPrefLocal.getString(s, s2);
    }

    public Set<String> getStringSet(final String s, final Set<String> set) {
        throw new UnsupportedOperationException();
    }

    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        final Iterator<OnSharedPreferenceChangeListener> iterator = this.mListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onSharedPreferenceChanged((SharedPreferences)this, s);
        }
        BackupManager.dataChanged(this.mPackageName);
        UsageStatistics.onEvent("CameraSettingsChange", null, s);
    }

    public void registerOnSharedPreferenceChangeListener(final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesOnSharedPreferenceChangeListener) {
        this.mListeners.add(sharedPreferencesOnSharedPreferenceChangeListener);
    }

    public void setLocalId(final Context context, final int n) {
        final String localSharedPreferencesName = getLocalSharedPreferencesName(context, n);
        if (this.mPrefLocal != null) {
            this.mPrefLocal.unregisterOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener)this);
        }
        (this.mPrefLocal = context.getSharedPreferences(localSharedPreferencesName, 0)).registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener)this);
    }

    public void unregisterOnSharedPreferenceChangeListener(final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesOnSharedPreferenceChangeListener) {
        this.mListeners.remove(sharedPreferencesOnSharedPreferenceChangeListener);
    }

    private class MyEditor implements SharedPreferences.Editor
    {
        private SharedPreferences.Editor mEditorGlobal;
        private SharedPreferences.Editor mEditorLocal;

        MyEditor() {
            super();
            this.mEditorGlobal = ComboPreferences.this.mPrefGlobal.edit();
            this.mEditorLocal = ComboPreferences.this.mPrefLocal.edit();
        }

        public void apply() {
            this.mEditorGlobal.apply();
            this.mEditorLocal.apply();
        }

        public SharedPreferences.Editor clear() {
            this.mEditorGlobal.clear();
            this.mEditorLocal.clear();
            return (SharedPreferences.Editor)this;
        }

        public boolean commit() {
            final boolean commit = this.mEditorGlobal.commit();
            final boolean commit2 = this.mEditorLocal.commit();
            return commit && commit2;
        }

        public SharedPreferences.Editor putBoolean(final String s, final boolean b) {
            if (isGlobal(s)) {
                this.mEditorGlobal.putBoolean(s, b);
                return (SharedPreferences.Editor)this;
            }
            this.mEditorLocal.putBoolean(s, b);
            return (SharedPreferences.Editor)this;
        }

        public SharedPreferences.Editor putFloat(final String s, final float n) {
            if (isGlobal(s)) {
                this.mEditorGlobal.putFloat(s, n);
                return (SharedPreferences.Editor)this;
            }
            this.mEditorLocal.putFloat(s, n);
            return (SharedPreferences.Editor)this;
        }

        public SharedPreferences.Editor putInt(final String s, final int n) {
            if (isGlobal(s)) {
                this.mEditorGlobal.putInt(s, n);
                return (SharedPreferences.Editor)this;
            }
            this.mEditorLocal.putInt(s, n);
            return (SharedPreferences.Editor)this;
        }

        public SharedPreferences.Editor putLong(final String s, final long n) {
            if (isGlobal(s)) {
                this.mEditorGlobal.putLong(s, n);
                return (SharedPreferences.Editor)this;
            }
            this.mEditorLocal.putLong(s, n);
            return (SharedPreferences.Editor)this;
        }

        public SharedPreferences.Editor putString(final String s, final String s2) {
            if (isGlobal(s)) {
                this.mEditorGlobal.putString(s, s2);
                return (SharedPreferences.Editor)this;
            }
            this.mEditorLocal.putString(s, s2);
            return (SharedPreferences.Editor)this;
        }

        public SharedPreferences.Editor putStringSet(final String s, final Set<String> set) {
            throw new UnsupportedOperationException();
        }

        public SharedPreferences.Editor remove(final String s) {
            this.mEditorGlobal.remove(s);
            this.mEditorLocal.remove(s);
            return (SharedPreferences.Editor)this;
        }
    }
}