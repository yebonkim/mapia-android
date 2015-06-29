package com.mapia.camera;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import com.mapia.camera.common.ApiHelper;
import com.mapia.camera.exif.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by daehyun on 15. 6. 8..
 */

public class Storage
{
    public static final String BUCKET_ID;
    public static final String DCIM;
    public static final String DIRECTORY;
    public static final long LOW_STORAGE_THRESHOLD = 50000000L;
    public static final String MAPIA_DIRECTORY;
    public static final long PREPARING = -2L;
    private static final String TAG = "CameraStorage";
    public static final long UNAVAILABLE = -1L;
    public static final long UNKNOWN_SIZE = -3L;

    static {
        DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        DIRECTORY = Storage.DCIM + "/Camera";
        MAPIA_DIRECTORY = Storage.DCIM + "/Mapia";
        BUCKET_ID = String.valueOf(Storage.DIRECTORY.toLowerCase().hashCode());
    }

    public static Uri addImage(final ContentResolver contentResolver, final String s, final long n, final Location location, final int n2, final int n3, final String s2, final int n4, final int n5) {
        final ContentValues contentValues = new ContentValues(9);
        contentValues.put("title", s);
        contentValues.put("_display_name", s + ".jpg");
        contentValues.put("datetaken", n);
        contentValues.put("mime_type", "image/jpeg");
        contentValues.put("orientation", n2);
        contentValues.put("_data", s2);
        contentValues.put("_size", n3);
        setImageSize(contentValues, n4, n5);
        if (location != null) {
            contentValues.put("latitude", location.getLatitude());
            contentValues.put("longitude", location.getLongitude());
        }
        try {
            return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        catch (Throwable t) {
            Log.e("CameraStorage", "Failed to write MediaStore" + t);
            return null;
        }
    }

    public static Uri addImage(final ContentResolver contentResolver, final String s, final long n, final Location location, final int n2, final ExifInterface exifInterface, final byte[] array, final int n3, final int n4) {
        final String generateFilepath = generateFilepath(s);
        while (true) {
            Label_0055: {
                if (exifInterface == null) {
                    break Label_0055;
                }
                try {
//                    exifInterface.writeExif(array, generateFilepath);
                    return addImage(contentResolver, s, n, location, n2, array.length, generateFilepath, n3, n4);
                }
                catch (Exception ex) {
                    Log.e("CameraStorage", "Failed to write data", (Throwable)ex);
                    return addImage(contentResolver, s, n, location, n2, array.length, generateFilepath, n3, n4);
                }
            }
            writeFile(generateFilepath, array);
            continue;
        }
    }

    public static void deleteImage(final ContentResolver contentResolver, final Uri uri) {
        try {
            contentResolver.delete(uri, (String)null, (String[])null);
        }
        catch (Throwable t) {
            Log.e("CameraStorage", "Failed to delete image: " + uri);
        }
    }

    public static void ensureOSXCompatible() {
        final File file = new File(Storage.DCIM, "100ANDRO");
        if (!file.exists() && !file.mkdirs()) {
            Log.e("CameraStorage", "Failed to create " + file.getPath());
        }
    }

    public static String generateFilepath(final String s) {
        return Storage.DIRECTORY + '/' + s + ".jpg";
    }

    public static long getAvailableSpace() {
        final long n = -1L;
        final String externalStorageState = Environment.getExternalStorageState();
        long n2;
        if ("checking".equals(externalStorageState)) {
            n2 = -2L;
        }
        else {
            n2 = n;
            if ("mounted".equals(externalStorageState)) {
                final File file = new File(Storage.DIRECTORY);
                file.mkdirs();
                n2 = n;
                if (file.isDirectory()) {
                    n2 = n;
                    if (file.canWrite()) {
                        try {
                            final StatFs statFs = new StatFs(Storage.DIRECTORY);
                            return statFs.getAvailableBlocks() * statFs.getBlockSize();
                        }
                        catch (Exception ex) {
                            Log.i("CameraStorage", "Fail to access external storage", (Throwable)ex);
                            return -3L;
                        }
                    }
                }
            }
        }
        return n2;
    }

    @TargetApi(16)
    private static void setImageSize(final ContentValues contentValues, final int n, final int n2) {
        if (ApiHelper.HAS_MEDIA_COLUMNS_WIDTH_AND_HEIGHT) {
            contentValues.put("width", n);
            contentValues.put("height", n2);
        }
    }

    public static void writeFile(String s, byte abyte0[])
    {

        FileOutputStream file;
        try {
            file = new FileOutputStream(new File(s));
            file.write(abyte0);
            file.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}