package com.mapia.camera.viewmodel.data;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class Size
{
    public final int height;
    public final int width;

    public Size(final int width, final int height) {
        super();
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("width = %d, height = %d", this.width, this.height);
    }
}