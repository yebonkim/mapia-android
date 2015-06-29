/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mapia.camera;

import android.util.Log;

import com.mapia.camera.exif.ExifInterface;
import com.mapia.camera.exif.ExifInvalidFormatException;
import com.mapia.camera.exif.ExifTag;
import com.mapia.camera.exif.ExifParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Exif {
    private static final String TAG = "CameraExif";

    // Returns the degrees in clockwise. Values are 0, 90, 180, or 270.

    public static int getOrientation(final ExifInterface exifInterface) {
        final Integer tagIntValue = exifInterface.getTagIntValue(ExifInterface.TAG_ORIENTATION);
        if (tagIntValue == null) {
            return 0;
        }
        return ExifInterface.getRotationForOrientationValue((short)(Object)tagIntValue);
    }

    public static int getOrientation(byte[] jpeg) {
        if (jpeg == null) return 0;

        InputStream is = new ByteArrayInputStream(jpeg);

        try {
            ExifParser parser = ExifParser.parse(is, new ExifInterface());
            int event = parser.next();
            while(event != ExifParser.EVENT_END) {
                if (event == ExifParser.EVENT_NEW_TAG) {
                    ExifTag tag = parser.getTag();
                    if (tag.getTagId() == ExifInterface.TAG_ORIENTATION &&
                            tag.hasValue()) {
                        int orient = (int) tag.getValueAt(0);
                        switch (orient) {
                            case ExifInterface.Orientation.TOP_LEFT:
                                return 0;
                            case ExifInterface.Orientation.BOTTOM_LEFT:
                                return 180;
                            case ExifInterface.Orientation.RIGHT_TOP:
                                return 90;
                            case ExifInterface.Orientation.RIGHT_BOTTOM:
                                return 270;
                            default:
                                Log.i(TAG, "Unsupported orientation");
                                return 0;
                        }
                    }
                }
                event = parser.next();
            }
            Log.i(TAG, "Orientation not found");
            return 0;
        } catch (IOException e) {
            Log.w(TAG, "Failed to read EXIF orientation", e);
            return 0;
        } catch (ExifInvalidFormatException e) {
            Log.w(TAG, "Failed to read EXIF orientation", e);
            return 0;
        }
    }

    public static ExifInterface getExif(final String s) {
        final ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(s);
            return exifInterface;
        } catch (IOException ex) {
            Log.w("CameraExif", "Failed to read EXIF data", (Throwable)ex);
            return exifInterface;
        }
    }

    public static ExifInterface getExif(final byte[] array) {
        final ExifInterface exifInterface = new ExifInterface();
        try {
            exifInterface.readExif(array);
            return exifInterface;
        }
        catch (IOException ex) {
            Log.w("CameraExif", "Failed to read EXIF data", (Throwable)ex);
            return exifInterface;
        }
    }
}
