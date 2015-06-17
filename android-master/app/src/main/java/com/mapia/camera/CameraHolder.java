package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CameraHolder {
    private static final boolean DEBUG_OPEN_RELEASE = true;
    private static final int KEEP_CAMERA_TIMEOUT = 3000;
    private static final int RELEASE_CAMERA = 1;
    private static final String TAG = "CameraHolder";
    private static com.mapia.camera.CameraManager.CameraProxy[] mMockCamera;
    private static Camera.CameraInfo[] mMockCameraInfo;
    private static SimpleDateFormat sDateFormat;
    private static CameraHolder sHolder;
    private static ArrayList<OpenReleaseState> sOpenReleaseStates;
    private int mBackCameraId;
    private com.mapia.camera.CameraManager.CameraProxy mCameraDevice;
    private int mCameraId;
    private boolean mCameraOpened;
    private int mFrontCameraId;
    private final Handler mHandler;
    private final Camera.CameraInfo[] mInfo;
    private long mKeepBeforeTime;
    private final int mNumberOfCameras;
    private Camera.Parameters mParameters;

    static {
        CameraHolder.sOpenReleaseStates = new ArrayList<OpenReleaseState>();
        CameraHolder.sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    private CameraHolder() {
        super();
        this.mCameraId = -1;
        this.mBackCameraId = -1;
        this.mFrontCameraId = -1;
        final HandlerThread handlerThread = new HandlerThread("CameraHolder");
        handlerThread.start();
        this.mHandler = new MyHandler(handlerThread.getLooper());
        if (CameraHolder.mMockCameraInfo != null) {
            this.mNumberOfCameras = CameraHolder.mMockCameraInfo.length;
            this.mInfo = CameraHolder.mMockCameraInfo;
        } else {
            this.mNumberOfCameras = Camera.getNumberOfCameras();
            this.mInfo = new Camera.CameraInfo[this.mNumberOfCameras];
            for (int i = 0; i < this.mNumberOfCameras; ++i) {
                Camera.getCameraInfo(i, this.mInfo[i] = new Camera.CameraInfo());
            }
        }
        for (int j = 0; j < this.mNumberOfCameras; ++j) {
            if (this.mBackCameraId == -1 && this.mInfo[j].facing == 0) {
                this.mBackCameraId = j;
            } else if (this.mFrontCameraId == -1 && this.mInfo[j].facing == 1) {
                this.mFrontCameraId = j;
            }
        }
    }

    private static void collectState(int i, final com.mapia.camera.CameraManager.CameraProxy cameraProxy) {
        final OpenReleaseState openReleaseState;
        final String[] stack;
        synchronized (CameraHolder.class) {
            openReleaseState = new OpenReleaseState();
            openReleaseState.time = System.currentTimeMillis();
            openReleaseState.id = i;
            if (cameraProxy == null) {
                openReleaseState.device = "(null)";
            } else {
                openReleaseState.device = cameraProxy.toString();
            }
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            stack = new String[stackTrace.length];
            for (i = 0; i < stackTrace.length; ++i) {
                stack[i] = stackTrace[i].toString();
            }
        }
        openReleaseState.stack = stack;
        if (CameraHolder.sOpenReleaseStates.size() > 10) {
            CameraHolder.sOpenReleaseStates.remove(0);
        }
        CameraHolder.sOpenReleaseStates.add(openReleaseState);
        // monitorexit(CameraHolder.class)
    }
    // monitorexit(CameraHolder.class)

    private static void dumpStates() {
        synchronized (CameraHolder.class) {
            for (int i = CameraHolder.sOpenReleaseStates.size() - 1; i >= 0; --i) {
                final OpenReleaseState openReleaseState = CameraHolder.sOpenReleaseStates.get(i);
                CameraHolder.sDateFormat.format(new Date(openReleaseState.time));
                for (int j = 0; j < openReleaseState.stack.length; ++j) {
                }
            }
        }
    }

    public static void injectMockCamera(final Camera.CameraInfo[] mMockCameraInfo, final com.mapia.camera.CameraManager.CameraProxy[] mMockCamera) {
        CameraHolder.mMockCameraInfo = mMockCameraInfo;
        CameraHolder.mMockCamera = mMockCamera;
        CameraHolder.sHolder = new CameraHolder();
    }

    public static CameraHolder instance() {
        synchronized (CameraHolder.class) {
            if (CameraHolder.sHolder == null) {
                CameraHolder.sHolder = new CameraHolder();
            }
            return CameraHolder.sHolder;
        }
    }

    public int getBackCameraId() {
        return this.mBackCameraId;
    }

    public Camera.CameraInfo[] getCameraInfo() {
        return this.mInfo;
    }

    public int getFrontCameraId() {
        return this.mFrontCameraId;
    }

    public int getNumberOfCameras() {
        return this.mNumberOfCameras;
    }

    public void keep() {
        this.keep(3000);
    }

    public void keep(final int n) {
        synchronized (this) {
            this.mKeepBeforeTime = System.currentTimeMillis() + n;
        }
    }

    public com.mapia.camera.CameraManager.CameraProxy open(final int mCameraId) throws CameraHardwareException {


        collectState(mCameraId, this.mCameraDevice);
        if (this.mCameraOpened) {
            Log.e("CameraHolder", "double open");
            dumpStates();
        }
        if (this.mCameraOpened) {
//            Util.Assert(b);
            if (this.mCameraDevice != null && this.mCameraId != mCameraId) {
                this.mCameraDevice.release();
                this.mCameraDevice = null;
                this.mCameraId = -1;
            }
            if (this.mCameraDevice == null) {

                try {
                    Log.v("CameraHolder", "open camera " + mCameraId);
                    if (CameraHolder.mMockCameraInfo == null) {
                        this.mCameraDevice = com.mapia.camera.CameraManager.instance().cameraOpen(mCameraId);
                        this.mCameraId = mCameraId;
                        this.mParameters = this.mCameraDevice.getParameters();
                        this.mCameraOpened = true;
                        this.mHandler.removeMessages(1);
                        this.mKeepBeforeTime = 0L;
                        return this.mCameraDevice;
                    }
                    if (CameraHolder.mMockCamera == null) {
                        throw new RuntimeException();
                    }
                } catch (RuntimeException ex) {
                    Log.e("CameraHolder", "fail to connect Camera", (Throwable) ex);
                    throw new CameraHardwareException(ex);
                }
            }

            this.mCameraDevice = CameraHolder.mMockCamera[mCameraId];
            return this.mCameraDevice;
        }


        try

        {
            this.mCameraDevice.reconnect();
            this.mCameraDevice.setParameters(this.mParameters);
            return this.mCameraDevice;
        } catch (IOException ex2) {
            Log.e("CameraHolder", "reconnect failed.");
            throw new CameraHardwareException(ex2);
        }
    }

    public void release() {
        while (true) {
            Label_0080:
            {
                synchronized (this) {
                    collectState(this.mCameraId, this.mCameraDevice);
                    if (this.mCameraDevice != null) {
                        final long currentTimeMillis = System.currentTimeMillis();
                        if (currentTimeMillis >= this.mKeepBeforeTime) {
                            break Label_0080;
                        }
                        if (this.mCameraOpened) {
                            this.mCameraOpened = false;
                            this.mCameraDevice.stopPreview();
                        }
                        this.mHandler.sendEmptyMessageDelayed(1, this.mKeepBeforeTime - currentTimeMillis);
                    }
                    return;
                }
            }
            this.mCameraOpened = false;
            this.mCameraDevice.release();
            this.mCameraDevice = null;
            this.mParameters = null;
            this.mCameraId = -1;
        }
    }

    public com.mapia.camera.CameraManager.CameraProxy tryOpen(final int n) {
        final CameraManager.CameraProxy cameraProxy = null;
        // monitorenter(this)
        // monitorenter(this)
        CameraManager.CameraProxy open = cameraProxy;
        try {
            if (!this.mCameraOpened) {
                open = this.open(n);
            }
            return open;
        } catch (CameraHardwareException ex) {
            open = cameraProxy;
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException(ex);
            }
            return open;
        } finally {
        }
        // monitorexit(this)
    }

    private class MyHandler extends Handler {
        MyHandler(final Looper looper) {
            super(looper);
        }

        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                }
                case 1: {
                    synchronized (CameraHolder.this) {
                        if (!CameraHolder.this.mCameraOpened) {
                            CameraHolder.this.release();
                        }
                        return;
                    }
                }
            }
        }
    }

    private static class OpenReleaseState {
        String device;
        int id;
        String[] stack;
        long time;
    }
}