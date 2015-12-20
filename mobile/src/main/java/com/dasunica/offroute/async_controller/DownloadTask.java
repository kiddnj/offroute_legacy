package com.dasunica.offroute.async_controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import com.dasunica.offroute.ListMaps;
import com.dasunica.offroute.Principal;
import com.dasunica.offroute.controllers.GeoController;
import com.dasunica.offroute.utils.VariablesMobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fran on 11/12/14.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private PowerManager.WakeLock mWakeLock;
    private final String PATH = VariablesMobile.getInstance().getProperties().getProperty("path");
    private final String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString() + "/offroute/maps/";
    private String ruta;

    @Override
    protected String doInBackground(String... sUrl) {
        String[] names = sUrl[0].split("/");
        if(!names[1].endsWith(".map")){
            File directory = new File(SD_CARD_PATH + names[1] + "/");
            if(!directory.exists()){
                directory.mkdirs();
            }
            ruta = SD_CARD_PATH + names[1] + "/" + names[2];
        }else{
            ruta = SD_CARD_PATH + names[1];
        }
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(PATH + sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(ruta);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) ListMaps.getInstance().getSystemService(ListMaps.getInstance().POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        ListMaps.getInstance().getmProgressDialog().show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        ListMaps.getInstance().getmProgressDialog().setIndeterminate(false);
        ListMaps.getInstance().getmProgressDialog().setMax(100);
        ListMaps.getInstance().getmProgressDialog().setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        ListMaps.getInstance().getmProgressDialog().dismiss();
        if (result != null){
            Toast.makeText(ListMaps.getInstance(), "Download error: " + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ListMaps.getInstance(), "File downloaded", Toast.LENGTH_SHORT).show();
            VariablesMobile.getInstance().setGeoController(new GeoController(ListMaps.getInstance()));
            VariablesMobile.getInstance().getGeoController().startUsingGPS();
            VariablesMobile.getInstance().getProperties().
                    setProperty("mapFile", ruta);
            Intent intent = new Intent(ListMaps.getInstance(),Principal.class);
            ListMaps.getInstance().startActivity(intent);
            ListMaps.getInstance().finish();
        }
    }
}
