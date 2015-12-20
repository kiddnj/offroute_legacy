package com.dasunica.offroute.controllers;

import com.dasunica.offroute.utils.VariablesMobile;

import org.mapsforge.core.model.LatLong;

/**
 * Created by fran on 18/11/14.
 */
public class PositionController {

    VariablesMobile variablesMobile;

    private double distance(LatLong latLong1,LatLong latLong2){
        double earthRadius = 6371; //in km, change to 3958.75 to mile output

        double dLat = Math.toRadians(latLong1.latitude - latLong2.latitude);
        double dLong = Math.toRadians(latLong1.longitude - latLong2.longitude);

        double sindLat = Math.sin(dLat/2);
        double sindLong = Math.sin(dLong/2);

        double a = Math.pow(sindLat,2) + Math.pow(sindLong,2) *
                Math.cos(Math.toRadians(latLong1.latitude)) *
                Math.cos(Math.toRadians(latLong1.longitude));

        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        return earthRadius * c;
    }

    public boolean comparePosition(LatLong latLong) {
        boolean foundPosition = false;
        int i = 0;
        while(i < variablesMobile.getTrack().getTrackpoints().size() && !foundPosition){
            if(distance(variablesMobile.getTrack().getTrackpoints().get(i),latLong) < 0.1){
                foundPosition = true;
            }else{
                i++;
            }
        }
        return foundPosition;
    }

    public PositionController(){
        variablesMobile = VariablesMobile.getInstance();
    }
}
