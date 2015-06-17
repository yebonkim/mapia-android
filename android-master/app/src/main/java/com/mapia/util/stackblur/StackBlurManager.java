package com.mapia.util.stackblur;

import android.graphics.Bitmap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by daehyun on 15. 6. 8..
 */
public class StackBlurManager
{
    static final ExecutorService EXECUTOR;
    static final int EXECUTOR_THREADS;
    private static volatile boolean hasRS;
    private final BlurProcess _blurProcess;
    private final Bitmap _image;
    private Bitmap _result;

    static {
        EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
        EXECUTOR = Executors.newFixedThreadPool(StackBlurManager.EXECUTOR_THREADS);
        StackBlurManager.hasRS = true;
    }

    public StackBlurManager(final Bitmap image) {
        super();
        this._image = image;
        this._blurProcess = new JavaBlurProcess();
    }

    public Bitmap process(final int n) {
        return this._result = this._blurProcess.blur(this._image, n);
    }
}