package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Iterator;

public class PreferenceGroup extends CameraPreference
{
    private ArrayList<CameraPreference> list;

    public PreferenceGroup(final Context context, final AttributeSet set) {
        super(context, set);
        this.list = new ArrayList<CameraPreference>();
    }

    public void addChild(final CameraPreference cameraPreference) {
        this.list.add(cameraPreference);
    }

    public ListPreference findPreference(final String s) {
        for (final CameraPreference cameraPreference : this.list) {
            if (cameraPreference instanceof ListPreference) {
                final ListPreference listPreference = (ListPreference)cameraPreference;
                if (listPreference.getKey().equals(s)) {
                    return listPreference;
                }
                continue;
            }
            else {
                if (!(cameraPreference instanceof PreferenceGroup)) {
                    continue;
                }
                final ListPreference preference = ((PreferenceGroup)cameraPreference).findPreference(s);
                if (preference != null) {
                    return preference;
                }
                continue;
            }
        }
        return null;
    }

    public CameraPreference get(final int n) {
        return this.list.get(n);
    }

    @Override
    public void reloadValue() {
        final Iterator<CameraPreference> iterator = this.list.iterator();
        while (iterator.hasNext()) {
            iterator.next().reloadValue();
        }
    }

    public void removePreference(final int n) {
        this.list.remove(n);
    }

    public int size() {
        return this.list.size();
    }
}