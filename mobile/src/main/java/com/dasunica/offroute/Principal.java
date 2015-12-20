package com.dasunica.offroute;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidResourceBitmap;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dasunica.offroute.file_explorer.FileChooserDialog;
import com.dasunica.offroute.async_controller.DataTask;
import com.dasunica.offroute.controllers.FileController;
import com.dasunica.offroute.controllers.LayerController;
import com.dasunica.offroute.utils.BitmapActions;
import com.dasunica.offroute.utils.PropertiesManager;
import com.dasunica.offroute.utils.VariablesMobile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Principal extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private VariablesMobile variablesMobile;
    private FileChooserDialog fileChooserDialog;
    private static Principal instance;
    private Intent servicio;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;

    public static Principal getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        AndroidGraphicFactory.createInstance(this.getApplication());

        variablesMobile = VariablesMobile.getInstance();

        variablesMobile.setGoogleApiClient(new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build());

        variablesMobile.setMapView(new MapView(this));
        variablesMobile.setLayerController(new LayerController(this));
        variablesMobile.setFileController(new FileController());

        setContentView(this.variablesMobile.getMapView());

        variablesMobile.getMapView().setClickable(true);
        variablesMobile.getMapView().getMapScaleBar().setVisible(true);
        variablesMobile.getMapView().setBuiltInZoomControls(true);
        variablesMobile.getMapView().getMapZoomControls().setZoomLevelMin((byte) 10);
        variablesMobile.getMapView().getMapZoomControls().setZoomLevelMax((byte) 20);

        // create a tile cache of suitable size
        variablesMobile.setTileCache(AndroidUtil.createTileCache(this, "mapcache",
                variablesMobile.getMapView().getModel().displayModel.getTileSize(), 1f,
                variablesMobile.getMapView().getModel().frameBufferModel.getOverdrawFactor()));


        variablesMobile.getGoogleApiClient().connect();
        servicio = new Intent(this, ServicePrincipal.class);
        startService(servicio);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.principal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        fileChooserDialog = new FileChooserDialog(this);
        switch (item.getItemId()) {
            case R.id.action_map_loader:
                fileChooserDialog.createFileChooserDialog(".map");
                return true;
            case R.id.action_route_loader:
                if (variablesMobile.getTrack() != null) {
                    variablesMobile.getLayerController().removeTrack(variablesMobile.getTrack());
                }
                fileChooserDialog.createFileChooserDialog(".gpx");
                return true;
            case R.id.action_route_info:
                if (variablesMobile.getTrack() != null) {
                    variablesMobile.getTrack().setDialog(this);
                } else {
                    Toast toast = Toast.makeText(this, getString(R.string.notrack_info), Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            case R.id.action_exit:
                exit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // tile renderer layer using internal render theme
        if(variablesMobile.getProperties().getProperty("mapFile") == null ||
                variablesMobile.getProperties().getProperty("mapFile").equals("")) {
            if(fileChooserDialog == null){
                fileChooserDialog = new FileChooserDialog(this);
                fileChooserDialog.createFileChooserDialog(".map");
            }
        }else{
            if(variablesMobile.getTileRendererLayer() == null){
                variablesMobile.getLayerController().createTileRender();
            }
        }

        /*if(!variablesMobile.getGoogleApiClient().isConnected()){
            variablesMobile.getGoogleApiClient().connect();
        }
        if(variablesMobile.getProperties() == null){
            variablesMobile.setProperties(new PropertiesManager().readProperties("offroute.xml"));
        }

        double latitude = Double.parseDouble(variablesMobile.getProperties().getProperty("latitude"));
        double longitude = Double.parseDouble(variablesMobile.getProperties().getProperty("longitude"));
        Log.d("Geo", latitude + ", " + longitude);

        try {
            data();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // tile renderer layer using internal render theme
        if(variablesMobile.getProperties().getProperty("mapFile") == null ||
                variablesMobile.getProperties().getProperty("mapFile").equals("")) {
            if(fileChooserDialog == null){
                fileChooserDialog = new FileChooserDialog(this);
                fileChooserDialog.createFileChooserDialog(".map");
            }
        }else{
            variablesMobile.getLayerController().createTileRender();
        }

        //Centering the map by geolocation (latitude GO FIRST, ANIMALMOTHER!!!!)
        variablesMobile.getLayerController().setUpMap(latitude,longitude);
        if(!variablesMobile.getGoogleApiClient().isConnected()){
            variablesMobile.getGoogleApiClient().connect();
        }*/

    }

    @Override
    protected void onResume(){
        super.onResume();
        /*variablesMobile.getGeoController().startUsingGPS();
        double latitude = Double.parseDouble(variablesMobile.getProperties().getProperty("latitude"));
        double longitude = Double.parseDouble(variablesMobile.getProperties().getProperty("longitude"));
        variablesMobile.getLayerController().setUpMap(latitude,longitude);
        if(variablesMobile.getTrack() != null){
            variablesMobile.getLayerController().setTrack(variablesMobile.getTrack());
        }*/mNotifyMgr.cancelAll();

    }

    @Override
    protected void onPause(){
        super.onPause();
        /*variablesMobile.getGeoController().stopUsingGPS();
        if(variablesMobile.getTrack() != null){
            variablesMobile.getLayerController().removeTrack(variablesMobile.getTrack());
        }*/

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text));
        Intent resultIntent = new Intent(this, Principal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotifyMgr.notify(001,mBuilder.build());

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            PropertiesManager.writeProperties("offroute.xml", variablesMobile.getProperties());
        }catch (Exception e){
            e.printStackTrace();
        }
        /*if(variablesMobile.getTileRendererLayer() != null){
            variablesMobile.getMapView().getLayerManager().getLayers()
                    .remove(variablesMobile.getTileRendererLayer());
            variablesMobile.getTileRendererLayer().onDestroy();
        }
        variablesMobile.getGeoController().stopUsingGPS();

        if(variablesMobile.getGoogleApiClient() != null && variablesMobile.getGoogleApiClient().isConnected()){
            variablesMobile.getGoogleApiClient().disconnect();
        }
        new DataTask("/kill_activity", null).start();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNotifyMgr.cancelAll();
        new DataTask("/kill_activity", null).start();
        stopService(servicio);
        variablesMobile.getGoogleApiClient().disconnect();
        variablesMobile.getTileCache().destroy();
        variablesMobile.getMapView().getModel().mapViewPosition.destroy();
        variablesMobile.getMapView().destroy();
        AndroidResourceBitmap.clearResourceBitmaps();
    }

    @Override
    public void onConnected(Bundle bundle) {
        new DataTask("/start_activity", null).start();
        /*try {
            data();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("PhoneActivity", "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("PhoneActivity", "onConnectionFailed: " + connectionResult);
    }

    private void data() throws IOException, ExecutionException, InterruptedException {
        if(variablesMobile.getGoogleApiClient() != null && variablesMobile.getGoogleApiClient().isConnected()){
            String WEARABLE_DATA_PATH = "/wearable_data";
            DataMap dataMap = new DataMap();
            dataMap.putDouble("latitude", variablesMobile.getGeoController().getLatitude());
            dataMap.putDouble("longitude", variablesMobile.getGeoController().getLongitude());
            dataMap.putAsset("map", variablesMobile.getWearMap());
            new DataTask(WEARABLE_DATA_PATH, dataMap).start();
        }
    }

    private void exit(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle(getString(R.string.alert_exit_title));
        // Setting Dialog Message
        alertDialog.setMessage(getString(R.string.alert_exit_text));
        // On pressing the Settings button.
        alertDialog.setPositiveButton(getString(R.string.alert_accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                try{
                    new PropertiesManager().writeProperties("offroute.xml", variablesMobile.getProperties());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(servicio != null){
                    stopService(servicio);
                }
                new DataTask("/kill_activity", null).start();
                finish();
            }
        });
        // On pressing the cancel button
        alertDialog.setNegativeButton(getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();

    }
}
