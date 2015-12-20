package com.dasunica.offroute.async_task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.dasunica.offroute.Wear_Principal;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by fran on 28/01/15.
 */
public class BitmapTask extends AsyncTask<Asset,Void,Bitmap> {

    @Override
    protected Bitmap doInBackground(Asset... assets) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(Wear_Principal.getInstance())
                .addApi(Wearable.API)
                .build();
        if (assets[0] == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
                mGoogleApiClient.blockingConnect(1000, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, assets[0]).await().getInputStream();
        mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w("Respuesta", "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Log.i("map",bitmap.toString());
        if(bitmap != null){
            Wear_Principal.getInstance().setBitmap(bitmap);
            Wear_Principal.getInstance().getMapImage().invalidate();
            Wear_Principal.getInstance().getMapImage().setImageBitmap(bitmap);
        }
    }
}
