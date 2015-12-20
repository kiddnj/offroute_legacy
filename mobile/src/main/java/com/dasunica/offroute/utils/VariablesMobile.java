package com.dasunica.offroute.utils;

import com.dasunica.offroute.controllers.FileController;
import com.dasunica.offroute.controllers.GeoController;
import com.dasunica.offroute.controllers.LayerController;
import com.dasunica.offroute.track_data.Track;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;

import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;

import java.util.Properties;

/**
 * Created by fran on 20/10/14.
 */
public class VariablesMobile {

    private MapView mapView;
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;
    private GeoController geoController;
    private Properties properties;
    private LayerController layerController;
    private FileController fileController;
    private Track track;
    private Position marker;
    private GoogleApiClient googleApiClient;
    private Asset wearMap;

    private static VariablesMobile variablesMobile;

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public TileCache getTileCache() {
        return tileCache;
    }

    public void setTileCache(TileCache tileCache) {
        this.tileCache = tileCache;
    }

    public TileRendererLayer getTileRendererLayer() {
        return tileRendererLayer;
    }

    public void setTileRendererLayer(TileRendererLayer tileRendererLayer) {
        this.tileRendererLayer = tileRendererLayer;
    }

    public GeoController getGeoController() {
        return geoController;
    }

    public void setGeoController(GeoController geoController) {
        this.geoController = geoController;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public LayerController getLayerController() {
        return layerController;
    }

    public void setLayerController(LayerController layerController) {
        this.layerController = layerController;
    }

    public FileController getFileController() {
        return fileController;
    }

    public void setFileController(FileController fileController) {
        this.fileController = fileController;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Position getMarker() {
        return marker;
    }

    public void setMarker(Position marker) {
        this.marker = marker;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public Asset getWearMap() {
        return wearMap;
    }

    public void setWearMap(Asset wearMap) {
        this.wearMap = wearMap;
    }

    private VariablesMobile(){}

    public static VariablesMobile getInstance(){
        if(variablesMobile == null){
            variablesMobile = new VariablesMobile();
        }
        return variablesMobile;
    }

    public void destroy(){
        variablesMobile = null;
    }
}
