package com.dasunica.offroute.controllers;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.dasunica.offroute.R;
import com.dasunica.offroute.async_controller.DataTask;
import com.dasunica.offroute.utils.BitmapActions;
import com.dasunica.offroute.utils.VariablesMobile;
import com.dasunica.offroute.async_controller.PositionTask;
import com.google.android.gms.wearable.DataMap;

import org.mapsforge.core.model.LatLong;

/**
 * Created by fran on 1/10/14.
 */
public class GeoController extends Service implements LocationListener{

    private final Context mContext;
    private VariablesMobile variablesMobile;

    private Location location; // Location
    private double latitude,longitude; // Latitude and longitude
    private double elevation;
    private float bearing,speed;
    private Criteria criteria; //Criteria
    private boolean gpsStatus = false;

    public boolean getGpsStatus(){
        return gpsStatus;
    }

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 10000; // 10 secs

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GeoController(Context context) {
        this.mContext = context;
        variablesMobile = VariablesMobile.getInstance();
        this.criteria = setMyCriteria();
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            if (location == null) {
                locationManager.requestLocationUpdates(
                        locationManager.getBestProvider(criteria,false),
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("Data type", locationManager.getBestProvider(criteria, false));
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        elevation = location.getAltitude();
                        speed = location.getSpeed();
                        bearing = location.getBearing();
                        Log.i("Bearing status",bearing + "");
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Criteria setMyCriteria(){
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        criteria.setSpeedRequired(true);
        criteria.setBearingRequired(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        return criteria;
    }

    /**
     * Start using GPS listener
     * Calling this function will start using GPS in your app.
     * */
    public void startUsingGPS(){
        if(locationManager != null) {
            locationManager.requestLocationUpdates(
                    locationManager.getBestProvider(criteria, false),
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }else{
            getLocation();
        }
    }


    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GeoController.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    public void comprobationPosition(){
        PositionTask positionTask = new PositionTask();
        positionTask.execute(new LatLong(latitude,longitude));
        boolean encontrado;
        try{
            encontrado = positionTask.get();
            if(!variablesMobile.getGoogleApiClient().isConnected()){
                if(!encontrado) {
                    Vibrator vibrator = (Vibrator)mContext.getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                }
            }
            String WEARABLE_DATA_PATH = "/wearable_data";
            DataMap dataMap = new DataMap();
            dataMap.putBoolean("onroute",encontrado);
            new DataTask(WEARABLE_DATA_PATH, dataMap).start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle(mContext.getString(R.string.alert_GPS_title));
        // Setting Dialog Message
        alertDialog.setMessage(mContext.getString(R.string.alert_GPS_text));
        // On pressing the Settings button.
        alertDialog.setPositiveButton(mContext.getString(R.string.alert_GPS_accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // On pressing the cancel button
        alertDialog.setNegativeButton(mContext.getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            longitude = location.getLongitude();
            variablesMobile.getProperties().setProperty("longitude",Double.toString(longitude));
            latitude = location.getLatitude();
            variablesMobile.getProperties().setProperty("latitude",Double.toString(latitude));
            elevation = location.getAltitude();
            speed = location.getSpeed();
            bearing = location.getBearing();
            if(variablesMobile.getMapView() != null){
                variablesMobile.getMapView().getModel().mapViewPosition.setCenter(
                        new LatLong(latitude,longitude));
                variablesMobile.getLayerController().removeMark();
                variablesMobile.getLayerController().setMark(latitude,longitude);
                variablesMobile.setWearMap(BitmapActions.createAsset());
                if(variablesMobile.getGoogleApiClient() != null && variablesMobile.getGoogleApiClient().isConnected()){
                    String WEARABLE_DATA_PATH = "/wearable_data";
                    DataMap dataMap = new DataMap();
                    dataMap.putDouble("latitude",latitude);
                    dataMap.putDouble("longitude",longitude);
                    dataMap.putDouble("elevation",elevation);
                    dataMap.putFloat("speed",speed);
                    dataMap.putFloat("bearing",bearing);
                    dataMap.putAsset("map",variablesMobile.getWearMap());
                    new DataTask(WEARABLE_DATA_PATH, dataMap).start();
                }
            }
            if(variablesMobile.getTrack() != null){
                comprobationPosition();
            }
            Log.d("Geo",latitude + ", " + longitude  + ", " + bearing);
        }
    }


    @Override
    public void onProviderDisabled(String provider) {
        if(!locationManager.isProviderEnabled(provider)){
            gpsStatus = false;
            showSettingsAlert();
        }
    }


    @Override
    public void onProviderEnabled(String provider) {
        if(locationManager.isProviderEnabled(provider)){
            gpsStatus = true;
            startUsingGPS();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if(status == GpsStatus.GPS_EVENT_STARTED){
            gpsStatus = true;
            startUsingGPS();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
