package com.dasunica.offroute.track_data;

import com.dasunica.offroute.utils.TapAction;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

/**
 * Created by fran on 7/10/14.
 */
public class Waypoint extends Marker {

    private LatLong latlong;
    private Bitmap bitmap;
    private String name,desc;
    private double elevation;
    private TapAction action;

    public Waypoint(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset,
                    String name, String desc, double elevation) {
        this(latLong,bitmap,horizontalOffset,verticalOffset);
        this.name = name;
        this.desc = desc;
        this.elevation = elevation;
    }

    public Waypoint(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.latlong = latLong;
        this.bitmap = bitmap;
    }

    public LatLong getLatlong() {
        return latlong;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public void setOnTapAction(TapAction action){
        this.action = action;
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        double centerX = layerXY.x + getHorizontalOffset();
        double centerY = layerXY.y + getVerticalOffset();

        double radiusX = (getBitmap().getWidth() / 2) *1.1;
        double radiusY = (getBitmap().getHeight() / 2) *1.1;

        double distX = Math.abs(centerX - tapXY.x);
        double distY = Math.abs(centerY - tapXY.y);

        if( distX < radiusX && distY < radiusY){
            if(action != null){
                action.run();
                return true;
            }
        }

        return false;
    }

}
