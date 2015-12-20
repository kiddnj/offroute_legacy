package com.dasunica.offroute.controllers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.dasunica.offroute.R;
import com.dasunica.offroute.utils.BitmapActions;
import com.dasunica.offroute.track_data.Track;
import com.dasunica.offroute.track_data.Trackpoint;
import com.dasunica.offroute.track_data.Waypoint;
import com.dasunica.offroute.utils.Position;
import com.dasunica.offroute.utils.TapAction;
import com.dasunica.offroute.utils.VariablesMobile;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.List;

/**
 * Created by fran on 4/10/14.
 */
public class LayerController {

    private static Context context;
    private Polyline polyline;

    private VariablesMobile variablesMobile = VariablesMobile.getInstance();

    public LayerController(Context context){
        this.context = context;
    }

    private Polyline createPolyline(){
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        paint.setStyle(Style.STROKE);

        return new Polyline(paint,AndroidGraphicFactory.INSTANCE);
    }

    public static Bitmap drawBitmap(int drawableObj){
        Drawable drawObj = context.getResources().getDrawable(drawableObj);
        return AndroidGraphicFactory.convertToBitmap(drawObj);
    }

    public void removeMark(){
        variablesMobile.getMapView().getLayerManager().getLayers().
                remove(variablesMobile.getMapView().getLayerManager().getLayers().indexOf(variablesMobile.getMarker()));
    }

    public void setMark(double latitude,double longitude){
        Bitmap mark = drawBitmap(R.drawable.ic_marker);
        this.variablesMobile.setMarker(new Position(new LatLong(latitude,longitude),mark, 0,0));
        this.variablesMobile.getMapView().getLayerManager().getLayers().add(variablesMobile.getMarker());
    }

    public void setTrack(Track track){
        if(track != null){
            for(Waypoint waypoint:track.getWaypoints()){
                waypoint.setOnTapAction(new TapAction(context,waypoint));
                variablesMobile.getMapView().getLayerManager().getLayers().add(waypoint);
            }
            if(polyline == null) {
                polyline = createPolyline();
            }
            List<LatLong> coordinates = polyline.getLatLongs();
            for(Trackpoint trkpt:track.getTrackpoints()){
                coordinates.add(trkpt);
            }
            variablesMobile.getMapView().getLayerManager().getLayers().add(polyline);
        }
    }

    public void removeTrack(Track track){
        for(Waypoint wpt:track.getWaypoints()){
            variablesMobile.getMapView().getLayerManager().getLayers().
                    remove(variablesMobile.getMapView().getLayerManager().getLayers().indexOf(wpt));
        }
        polyline.getLatLongs().removeAll(polyline.getLatLongs());
        variablesMobile.getMapView().getLayerManager().getLayers().
                remove(variablesMobile.getMapView().getLayerManager().getLayers().indexOf(polyline));
    }

    public void createTileRender(){
        if(variablesMobile.getTileRendererLayer() == null){
            variablesMobile.setTileRendererLayer(new TileRendererLayer(variablesMobile.getTileCache(),
                    variablesMobile.getMapView().getModel().mapViewPosition,false,
                    AndroidGraphicFactory.INSTANCE));
        }

        variablesMobile.getTileRendererLayer().setMapFile
                (new File(variablesMobile.getProperties().getProperty("mapFile")));
        variablesMobile.getTileRendererLayer().setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        variablesMobile.getMapView().getModel().mapViewPosition.setZoomLevel((byte) Integer.parseInt
                (variablesMobile.getProperties().getProperty("zoom")));

        // only once a layer is associated with a mapView the rendering starts
        if(variablesMobile.getTileRendererLayer() != null){
            variablesMobile.getMapView().getLayerManager().getLayers().
                    add(variablesMobile.getTileRendererLayer());

        }
        setUpMap(Double.parseDouble(variablesMobile.getProperties().getProperty("latitude")),
                Double.parseDouble(variablesMobile.getProperties().getProperty("latitude")));
        variablesMobile.setWearMap(BitmapActions.createAsset());
    }

    public void setUpMap(double latitude,double longitude){
        variablesMobile.getMapView().getModel().mapViewPosition.setCenter(new LatLong(latitude,
                longitude));
        if(variablesMobile.getMarker() != null){
            variablesMobile.getLayerController().removeMark();
        }
        variablesMobile.getLayerController().setMark(latitude, longitude);
    }
}
