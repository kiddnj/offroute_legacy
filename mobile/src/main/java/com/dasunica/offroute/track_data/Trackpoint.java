package com.dasunica.offroute.track_data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.mapsforge.core.model.LatLong;

/**
 * Created by fran on 7/10/14.
 */
public class Trackpoint extends LatLong{

    private double latitude,longitude,elevation;
    private AlertDialog alertDialog;

    public Trackpoint(double latitude, double longitude) {
        super(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Trackpoint(double latitude,double longitude,double elevation){
        this(latitude,longitude);
        this.elevation = elevation;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }


}
