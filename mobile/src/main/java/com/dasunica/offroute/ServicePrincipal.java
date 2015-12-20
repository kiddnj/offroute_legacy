package com.dasunica.offroute;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.dasunica.offroute.async_controller.DataTask;
import com.dasunica.offroute.controllers.GeoController;
import com.dasunica.offroute.utils.BitmapActions;
import com.dasunica.offroute.utils.PropertiesManager;
import com.dasunica.offroute.utils.VariablesMobile;
import com.google.android.gms.wearable.DataMap;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by fran on 23/02/15.
 */
public class ServicePrincipal extends Service{

    private VariablesMobile variablesMobile;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service","Arrancando servicio");
        variablesMobile = VariablesMobile.getInstance();
        variablesMobile.setGeoController(new GeoController(Principal.getInstance()));
        variablesMobile.getGeoController().startUsingGPS();
        if(!variablesMobile.getGoogleApiClient().isConnected()){
            variablesMobile.getGoogleApiClient().connect();
        }
        if(variablesMobile.getProperties() == null){
            variablesMobile.setProperties(PropertiesManager.readProperties("offroute.xml"));
        }

        double latitude = Double.parseDouble(variablesMobile.getProperties().getProperty("latitude"));
        double longitude = Double.parseDouble(variablesMobile.getProperties().getProperty("longitude"));
        Log.d("Geo", latitude + ", " + longitude);

        //Centering the map by geolocation (latitude GO FIRST, ANIMALMOTHER!!!!)
        variablesMobile.getLayerController().setUpMap(latitude,longitude);
        if(!variablesMobile.getGoogleApiClient().isConnected()){
            variablesMobile.getGoogleApiClient().connect();
        }

        try {
            variablesMobile.setWearMap(BitmapActions.createAsset());
            Thread.sleep(2500);
            data();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return startId;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service","Parando servicio");
        if(variablesMobile.getTileRendererLayer() != null){
            variablesMobile.getMapView().getLayerManager().getLayers()
                    .remove(variablesMobile.getTileRendererLayer());
            variablesMobile.getTileRendererLayer().onDestroy();
        }
        variablesMobile.getGeoController().stopUsingGPS();

        if(variablesMobile.getGoogleApiClient() != null && variablesMobile.getGoogleApiClient().isConnected()){
            variablesMobile.getGoogleApiClient().disconnect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void data() throws IOException, ExecutionException, InterruptedException {
        if(variablesMobile.getGoogleApiClient() != null && variablesMobile.getGoogleApiClient().isConnected()){
            String WEARABLE_DATA_PATH = "/wearable_data";
            DataMap dataMap = new DataMap();
            dataMap.putAsset("map", variablesMobile.getWearMap());
            new DataTask(WEARABLE_DATA_PATH, dataMap).start();
        }
    }
}
