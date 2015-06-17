package com.mapia.camera;

/**
 * Created by daehyun on 15. 6. 16..
 */

import android.content.Context;
import android.location.Location;
import android.util.Log;


public class LocationManager
{
    private static final String TAG = "LocationManager";
    private Context mContext;
    protected Listener mListener;
    LocationListener[] mLocationListeners;
    private android.location.LocationManager mLocationManager;
    protected boolean mRecordLocation;

    public LocationManager(final Context mContext, final Listener mListener) {
        super();
        this.mLocationListeners = new LocationListener[] { new LocationListener("gps", this), new LocationListener("network", this) };
        this.mContext = mContext;
        this.mListener = mListener;
    }

    private void startReceivingLocationUpdates() {
        if (mLocationManager == null)
        {
            mLocationManager = (android.location.LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        }
        try
        {
            mLocationManager.requestLocationUpdates("network", 1000L, 0.0F, mLocationListeners[1]);
        }
        catch (SecurityException securityexception)
        {
            Log.i("LocationManager", "fail to request location update, ignore", securityexception);
        }
        catch (IllegalArgumentException illegalargumentexception) { }

        mLocationManager.requestLocationUpdates("gps", 1000L, 0.0F, mLocationListeners[0]);

        if (mListener != null)
        {
            mListener.showGpsOnScreenIndicator(false);
        }
        return;
    }

    private void stopReceivingLocationUpdates() {
        if (this.mLocationManager != null) {
            int i = 0;
            while (i < this.mLocationListeners.length) {
                    try {
                        this.mLocationManager.removeUpdates((LocationListener)this.mLocationListeners[i]);
                        ++i;
                        continue;
                    }
                    catch (Exception ex) {
                        Log.i("LocationManager", "fail to remove location listners, ignore", (Throwable) ex);
                        continue;
                    }
                }
            }

        if (this.mListener != null) {
            this.mListener.hideGpsOnScreenIndicator();
        }
    }

    public Location getCurrentLocation() {
        if (this.mRecordLocation) {
            for (int i = 0; i < this.mLocationListeners.length; ++i) {
                final Location current;
                if ((current = ((com.mapia.camera.LocationListener)this.mLocationListeners[i]).current()) != null) {
                    return current;
                }
            }
            return null;
        }
        return null;
    }

    public void recordLocation(final boolean mRecordLocation) {
        if (this.mRecordLocation != mRecordLocation) {
            this.mRecordLocation = mRecordLocation;
            if (!mRecordLocation) {
                this.stopReceivingLocationUpdates();
                return;
            }
            this.startReceivingLocationUpdates();
        }
    }

    public interface Listener
    {
        void hideGpsOnScreenIndicator();

        void showGpsOnScreenIndicator(boolean p0);
    }


}