package com.mapia.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by JahyunKim on 15. 7. 30..
 */
public class CircleData {
    LatLng location;
    int color;
    float radius;
    Circle circle;
    public CircleData(LatLng location){
        this.location = location;
        this.color = Color.argb(30,255,00,00);
        this.radius = 200;
    }
}
