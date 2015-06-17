package com.mapia.camera;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by daehyun on 15. 6. 16..
 */
public class LocationListener implements android.location.LocationListener
{
    Location mLastLocation;
    String mProvider;
    LocationManager mLocationManager;
    boolean mValid;

    public LocationListener(final String mProvider, LocationManager mLocationManager) {
        super();
        this.mValid = false;
        this.mProvider = mProvider;
        this.mLocationManager = mLocationManager;
        this.mLastLocation = new Location(this.mProvider);
    }

    public Location current() {
        if (this.mValid) {
            return this.mLastLocation;
        }
        return null;
    }

    public void onLocationChanged(final Location location) {
        if (location.getLatitude() == 0.0 && location.getLongitude() == 0.0) {
            return;
        }
        if (mLocationManager.mListener != null && mLocationManager.mRecordLocation && "gps".equals(this.mProvider)) {
            mLocationManager.mListener.showGpsOnScreenIndicator(true);
        }
        while (true) {
            if (!this.mValid) {
                this.mLastLocation.set(location);
                this.mValid = true;
                return;
            }
            continue;
        }
    }

    public void onProviderDisabled(final String s) {
        this.mValid = false;
    }

    public void onProviderEnabled(final String s) {
    }

    public void onStatusChanged(final String s, final int n, final Bundle bundle) {
        switch (n) {
            case 0:
            case 1: {
                this.mValid = false;
                if (mLocationManager.mListener != null && mLocationManager.mRecordLocation && "gps".equals(s)) {
                    mLocationManager.mListener.showGpsOnScreenIndicator(false);
                    return;
                }
                break;
            }
        }
    }
}