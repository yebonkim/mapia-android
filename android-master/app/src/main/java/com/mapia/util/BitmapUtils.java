package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;

import com.mapia.MainApplication;
import com.mapia.camera.Storage;
import com.mapia.common.Thumb;
import com.mapia.util.stackblur.StackBlurManager;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapUtils
{
    public static final String IMAGE_CHOOSER_DIR = "mapia_tmp";
    public static final int STANDARD_SIZE = 4000;
    private static final String TAG = "BitmapUtils";
    private static MainApplication mainApplication;

    static {
        BitmapUtils.mainApplication = MainApplication.getInstance();
    }

    public static File bitmapConvertToFile(final Bitmap bitmap) {
        return bitmapConvertToFile(bitmap, 90);
    }

    public static File bitmapConvertToFile(final Bitmap bitmap, final int n) {
        return bitmapConvertToFile(bitmap, n, null);
    }

    public static File bitmapConvertToFile(final Bitmap bitmap, final int n, MediaScannerConnection.OnScanCompletedListener onScanCompletedListener) {
        final File bitmapToFile = bitmapToFile(bitmap, n);
        MediaScannerConnection.scanFile(MainApplication.getContext(), new String[]{bitmapToFile.getAbsolutePath()}, (String[]) null, onScanCompletedListener);
        return bitmapToFile;
    }

    public static File bitmapToFile(final Bitmap bitmap, final int n) {
        final File file = new File(Storage.MAPIA_DIRECTORY, "");
        if (!file.exists()) {
            file.mkdir();
        }
        final String string = Storage.MAPIA_DIRECTORY + "/" + "mapia_" + DateUtils.getYyyyMMddHHmmss() + "." + "jpg";
        try {
//            JpegTurbo.compressSafely(bitmap, n, string);
            return new File(string);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return bitmapToFileWithAndroid(bitmap, n, string);
        }
    }


    public static File bitmapToFileWithAndroid(Bitmap bitmap, int i, String s) {

        File file = new File(s);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }

        OutputStream out = null;

        try {
            out = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, i, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }



    public static Bitmap blurBitmap(final Bitmap bitmap, final int n) {
        return new StackBlurManager(bitmap).process(n);
    }

    public static String calcEndBitmapSuffic(final int n, final int n2) {
        switch (GalleryUtils.getRatioType(n, n2)) {
            default: {
                return "";
            }
            case 0: {
                return ThumbUtils.getSuffix(Thumb.END_BODY_11);
            }
            case 1: {
                return ThumbUtils.getSuffix(Thumb.END_BODY_43);
            }
            case 2: {
                return ThumbUtils.getSuffix(Thumb.END_BODY_34);
            }
        }
    }

    public static int calculateInSampleSize(final BitmapFactory.Options bitmapFactoryOptions, final int n, final int n2) {
        final int outHeight = bitmapFactoryOptions.outHeight;
        final int outWidth = bitmapFactoryOptions.outWidth;
        int n3 = 1;
        int n4 = 1;
        if (outHeight > n2 || outWidth > n) {
            final int n5 = outHeight / 2;
            final int n6 = outWidth / 2;
            while (true) {
                n3 = n4;
                if (n5 / n4 <= n2) {
                    break;
                }
                n3 = n4;
                if (n6 / n4 <= n) {
                    break;
                }
                n4 *= 2;
            }
        }
        return n3;
    }

    public static Bitmap comvertRGB565toARGB8888(Bitmap bitmap) {
        if (bitmap.isRecycled()) {
            return null;
        }
        final int[] array = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.setPixels(array, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap;
    }

    public static Bitmap convert(final Bitmap bitmap, final Bitmap.Config bitmapConfig) {
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmapConfig);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        paint.setColor(-16777216);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return bitmap2;
    }

    public static Bitmap convertByteToBitmap(final byte[] array) {
        final BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inMutable = true;
        return BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactoryOptions);
    }

    public static float convertDipToPixelFloat(final float n) {
        return TypedValue.applyDimension(1, n, BitmapUtils.mainApplication.getResources().getDisplayMetrics());
    }

    public static int convertDipToPixelInt(final float n) {
        return (int)(BitmapUtils.mainApplication.getResources().getDisplayMetrics().density * n + 0.5f);
    }

    public static int convertPixelToDipInt(final int n) {
        return (int)(n / BitmapUtils.mainApplication.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static Bitmap createBitmap(Bitmap bitmap, final String s) {
        bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.parseColor(s));
        return bitmap;
    }

    public static Bitmap createBitmapFitToView(final Bitmap bitmap, int n, int n2) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        if (width > n || height > n2) {
            if (width > height) {
                final int n3 = height * (n / width);
                n2 = n;
                n = n3;
            }
            else if (width < height) {
                final int n4 = width * (n2 / height);
                n = n2;
                n2 = n4;
            }
            else {
                float n5;
                if (n2 > n) {
                    n5 = n / width;
                }
                else {
                    n5 = n2 / height;
                }
                n2 = (int)(width * n5);
                n = (int)(height * n5);
            }
            return resizeBitmap(bitmap, n2, n);
        }
        return bitmap;
    }

    public static Bitmap cropBitmap(final Bitmap bitmap) {
        return cropBitmap(bitmap, 0);
    }

    public static Bitmap cropBitmap(final Bitmap bitmap, int convertDipToPixelInt) {
        final int deviceWidth = DeviceUtils.getDeviceWidth();
        final int deviceHeight = DeviceUtils.getDeviceHeight();
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final float n = height / width;
        if (convertDipToPixelInt > 0) {
            convertDipToPixelInt = convertDipToPixelInt(convertDipToPixelInt);
        }
        else {
            convertDipToPixelInt = deviceHeight;
        }
        final float n2 = convertDipToPixelInt / deviceWidth;
        if (n > 1.0f) {
            if (n > n2) {
                convertDipToPixelInt = (int)(width * n2);
                return Bitmap.createBitmap(bitmap, 0, (height - convertDipToPixelInt) / 2, width, convertDipToPixelInt);
            }
            convertDipToPixelInt = (int)(height / n2);
            return Bitmap.createBitmap(bitmap, (width - convertDipToPixelInt) / 2, 0, convertDipToPixelInt, height);
        }
        else {
            if (n < n2) {
                convertDipToPixelInt = (int)(height / n2);
                return Bitmap.createBitmap(bitmap, (width - convertDipToPixelInt) / 2, 0, convertDipToPixelInt, height);
            }
            convertDipToPixelInt = (int)(width * n2);
            return Bitmap.createBitmap(bitmap, 0, (height - convertDipToPixelInt) / 2, width, convertDipToPixelInt);
        }
    }

    public static Bitmap cropBitmapForSquare(final Bitmap bitmap) {
        final float n = DeviceUtils.getDeviceHeight() / DeviceUtils.getDeviceWidth();
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        final int n2 = (int)(height / n);
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap, (width - n2) / 2, 0, n2, height);
        bitmap.recycle();
        return bitmap2;
    }

    public static Bitmap cropBitmapToSquare(final Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        if (height / width > 1.0f) {
            return Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
        }
        return Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
    }

    public static Bitmap decodeSampledBitmapFromFilePath(final String s, int i, final int n) {
        final BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        bitmapFactoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, bitmapFactoryOptions);
        bitmapFactoryOptions.inSampleSize = calculateInSampleSize(bitmapFactoryOptions, i, n);
        bitmapFactoryOptions.inJustDecodeBounds = false;
        Bitmap decodeFile = null;
        i = 0;
        while (i == 0) {
            i = 1;
            try {
                decodeFile = BitmapFactory.decodeFile(s, bitmapFactoryOptions);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                Log.w("BitmapUtils", "decodeSampledBitmapFromFilePath OutOfMemoryError");
                bitmapFactoryOptions.inSampleSize *= 2;
                i = 0;
            }
        }
        return rotate(decodeFile, exifToDegrees(getExif(s).getAttributeInt("Orientation", 1)));
    }

    public static Bitmap decodeSampledBitmapFromUri(final Uri uri, final int n, final int n2) {
        final String pathFromUri = FileUtils.getPathFromUri(MainApplication.getContext(), uri);
        if (StringUtils.isNotBlank(pathFromUri)) {
            return decodeSampledBitmapFromFilePath(pathFromUri, n, n2);
        }
        return null;
    }

    public static void deleteImageChooserTemp() {
//        final File file = new File(FileUtils.getDirectory("mapia_tmp"));
//        if (file.isDirectory()) {
//            final File[] listFiles = file.listFiles();
//            for (int length = listFiles.length, i = 0; i < length; ++i) {
//                listFiles[i].delete();
//            }
//            file.delete();
//            return;
//        }
//        file.delete();
    }

    public static int exifToDegrees(final int n) {
        if (n == 6) {
            return 90;
        }
        if (n == 3) {
            return 180;
        }
        if (n == 8) {
            return 270;
        }
        return 0;
    }

    @Deprecated
    public static Bitmap getBitmapFromFilePath(final String s) {
        return rotate(BitmapFactory.decodeFile(s), exifToDegrees(getExif(s).getAttributeInt("Orientation", 1)));
    }

    @Deprecated
    public static Bitmap getBitmapFromUri(final Context context, final Uri uri) {
        return getBitmapFromFilePath(FileUtils.getPathFromUri(context, uri));
    }

    public static ExifInterface getExif(final String s) {
        try {
            return new ExifInterface(s);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Bitmap getLeftRightInversionBitmap(final Bitmap bitmap) {
        final Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static Bitmap getVerticalFlipBitmap(final Bitmap bitmap) {
        final Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bitmap2;
    }

    public static Bitmap lanczosResizeBitmap(final Bitmap bitmap, final int n, final int n2) {
//        final JpegResize jpegResize = new JpegResize();
//        final Bitmap bitmap2 = Bitmap.createBitmap(n, n2, bitmap.getConfig());
//        if (jpegResize.ResizeBitmap(bitmap, bitmap2, JpegResize.JPEGREIZE_INTERPOLATION_LANCZOS) == 0) {
//            return bitmap2;
//        }
//        Log.w("BitmapUtils", "lanczosResize fail");
        return bitmap;
    }

    public static byte[] makeSquareSize(final byte[] array) {
        final Bitmap squareSizeBitmap = makeSquareSizeBitmap(convertByteToBitmap(array));
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        squareSizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap makeSquareSizeBitmap(final Bitmap bitmap) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        if (width > height) {
            return Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
        }
        return Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
    }

    public static Bitmap makeTransparent(final Bitmap bitmap, final int alpha) {
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        canvas.drawARGB(0, 0, 0, 0);
        final Paint paint = new Paint();
        paint.setAlpha(alpha);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        bitmap.recycle();
        return bitmap2;
    }

    public static Bitmap overlayBitmap(final Bitmap bitmap, final Bitmap bitmap2) {
        final Bitmap bitmap3 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        final Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap, new Matrix(), (Paint)null);
        canvas.drawBitmap(bitmap2, new Matrix(), (Paint)null);
        bitmap.recycle();
        bitmap2.recycle();
        return bitmap3;
    }

    public static Bitmap resizeBitmap(final Bitmap bitmap, final int n, final int n2) {
        return resizeBitmap(bitmap, n, n2, true);
    }

    public static Bitmap resizeBitmap(final Bitmap bitmap, final int n, final int n2, final boolean b) {
        Bitmap bitmap2;
        if (n == 0 || n2 == 0) {
            bitmap2 = null;
        }
        else {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final float n3 = n / width;
            final float n4 = n2 / height;
            final Matrix matrix = new Matrix();
            matrix.postScale(n3, n4);
            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            if (b) {
                bitmap.recycle();
                return bitmap2;
            }
        }
        return bitmap2;
    }

    public static Bitmap rotate(final Bitmap bitmap, final int n) {
        Bitmap bitmap2 = bitmap;
        if (n == 0 || (bitmap2 = bitmap) == null) {
            return bitmap2;
        }
        final Matrix matrix = new Matrix();
        matrix.setRotate((float)n, bitmap.getWidth() / 2.0f, bitmap.getHeight() / 2.0f);
        try {
            final Bitmap bitmap3 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap2 = bitmap;
            if (bitmap != bitmap3) {
                bitmap.recycle();
                bitmap2 = bitmap3;
            }
            return bitmap2;
        }
        catch (OutOfMemoryError outOfMemoryError) {
            return bitmap;
        }
    }

    public static Bitmap rotateBitmap(final Bitmap bitmap, final float n) {
        final Matrix matrix = new Matrix();
        matrix.postRotate(n);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getHeight(), bitmap.getWidth(), matrix, true);
    }

    public static Bitmap scale(final Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, DeviceUtils.getDeviceWidth(), DeviceUtils.getDeviceHeight(), true);
    }

    public static Bitmap thumbnailBitmap(final Bitmap bitmap, final int n) {
        if (bitmap == null) {
            return null;
        }
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        if (width > height) {
            final float n2 = n / height;
            return resizeBitmap(bitmap, (int)(width * n2), (int)(height * n2));
        }
        final float n3 = n / width;
        return resizeBitmap(bitmap, (int)(width * n3), (int)(height * n3));
    }

    public static Bitmap thumbnailBitmap(final String s, final int n) {
        return thumbnailBitmap(decodeSampledBitmapFromFilePath(s, (int)(n * 1.4), (int)(n * 1.4)), n);
    }
}