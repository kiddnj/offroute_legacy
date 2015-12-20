package com.dasunica.offroute;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class ListenerService extends WearableListenerService {

    private static final String WEARABLE_DATA_PATH = "/wearable_data";

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        DataMap dataMap;
        for(DataEvent event:dataEvents){
            if(event.getType() == DataEvent.TYPE_CHANGED){
                String path = event.getDataItem().getUri().getPath();
                Log.i("Path",path);
                if(path.equalsIgnoreCase(WEARABLE_DATA_PATH)){
                    dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                    Log.i("Data",dataMap + "");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra("Payload",dataMap.toBundle());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }else{
                    Intent intent = new Intent(this,Wear_Principal.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/start_activity")) {
            Intent intent = new Intent( this, Wear_Principal.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(messageEvent.getPath().equals("/kill_activity")){
            Wear_Principal.getInstance().finish();
        }else{
            super.onMessageReceived(messageEvent);
        }
    }
}