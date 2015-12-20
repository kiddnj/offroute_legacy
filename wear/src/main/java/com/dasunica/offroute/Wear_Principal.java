package com.dasunica.offroute;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dasunica.offroute.async_task.BitmapTask;
import com.google.android.gms.wearable.Asset;

public class Wear_Principal extends Activity{

    private ImageView mapImage;
    private Bitmap background;
    private double latitude,longitude,elevation;
    private float speed,bearing;
    private MessageReceiver messageReceiver;
    private static Wear_Principal instance;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;

    public static Wear_Principal getInstance(){
        return instance;
    }

    public ImageView getMapImage(){
        return mapImage;
    }

    public void setBitmap(Bitmap bitmap){
        this.background = bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setContentView(R.layout.round_activity_wear__principal);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mapImage = (ImageView)findViewById(R.id.mapImage);
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notificaton_text))
                        .extend(new NotificationCompat.WearableExtender().setBackground(background));
        Intent resultIntent = new Intent(this, Wear_Principal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotifyMgr.notify(001,mBuilder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotifyMgr.cancelAll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Death","Offroute en la UVI");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mNotifyMgr.cancelAll();
        Log.i("Death","RIP Offroute");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = "";
            if(intent.hasExtra("Payload")){
                Bundle payload = intent.getBundleExtra("Payload");
                // Display message in UI
                if(payload.containsKey("latitude")){
                    latitude =  payload.getDouble("latitude");
                    message += "Latitude: " + latitude + "\n";
                    Log.i("latitude",latitude + "");
                }
                if(payload.containsKey("longitude")){
                    longitude = payload.getDouble("longitude");
                    message += "Longitude: " + longitude + "\n";
                    Log.i("longitude",longitude + "");
                }
                if(payload.containsKey("elevation")){
                    elevation = payload.getDouble("elevation");
                    message += "Elevation: " + elevation + "\n";
                    Log.i("elevation",elevation + "");
                }
                if(payload.containsKey("speed")){
                    speed = payload.getFloat("speed");
                    message += "Speed: " + speed + "\n";
                    Log.i("speed",speed + "");
                }
                if(payload.containsKey("bearing")){
                    bearing = payload.getFloat("bearing");
                    message += "Bearing: " + bearing + "\n";
                    Log.i("bearing",bearing + "");
                }
                if(payload.containsKey("map")){
                    try{
                        Log.i("Asset",payload.get("map").toString());
                        BitmapTask bitmapTask = new BitmapTask();
                        bitmapTask.execute((Asset)payload.get("map"));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if(payload.containsKey("onroute")){
                    boolean encontrado = payload.getBoolean("onroute");
                    Log.i("onroute",encontrado + "");
                    if(!encontrado){
                        Vibrator vibrator = (Vibrator)Wear_Principal.getInstance().getSystemService(VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                    }
                }
            }
        }
    }
}
