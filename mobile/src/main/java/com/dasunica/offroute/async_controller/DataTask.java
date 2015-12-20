package com.dasunica.offroute.async_controller;


import android.util.Log;

import com.dasunica.offroute.utils.VariablesMobile;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by fran on 6/11/14.
 */
public class DataTask extends Thread {

    String path;
    DataMap data;
    VariablesMobile variablesMobile;

    public DataTask(String p,DataMap data){
        path = p;
        this.data = data;
        variablesMobile = VariablesMobile.getInstance();
    }

    @Override
    public void run() {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.
                getConnectedNodes(variablesMobile.getGoogleApiClient()).await();
        for(Node node : nodes.getNodes()){
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(path);
            PutDataRequest putDataRequest = null;
            DataApi.DataItemResult result;
            if(data != null){
                putDataMapRequest.getDataMap().putAll(data);
                putDataRequest = putDataMapRequest.asPutDataRequest();
                result =
                        Wearable.DataApi.putDataItem(variablesMobile.getGoogleApiClient(),
                                putDataRequest).await();
            }else{
                result = null;
                Log.i("Info","Send signal to " + node.getDisplayName());
                Wearable.MessageApi.sendMessage(variablesMobile.getGoogleApiClient(),"",path,null).await();
            }
            if(result != null && result.getStatus().isSuccess()){
                Log.i("Info","Send " + data  + " to " + node.getDisplayName());
            }
        }
    }
}
