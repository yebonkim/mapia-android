package com.mapia.map;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.sql.Timestamp;

class MarkerData{
    boolean isNew;
	LatLng location;
    String letter;
    Timestamp time;
    String user;
    Bitmap image;
	Marker marker;

	public MarkerData(LatLng location){
		super();
		this.location = location;
	}

    public MarkerData(LatLng location, Timestamp time, String letter){
        super();
        this.location = location;
        this.time = time;
        this.letter = letter;
    }

    public MarkerData(LatLng location, String letter){
        super();
        this.location = location;
        this.letter = letter;
    }

    public MarkerData(LatLng location, Timestamp time, String letter, String user){
        super();
        this.location = location;
        this.time = time;
        this.letter = letter;
        this.user = user;
    }

    public MarkerData(LatLng location, Timestamp time, String letter, String user, Bitmap image){
        super();
        this.location = location;
        this.time = time;
        this.letter = letter;
        this.user = user;
        this.image = image;
    }
    public MarkerData(LatLng location, String letter, String user){
        super();
        this.location = location;
        this.letter = letter;
        this.user = user;
    }
}
