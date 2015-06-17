package com.mapia.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.mapia.R;
import com.mapia.camera.common.ApiHelper;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by daehyun on 15. 6. 16..
 */

public class Util {
    public static final String ACTION_NEW_PICTURE = "android.hardware.action.NEW_PICTURE";
    public static final String ACTION_NEW_VIDEO = "android.hardware.action.NEW_VIDEO";
    private static final String AUTO_EXPOSURE_LOCK_SUPPORTED = "auto-exposure-lock-supported";
    private static final String AUTO_WHITE_BALANCE_LOCK_SUPPORTED = "auto-whitebalance-lock-supported";
    private static final String EXTRAS_CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
    public static final String FALSE = "false";
    public static final String FOCUS_MODE_CONTINUOUS_PICTURE = "continuous-picture";
    public static final int ORIENTATION_HYSTERESIS = 5;
    public static final String RECORDING_HINT = "recording-hint";
    public static final String REVIEW_ACTION = "com.android.camera.action.REVIEW";
    public static final String SCENE_MODE_HDR = "hdr";
    private static final String TAG = "Util";
    public static final String TRUE = "true";
    private static final String VIDEO_SNAPSHOT_SUPPORTED = "video-snapshot-supported";
    private static ImageFileNamer sImageFileNamer;
    private static int[] sLocation;
    private static float sPixelDensity;

    static {
        Util.sPixelDensity = 1.0f;
        Util.sLocation = new int[2];
    }

    public static void Assert(final boolean b) {
        if (!b) {
            throw new AssertionError();
        }
    }

    public static void broadcastNewPicture(final Context context, final Uri uri) {
        context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", uri));
        context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
    }

    public static <T> T checkNotNull(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    public static int clamp(final int n, final int n2, final int n3) {
        if (n > n3) {
            return n3;
        }
        if (n < n2) {
            return n2;
        }
        return n;
    }

    public static void closeSilently(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Throwable t) {
        }
    }

    private static int computeInitialSampleSize(final BitmapFactory.Options bitmapFactory, final int n, final int n2) {
        final double n3 = bitmapFactory.outWidth;
        final double n4 = bitmapFactory.outHeight;
        int n5;
        if (n2 < 0) {
            n5 = 1;
        } else {
            n5 = (int) Math.ceil(Math.sqrt(n3 * n4 / n2));
        }
        int n6;
        if (n < 0) {
            n6 = 128;
        } else {
            n6 = (int) Math.min(Math.floor(n3 / n), Math.floor(n4 / n));
        }
        if (n6 >= n5) {
            if (n2 < 0 && n < 0) {
                return 1;
            }
            if (n >= 0) {
                return n6;
            }
        }
        return n5;
    }

    public static int computeSampleSize(final BitmapFactory.Options bitmapFactory, int n, int n2) {
        final int computeInitialSampleSize = computeInitialSampleSize(bitmapFactory, n, n2);
        if (computeInitialSampleSize <= 8) {
            n = 1;
            while (true) {
                n2 = n;
                if (n >= computeInitialSampleSize) {
                    break;
                }
                n <<= 1;
            }
        } else {
            n2 = (computeInitialSampleSize + 7) / 8 * 8;
        }
        return n2;
    }

    public static String createJpegName(final long n) {
        synchronized (Util.sImageFileNamer) {
            return Util.sImageFileNamer.generateName(n);
        }
    }

    public static float distance(float n, float n2, final float n3, final float n4) {
        n -= n3;
        n2 -= n4;
        return FloatMath.sqrt(n * n + n2 * n2);
    }

    public static int dpToPixel(final int n) {
        return Math.round(Util.sPixelDensity * n);
    }

    public static void dumpParameters(final Camera.Parameters camera) {
        while (new StringTokenizer(camera.flatten(), ";").hasMoreElements()) {
        }
    }

    public static void dumpRect(final RectF rectF, final String s) {
        Log.v("Util", s + "=(" + rectF.left + "," + rectF.top + "," + rectF.right + "," + rectF.bottom + ")");
    }

    public static boolean equals(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }

    public static void fadeIn(final View view) {
        fadeIn(view, 0.0f, 1.0f, 400L);
        view.setEnabled(true);
    }

    public static void fadeIn(final View view, final float n, final float n2, final long duration) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        ((Animation) alphaAnimation).setDuration(duration);
        view.startAnimation((Animation) alphaAnimation);
    }

    public static void fadeOut(final View view) {
        if (view.getVisibility() != View.VISIBLE) {
            return;
        }
        view.setEnabled(false);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        ((Animation) alphaAnimation).setDuration(400L);
        view.startAnimation((Animation) alphaAnimation);
        view.setVisibility(View.INVISIBLE);
    }

    public static int getCameraFacingIntentExtras(final Activity activity) {
        final int n = -1;
        final int intExtra = activity.getIntent().getIntExtra("android.intent.extras.CAMERA_FACING", -1);
        int n2;
        if (isFrontCameraIntent(intExtra)) {
            final int frontCameraId = CameraHolder.instance().getFrontCameraId();
            n2 = n;
            if (frontCameraId != -1) {
                n2 = frontCameraId;
            }
        } else {
            n2 = n;
            if (isBackCameraIntent(intExtra)) {
                final int backCameraId = CameraHolder.instance().getBackCameraId();
                n2 = n;
                if (backCameraId != -1) {
                    return backCameraId;
                }
            }
        }
        return n2;
    }

    public static int getCameraOrientation(final int n) {
        final Camera.CameraInfo CameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(n, CameraInfo);
        return CameraInfo.orientation;
    }

    @TargetApi(13)
    private static Point getDefaultDisplaySize(final Activity activity, final Point point) {
        final Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(point);
            return point;
        }
        point.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        return point;
    }

    public static int getDisplayOrientation(final int n, final int n2) {
        final Camera.CameraInfo CameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(n2, CameraInfo);
        if (CameraInfo.facing == 1) {
            return (360 - (CameraInfo.orientation + n) % 360) % 360;
        }
        return (CameraInfo.orientation - n + 360) % 360;
    }

    public static int getDisplayRotation(final Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            default: {
                return 0;
            }
            case 1: {
                return 90;
            }
            case 2: {
                return 180;
            }
            case 3: {
                return 270;
            }
        }
    }

    public static int getJpegRotation(final int n, final int n2) {
        int n3 = 0;
        if (n2 != -1) {
            final Camera.CameraInfo CameraInfo = CameraHolder.instance().getCameraInfo()[n];
            if (CameraInfo.facing != 1) {
                return (CameraInfo.orientation + n2) % 360;
            }
            n3 = (CameraInfo.orientation - n2 + 360) % 360;
        }
        return n3;
    }

    public static Camera.Size getOptimalPreviewSize(final Activity activity, final List<Camera.Size> list, double n) {
        Camera.Size size;
        if (list == null) {
            size = null;
        } else {
            final Camera.Size size2 = null;
            double n2 = Double.MAX_VALUE;
            final Point defaultDisplaySize = getDefaultDisplaySize(activity, new Point());
            final int min = Math.min(defaultDisplaySize.x, defaultDisplaySize.y);
            final Iterator<Camera.Size> iterator = list.iterator();
            Camera.Size size3 = size2;
            while (iterator.hasNext()) {
                final Camera.Size size4 = iterator.next();
                if (Math.abs(size4.width / size4.height - n) <= 0.001 && Math.abs(size4.height - min) < n2) {
                    size3 = size4;
                    n2 = Math.abs(size4.height - min);
                }
            }
            if ((size = size3) == null) {
                Log.w("Util", "No preview size match the aspect ratio");
                n = Double.MAX_VALUE;
                final Iterator<Camera.Size> iterator2 = list.iterator();
                while (true) {
                    size = size3;
                    if (!iterator2.hasNext()) {
                        break;
                    }
                    final Camera.Size size5 = iterator2.next();
                    if (Math.abs(size5.height - min) >= n) {
                        continue;
                    }
                    size3 = size5;
                    n = Math.abs(size5.height - min);
                }
            }
        }
        return size;
    }

    public static Camera.Size getOptimalVideoSnapshotPictureSize(final List<Camera.Size> list, final double n) {
        Camera.Size size;
        if (list == null) {
            size = null;
        } else {
            Camera.Size size2 = null;
            for (final Camera.Size size3 : list) {
                if (Math.abs(size3.width / size3.height - n) <= 0.001 && (size2 == null || size3.width > size2.width)) {
                    size2 = size3;
                }
            }
            if ((size = size2) == null) {
                Log.w("Util", "No picture size match the aspect ratio");
                final Iterator<Camera.Size> iterator2 = list.iterator();
                while (true) {
                    size = size2;
                    if (!iterator2.hasNext()) {
                        break;
                    }
                    final Camera.Size size4 = iterator2.next();
                    if (size2 != null && size4.width <= size2.width) {
                        continue;
                    }
                    size2 = size4;
                }
            }
        }
        return size;
    }

    public static int[] getRelativeLocation(final View view, final View view2) {
        view.getLocationInWindow(Util.sLocation);
        final int n = Util.sLocation[0];
        final int n2 = Util.sLocation[1];
        view2.getLocationInWindow(Util.sLocation);
        final int[] sLocation = Util.sLocation;
        sLocation[0] -= n;
        final int[] sLocation2 = Util.sLocation;
        sLocation2[1] -= n2;
        return Util.sLocation;
    }

    public static void initialize(final Context context) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        Util.sPixelDensity = displayMetrics.density;
        Util.sImageFileNamer = new ImageFileNamer(context.getString(R.string.image_file_name_format));
    }

    public static boolean isAutoExposureLockSupported(final Camera.Parameters parameters) {
        return "true".equals(parameters.get("auto-exposure-lock-supported"));
    }

    public static boolean isAutoWhiteBalanceLockSupported(final Camera.Parameters parameters) {
        return "true".equals(parameters.get("auto-whitebalance-lock-supported"));
    }

    private static boolean isBackCameraIntent(final int n) {
        return n == 0;
    }

    public static boolean isCameraHdrSupported(final Camera.Parameters parameters) {
        final List supportedSceneModes = parameters.getSupportedSceneModes();
        return supportedSceneModes != null && supportedSceneModes.contains("hdr");
    }

    @TargetApi(14)
    public static boolean isFocusAreaSupported(final Camera.Parameters parameters) {
        boolean b2;
        final boolean b = b2 = false;
        if (ApiHelper.HAS_CAMERA_FOCUS_AREA) {
            b2 = b;
            if (parameters.getMaxNumFocusAreas() > 0) {
                b2 = b;
                if (isSupported("auto", parameters.getSupportedFocusModes())) {
                    b2 = true;
                }
            }
        }
        return b2;
    }

    private static boolean isFrontCameraIntent(final int n) {
        return n == 1;
    }

    @TargetApi(14)
    public static boolean isMeteringAreaSupported(final Camera.Parameters parameters) {
        boolean b = false;
        if (ApiHelper.HAS_CAMERA_METERING_AREA) {
            b = b;
            if (parameters.getMaxNumMeteringAreas() > 0) {
                b = true;
            }
        }
        return b;
    }

    public static boolean isMmsCapable(final Context context) {
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return false;
        }
        try {
            return (boolean) TelephonyManager.class.getMethod("isVoiceCapable", (Class[]) new Class[0]).invoke(telephonyManager, new Object[0]);
        } catch (NoSuchMethodException ex) {
        } catch (IllegalAccessException ex2) {
            return false;
        } catch (InvocationTargetException ex3) {
            return false;
        }
        return false;
    }

    public static boolean isSupported(final String s, final List<String> list) {
        return list != null && list.indexOf(s) >= 0;
    }

    public static boolean isUriValid(final Uri uri, final ContentResolver contentResolver) {
        if (uri == null) {
            return false;
        }
        try {
            final ParcelFileDescriptor openFileDescriptor = contentResolver.openFileDescriptor(uri, "r");
            if (openFileDescriptor == null) {
                Log.e("Util", "Fail to open URI. URI=" + uri);
                return false;
            }
            openFileDescriptor.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public static boolean isVideoSnapshotSupported(final Camera.Parameters parameters) {
        return "true".equals(parameters.get("video-snapshot-supported"));
    }

    public static Bitmap makeBitmap(final byte[] array, final int n) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(array, 0, array.length, options);
            if (!options.mCancel && options.outWidth != -1) {
                if (options.outHeight == -1) {
                    return null;
                }
                options.inSampleSize = computeSampleSize(options, -1, n);
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                return BitmapFactory.decodeByteArray(array, 0, array.length, options);
            }
        } catch (OutOfMemoryError outOfMemoryError) {
            Log.e("Util", "Got oom exception ", (Throwable) outOfMemoryError);
        }
        return null;
    }

    public static int nextPowerOf2(int n) {
        --n;
        n |= n >>> 16;
        n |= n >>> 8;
        n |= n >>> 4;
        n |= n >>> 2;
        return (n | n >>> 1) + 1;
    }

    public static com.mapia.camera.CameraManager.CameraProxy openCamera(final Activity activity, final int n) throws CameraHardwareException, CameraDisabledException {
        throwIfCameraDisabled(activity);
        try {
            return CameraHolder.instance().open(n);
        } catch (CameraHardwareException ex) {
            if ("eng".equals(Build.TYPE)) {
                throw new RuntimeException("openCamera failed", ex);
            }
            throw ex;
        }
    }

    public static boolean pointInView(final float n, final float n2, final View view) {
        view.getLocationInWindow(Util.sLocation);
        return n >= Util.sLocation[0] && n < Util.sLocation[0] + view.getWidth() && n2 >= Util.sLocation[1] && n2 < Util.sLocation[1] + view.getHeight();
    }

    public static void prepareMatrix(final Matrix matrix, final boolean b, final int n, final int n2, final int n3) {
        float n4;
        if (b) {
            n4 = -1.0f;
        } else {
            n4 = 1.0f;
        }
        matrix.setScale(n4, 1.0f);
        matrix.postRotate((float) n);
        matrix.postScale(n2 / 2000.0f, n3 / 2000.0f);
        matrix.postTranslate(n2 / 2.0f, n3 / 2.0f);
    }

    public static void rectFToRect(final RectF rectF, final Rect rect) {
        rect.left = Math.round(rectF.left);
        rect.top = Math.round(rectF.top);
        rect.right = Math.round(rectF.right);
        rect.bottom = Math.round(rectF.bottom);
    }

    public static Bitmap rotate(final Bitmap bitmap, final int n) {
        return rotateAndMirror(bitmap, n, false);
    }

    public static Bitmap rotateAndMirror(final Bitmap bitmap, final int n, final boolean b) {
        if (n == 0) {
            final Bitmap bitmap2 = bitmap;
            if (!b) {
                return bitmap2;
            }
        }
        Bitmap bitmap2;
        if ((bitmap2 = bitmap) == null) {
            return bitmap2;
        }
        final Matrix matrix = new Matrix();
        int n2 = n;
        if (b) {
            matrix.postScale(-1.0f, 1.0f);
            n2 = (n + 360) % 360;
            if (n2 != 0 && n2 != 180) {
                return bitmap;
            }
            matrix.postTranslate((float) bitmap.getWidth(), 0.0f);
        }
        if (n2 != 0) {
            matrix.postRotate((float) n2, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
        }
        try {
            final Bitmap bitmap3 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if ((bitmap2 = bitmap) != bitmap3) {
                bitmap.recycle();
                bitmap2 = bitmap3;

                return bitmap2;
            }
            matrix.postTranslate((float) bitmap.getHeight(), 0.0f);
            return bitmap;
        } catch (OutOfMemoryError outOfMemoryError) {
            return bitmap;
        }

    }


    public static int roundOrientation(final int n, int n2) {
        int n3;
        if (n2 == -1) {
            n3 = 1;
        } else {
            final int abs = Math.abs(n - n2);
            if (Math.min(abs, 360 - abs) >= 50) {
                n3 = 1;
            } else {
                n3 = 0;
            }
        }
        if (n3 != 0) {
            n2 = (n + 45) / 90 * 90 % 360;
        }
        return n2;
    }

    public static void setGpsParameters(final Camera.Parameters parameters, final Location location) {
        parameters.removeGpsData();
        parameters.setGpsTimestamp(System.currentTimeMillis() / 1000L);
        if (location != null) {
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();
            int n;
            if (latitude != 0.0 || longitude != 0.0) {
                n = 1;
            } else {
                n = 0;
            }
            if (n == 0) {
                return;
            }
            parameters.setGpsLatitude(latitude);
            parameters.setGpsLongitude(longitude);
            parameters.setGpsProcessingMethod(location.getProvider().toUpperCase());
            if (location.hasAltitude()) {
                parameters.setGpsAltitude(location.getAltitude());
            } else {
                parameters.setGpsAltitude(0.0);
            }
            if (location.getTime() != 0L) {
                parameters.setGpsTimestamp(location.getTime() / 1000L);
            }
        }
    }

    public static void showErrorAndFinish(final Activity activity, final int message) {
        final DialogInterface.OnClickListener util = (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                activity.finish();
            }
        };
        final TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(16843605, typedValue, true);
        new AlertDialog.Builder((Context) activity).setCancelable(false).setTitle(R.string.camera_error_title).setMessage(message).setNeutralButton(R.string.dialog_ok, (DialogInterface.OnClickListener) util).setIcon(typedValue.resourceId).show();
    }

    public static boolean systemRotationLocked(final Activity activity) {
        boolean b = false;
        if (Settings.System.getInt(activity.getContentResolver(), "accelerometer_rotation", 0) == 0) {
            b = true;
        }
        return b;
    }

    @TargetApi(14)
    private static void throwIfCameraDisabled(final Activity activity) throws CameraDisabledException {
        if (ApiHelper.HAS_GET_CAMERA_DISABLED && ((DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE)).getCameraDisabled((ComponentName) null)) {
            throw new CameraDisabledException();
        }
    }

    public static void viewUri(final Uri uri, final Context context) {
        if (!isUriValid(uri, context.getContentResolver())) {
            Log.e("Util", "Uri invalid. uri=" + uri);
            return;
        }
        try {
            context.startActivity(new Intent("com.android.camera.action.REVIEW", uri));
        } catch (ActivityNotFoundException ex2) {
            try {
                context.startActivity(new Intent("android.intent.action.VIEW", uri));
            } catch (ActivityNotFoundException ex) {
                Log.e("Util", "review image fail. uri=" + uri, (Throwable) ex);
            }
        }
    }

    private static class ImageFileNamer {
        private SimpleDateFormat mFormat;
        private long mLastDate;
        private int mSameSecondCount;

        public ImageFileNamer(final String s) {
            super();
            this.mFormat = new SimpleDateFormat(s);
        }

        public String generateName(final long mLastDate) {
            final String format = this.mFormat.format(new Date(mLastDate));
            if (mLastDate / 1000L == this.mLastDate / 1000L) {
                ++this.mSameSecondCount;
                return format + "_" + this.mSameSecondCount;
            }
            this.mLastDate = mLastDate;
            this.mSameSecondCount = 0;
            return format;
        }
    }
}