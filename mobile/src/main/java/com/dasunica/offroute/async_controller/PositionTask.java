package com.dasunica.offroute.async_controller;

import android.os.AsyncTask;

import com.dasunica.offroute.controllers.PositionController;

import org.mapsforge.core.model.LatLong;

/**
 * Created by fran on 3/11/14.
 */
public class PositionTask extends AsyncTask<LatLong,Void,Boolean>{

    @Override
    protected Boolean doInBackground(LatLong... params) {
        PositionController positionController = new PositionController();
        return positionController.comparePosition(params[0]);
    }
}
