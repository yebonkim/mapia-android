package com.mapia.util;

/**
 * Created by daehyun on 15. 6. 8..
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mapia.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils
{
    public static boolean copyFile(final File file, final String s) {
        if (file != null && file.exists()) {
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;

                try {
                    fileInputStream = new FileInputStream(file);
                    fileOutputStream = new FileOutputStream(s);
                    final byte[] array = new byte[1024];
                    while (true) {
                        final int read = fileInputStream.read(array, 0, 1024);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(array, 0, read);
                    }

                    fileOutputStream.close();
                    fileInputStream.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;

        }
        return false;
    }

    public static String createVideoName(final Context context, final long n) {
        return new SimpleDateFormat(context.getString(R.string.video_file_name_format)).format(new Date(n));
    }

    public static boolean deleteFile(final File file) {
        return file != null && file.exists() && file.delete();
    }

    public static String getPathFromUri(final Context context, Uri string) {
        if (string == null) {
            string = null;
            return null;
        }
        Object o = null;
        try {
            final Cursor query = context.getContentResolver().query(string, new String[] { "_data" }, (String)null, (String[])null, (String)null);
            if (query == null) {
                return null;
            }
            o = query;
            final int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
            o = query;
            query.moveToFirst();
            o = query;
            string = Uri.parse(query.getString(columnIndexOrThrow));
            o = string;
            return (String)o;
        }
        finally {
            if (o != null) {
                ((Cursor)o).close();
            }
        }
    }
}