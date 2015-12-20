package com.dasunica.offroute.utils;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.layer.overlay.Marker;

/**
 * Created by fran on 29/10/14.
 */
public class Position extends Marker {

    private LatLong latLong;
    private Bitmap bitmap;
    private int horizontal,vertical;

    public Position(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
        this.latLong = latLong;
        this.bitmap = bitmap;
        horizontal = horizontalOffset;
        vertical = verticalOffset;
    }

    @Override
    public LatLong getLatLong() {
        return latLong;
    }

    @Override
    public void setLatLong(LatLong latLong) {
        this.latLong = latLong;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }
}
