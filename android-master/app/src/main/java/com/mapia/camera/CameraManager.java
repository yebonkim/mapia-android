package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;


public class CameraManager
{
    private static final int ADD_CALLBACK_BUFFER = 9;
    private static final int AUTO_FOCUS = 10;
    private static final int CANCEL_AUTO_FOCUS = 11;
    private static final int ENABLE_SHUTTER_SOUND = 23;
    private static final int GET_PARAMETERS = 20;
    private static final int LOCK = 4;
    private static final int RECONNECT = 2;
    private static final int REFRESH_PARAMETERS = 24;
    private static final int RELEASE = 1;
    private static final int SET_AUTO_FOCUS_MOVE_CALLBACK = 12;
    private static final int SET_DISPLAY_ORIENTATION = 13;
    private static final int SET_ERROR_CALLBACK = 18;
    private static final int SET_FACE_DETECTION_LISTENER = 15;
    private static final int SET_PARAMETERS = 19;
    private static final int SET_PREVIEW_CALLBACK = 22;
    private static final int SET_PREVIEW_CALLBACK_WITH_BUFFER = 8;
    private static final int SET_PREVIEW_DISPLAY_ASYNC = 21;
    private static final int SET_PREVIEW_TEXTURE_ASYNC = 5;
    private static final int SET_ZOOM_CHANGE_LISTENER = 14;
    private static final int START_FACE_DETECTION = 16;
    private static final int START_PREVIEW_ASYNC = 6;
    private static final int STOP_FACE_DETECTION = 17;
    private static final int STOP_PREVIEW = 7;
    private static final String TAG = "CameraManager";
    private static final int UNLOCK = 3;
    private static CameraManager sCameraManager;
    private Camera mCamera;
    private Handler mCameraHandler;
    private Camera.Parameters mParameters;
    private boolean mParametersIsDirty;
    private Camera.Parameters mParamsToSet;
    private IOException mReconnectIOException;

    static {
        CameraManager.sCameraManager = new CameraManager();
    }

    private CameraManager() {
        super();
        final HandlerThread handlerThread = new HandlerThread("Camera Handler Thread");
        handlerThread.start();
        this.mCameraHandler = new CameraHandler(handlerThread.getLooper());
    }

    public static CameraManager instance() {
        return CameraManager.sCameraManager;
    }

    @TargetApi(16)
    private void setAutoFocusMoveCallback(final Camera camera, final Object o) {
        camera.setAutoFocusMoveCallback((Camera.AutoFocusMoveCallback)o);
    }

    CameraProxy cameraOpen(final int n) {
        this.mCamera = Camera.open(n);
        if (this.mCamera != null) {
            this.mParametersIsDirty = true;
            if (this.mParamsToSet == null) {
                this.mParamsToSet = this.mCamera.getParameters();
            }
            return new CameraProxy();
        }
        return null;
    }

    private class CameraHandler extends Handler {
        CameraHandler(final Looper looper) {
            super(looper);
        }

        @TargetApi(17)
        private void enableShutterSound(final boolean b) {
            CameraManager.this.mCamera.enableShutterSound(b);
        }

        @TargetApi(14)
        private void setFaceDetectionListener(final Camera.FaceDetectionListener faceDetectionListener) {
            CameraManager.this.mCamera.setFaceDetectionListener(faceDetectionListener);
        }

        @TargetApi(11)
        private void setPreviewTexture(final Object o) {
            try {
                CameraManager.this.mCamera.setPreviewTexture((SurfaceTexture) o);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @TargetApi(14)
        private void startFaceDetection() {
            CameraManager.this.mCamera.startFaceDetection();
        }

        @TargetApi(14)
        private void stopFaceDetection() {
            CameraManager.this.mCamera.stopFaceDetection();
        }
    }

    public class CameraProxy
    {
        private CameraProxy() {
            super();
            Util.Assert(CameraManager.this.mCamera != null);
        }

        public void addCallbackBuffer(final byte[] array) {
            CameraManager.this.mCameraHandler.obtainMessage(9, (Object)array).sendToTarget();
        }

        public void autoFocus(final Camera.AutoFocusCallback autoFocusCallback) {
            CameraManager.this.mCameraHandler.obtainMessage(10, (Object)autoFocusCallback).sendToTarget();
        }

        public void cancelAutoFocus() {
            CameraManager.this.mCameraHandler.removeMessages(10);
            CameraManager.this.mCameraHandler.sendEmptyMessage(11);
        }

        public void enableShutterSound(final boolean b) {
            final Handler mHandler = CameraManager.this.mCameraHandler;
            boolean b2;
            if (b) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            mHandler.obtainMessage(23, (int) (b2 ? 1 : 0), 0).sendToTarget();
        }

        public Camera getCamera() {
            return CameraManager.this.mCamera;
        }

        public Camera.Parameters getParameters() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(20);
            this.waitDone();
            return CameraManager.this.mParameters;
        }

        public void lock() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(4);
        }

        public void reconnect() throws IOException {
            CameraManager.this.mCameraHandler.sendEmptyMessage(2);
            this.waitDone();
            if (CameraManager.this.mReconnectIOException != null) {
                throw CameraManager.this.mReconnectIOException;
            }
        }

        public void refreshParameters() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(24);
        }

        public void release() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(1);
            this.waitDone();
        }

        @TargetApi(16)
        public void setAutoFocusMoveCallback(final Camera.AutoFocusMoveCallback autoFocusMoveCallback) {
            CameraManager.this.mCameraHandler.obtainMessage(12, (Object)autoFocusMoveCallback).sendToTarget();
        }

        public void setDisplayOrientation(final int n) {
            CameraManager.this.mCameraHandler.obtainMessage(13, n, 0).sendToTarget();
        }

        public void setErrorCallback(final Camera.ErrorCallback errorCallback) {
            CameraManager.this.mCameraHandler.obtainMessage(18, (Object)errorCallback).sendToTarget();
        }

        @TargetApi(14)
        public void setFaceDetectionListener(final Camera.FaceDetectionListener faceDetectionListener) {
            CameraManager.this.mCameraHandler.obtainMessage(15, (Object)faceDetectionListener).sendToTarget();
        }

        public void setParameters(final Camera.Parameters parameters) {
            if (parameters == null) {
                Log.v("CameraManager", "null parameters in setParameters()");
                return;
            }
            CameraManager.this.mCameraHandler.obtainMessage(19, (Object)parameters.flatten()).sendToTarget();
        }

        public void setPreviewCallback(final Camera.PreviewCallback previewCallback) {
            CameraManager.this.mCameraHandler.obtainMessage(22, (Object)previewCallback).sendToTarget();
        }

        public void setPreviewCallbackWithBuffer(final Camera.PreviewCallback previewCallback) {
            CameraManager.this.mCameraHandler.obtainMessage(8, (Object)previewCallback).sendToTarget();
        }

        public void setPreviewDisplayAsync(final SurfaceHolder surfaceHolder) {
            CameraManager.this.mCameraHandler.obtainMessage(21, (Object)surfaceHolder).sendToTarget();
        }

        @TargetApi(11)
        public void setPreviewTextureAsync(final SurfaceTexture surfaceTexture) {
            CameraManager.this.mCameraHandler.obtainMessage(5, (Object)surfaceTexture).sendToTarget();
        }

        public void setZoomChangeListener(final Camera.OnZoomChangeListener onZoomChangeListener) {
            CameraManager.this.mCameraHandler.obtainMessage(14, (Object)onZoomChangeListener).sendToTarget();
        }

        public void startFaceDetection() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(16);
        }

        public void startPreviewAsync() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(6);
        }

        public void stopFaceDetection() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(17);
        }

        public void stopPreview() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(7);
            this.waitDone();
        }

        public void takePicture(final Camera.ShutterCallback shutterCallback, final Camera.PictureCallback pictureCallback, final Camera.PictureCallback pictureCallback2, final Camera.PictureCallback pictureCallback3) {
            CameraManager.this.mCameraHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    CameraManager.this.mCamera.takePicture(shutterCallback, pictureCallback, pictureCallback2, pictureCallback3);
                }
            });
        }

        public void takePicture2(final Camera.ShutterCallback shutterCallback, final Camera.PictureCallback pictureCallback, final Camera.PictureCallback pictureCallback2, final Camera.PictureCallback pictureCallback3, final int n, final int n2) {
            CameraManager.this.mCameraHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        CameraManager.this.mCamera.takePicture(shutterCallback, pictureCallback, pictureCallback2, pictureCallback3);
                    }
                    catch (RuntimeException ex) {
                        Log.w("CameraManager", "take picture failed; cameraState:" + n + ", focusState:" + n2);
                        throw ex;
                    }
                }
            });
        }

        public void unlock() {
            CameraManager.this.mCameraHandler.sendEmptyMessage(3);
        }

        public boolean waitDone() {
            final Object o = new Object();
            final Runnable cameraProxy = new Runnable() {
                @Override
                public void run() {
                    synchronized (o) {
                        o.notifyAll();
                    }
                }
            };
            synchronized (o) {
                CameraManager.this.mCameraHandler.post((Runnable)cameraProxy);
                try {
                    o.wait();
                    return true;
                }
                catch (InterruptedException ex) {
                    Log.v("CameraManager", "waitDone interrupted");
                    return false;
                }
            }
        }
    }

    private class IOExceptionHolder
    {
        public IOException ex;
    }
}