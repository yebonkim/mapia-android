package com.mapia.camera.ui;

/**
 * Created by daehyun on 15. 6. 16..
 */

public interface FocusIndicator
{
    void clear();

    void showFail(boolean p0);

    void showStart();

    void showStart(boolean p0);

    void showSuccess(boolean p0);
}