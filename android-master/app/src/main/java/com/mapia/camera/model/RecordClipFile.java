package com.mapia.camera.model;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class RecordClipFile
{
    private int duration;
    private boolean facingBack;
    private String path;

    public RecordClipFile() {
        super();
        this.path = null;
        this.duration = 0;
        this.facingBack = true;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getPath() {
        return this.path;
    }

    public boolean isFacingBack() {
        return this.facingBack;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public void setFacingBack(final boolean facingBack) {
        this.facingBack = facingBack;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}