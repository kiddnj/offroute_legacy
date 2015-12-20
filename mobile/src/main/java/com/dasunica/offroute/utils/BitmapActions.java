package com.dasunica.offroute.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import com.dasunica.offroute.Principal;
import com.google.android.gms.wearable.Asset;

import java.io.ByteArrayOutputStream;

/**
 * Created by fran on 2/02/15.
 */
public class BitmapActions {

    private static Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }

    public static Asset createAsset(){
        DisplayMetrics metrics = Principal.getInstance().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        VariablesMobile.getInstance().getMapView().draw(canvas);
        return createAssetFromBitmap(Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2) - 140, (bitmap.getHeight() / 2) - 140, 280, 280));
    }

}
