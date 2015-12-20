package com.dasunica.offroute.controllers;

import com.dasunica.offroute.async_controller.GPXToTrackTask;
import com.dasunica.offroute.utils.VariablesMobile;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by fran on 5/10/14.
 */
public class FileController {

    private VariablesMobile variablesMobile;

    public void loadMap(File file){
        variablesMobile.getProperties().setProperty("mapFile", file.getAbsolutePath());
        variablesMobile.getLayerController().createTileRender();
    }



    public void loadRoute(File file){
        try {
            GPXToTrackTask gpxToTrackTask = new GPXToTrackTask();
            gpxToTrackTask.execute(file);
            variablesMobile.setTrack(gpxToTrackTask.get());
            variablesMobile.getLayerController().setTrack(variablesMobile.getTrack());
            if(variablesMobile.getGeoController() != null){
                variablesMobile.getGeoController().comprobationPosition();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void loadFile(File file){
        if(file.getName().endsWith(".gpx")){
            loadRoute(file);
        }else{
            loadMap(file);
        }
    }

    public FileController(){
        variablesMobile = VariablesMobile.getInstance();
    }

}
