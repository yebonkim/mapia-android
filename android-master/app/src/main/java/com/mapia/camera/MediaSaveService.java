package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.File;


public class MediaSaveService extends Service
{
    private static final int SAVE_TASK_LIMIT = 3;
    private static final String TAG;
    private final IBinder mBinder;
    private Listener mListener;
    private int mTaskNumber;

    static {
        TAG = MediaSaveService.class.getSimpleName();
    }

    public MediaSaveService() {
        super();
        this.mBinder = (IBinder)new LocalBinder();
    }

    private void onQueueAvailable() {
        if (this.mListener != null) {
            this.mListener.onQueueStatus(false);
        }
    }

    private void onQueueFull() {
        if (this.mListener != null) {
            this.mListener.onQueueStatus(true);
        }
    }

    public void addImage(final byte[] array, final String s, final long n, Location location, final int n2, final int n3, final int n4, final ExifInterface exifInterface, final OnMediaSavedListener onMediaSavedListener, final ContentResolver contentResolver) {
        if (this.isQueueFull()) {
            Log.e(MediaSaveService.TAG, "Cannot add image when the queue is full");
            return;
        }
        if (location == null) {
            location = null;
        }
        else {
            location = new Location(location);
        }
        final ImageSaveTask imageSaveTask = new ImageSaveTask(array, s, n, location, n2, n3, n4, exifInterface, contentResolver, onMediaSavedListener);
        ++this.mTaskNumber;
        if (this.isQueueFull()) {
            this.onQueueFull();
        }
        imageSaveTask.execute(new Void[0]);
    }

    public void addVideo(final String s, final long n, final ContentValues contentValues, final OnMediaSavedListener onMediaSavedListener, final ContentResolver contentResolver) {
        new VideoSaveTask(s, n, contentValues, onMediaSavedListener, contentResolver).execute(new Void[0]);
    }

    public boolean isQueueFull() {
        return this.mTaskNumber >= 3;
    }

    public IBinder onBind(final Intent intent) {
        return this.mBinder;
    }

    public void onCreate() {
        this.mTaskNumber = 0;
    }

    public void onDestroy() {
    }

    public int onStartCommand(final Intent intent, final int n, final int n2) {
        return 1;
    }

    public void setListener(final Listener mListener) {
        this.mListener = mListener;
        if (mListener == null) {
            return;
        }
        mListener.onQueueStatus(this.isQueueFull());
    }

    private class ImageSaveTask extends AsyncTask<Void, Void, Uri>
    {
        private byte[] data;
        private long date;
        private ExifInterface exif;
        private int height;
        private OnMediaSavedListener listener;
        private Location loc;
        private int orientation;
        private ContentResolver resolver;
        private String title;
        private int width;

        public ImageSaveTask(final byte[] data, final String title, final long date, final Location loc, final int width, final int height, final int orientation, final ExifInterface exif, final ContentResolver resolver, final OnMediaSavedListener listener) {
            super();
            this.data = data;
            this.title = title;
            this.date = date;
            this.loc = loc;
            this.width = width;
            this.height = height;
            this.orientation = orientation;
            this.exif = exif;
            this.resolver = resolver;
            this.listener = listener;
        }

        protected Uri doInBackground(final Void... array) {
            return Storage.addImage(this.resolver, this.title, this.date, this.loc, this.orientation, this.exif, this.data, this.width, this.height);
        }

        protected void onPostExecute(final Uri uri) {
            if (this.listener != null) {
                this.listener.onMediaSaved(uri);
            }
            MediaSaveService.this.mTaskNumber--;
            if (MediaSaveService.this.mTaskNumber == 2) {
                MediaSaveService.this.onQueueAvailable();
            }
        }

        protected void onPreExecute() {
        }
    }

    interface Listener
    {
        void onQueueStatus(boolean p0);
    }

    class LocalBinder extends Binder
    {
        public MediaSaveService getService() {
            return MediaSaveService.this;
        }
    }

    interface OnMediaSavedListener
    {
        void onMediaSaved(Uri p0);
    }

    private class VideoSaveTask extends AsyncTask<Void, Void, Uri>
    {
        private long duration;
        private OnMediaSavedListener listener;
        private String path;
        private ContentResolver resolver;
        private ContentValues values;

        public VideoSaveTask(final String path, final long duration, final ContentValues contentValues, final OnMediaSavedListener listener, final ContentResolver resolver) {
            super();
            this.path = path;
            this.duration = duration;
            this.values = new ContentValues(contentValues);
            this.listener = listener;
            this.resolver = resolver;
        }

        protected Uri doInBackground(Void... array) {
            this.values.put("_size", new File(this.path).length());
            this.values.put("duration", this.duration);
            final Void[] array2 = null;
            Object insert = null;
            array = array2;
            try {
                final Uri parse = Uri.parse("content://media/external/video/media");
                insert = insert;
                array = array2;
                final Object o = array = (Void[])(insert = this.resolver.insert(parse, this.values));
                final String asString = this.values.getAsString("_data");
                insert = o;
                array = (Void[])o;
                if (new File(this.path).renameTo(new File(asString))) {
                    insert = o;
                    array = (Void[])o;
                    this.path = asString;
                }
                insert = o;
                array = (Void[])o;
                this.resolver.update((Uri)o, this.values, (String)null, (String[])null);
                return (Uri)o;
            }
            catch (Exception ex) {
                array = (Void[])insert;
                Log.e(MediaSaveService.TAG, "failed to add video to media store", (Throwable)ex);
                Log.v(MediaSaveService.TAG, "Current video URI: " + (Object)null);
                return null;
            }
            finally {
                Log.v(MediaSaveService.TAG, "Current video URI: " + array);
            }
        }

        protected void onPostExecute(final Uri uri) {
            if (this.listener != null) {
                this.listener.onMediaSaved(uri);
            }
        }

        protected void onPreExecute() {
        }
    }
}