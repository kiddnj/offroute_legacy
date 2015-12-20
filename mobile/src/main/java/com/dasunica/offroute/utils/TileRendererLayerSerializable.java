package com.dasunica.offroute.utils;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.io.Serializable;

/**
 * Created by fran on 17/11/14.
 */
public class TileRendererLayerSerializable extends TileRendererLayer implements Serializable {

    public TileRendererLayerSerializable(TileCache tileCache, MapViewPosition mapViewPosition,
                                         boolean isTransparent, GraphicFactory graphicFactory) {
        super(tileCache, mapViewPosition, isTransparent, graphicFactory);
    }

    @Override
    public MapDatabase getMapDatabase() {
        return super.getMapDatabase();
    }

    @Override
    public File getMapFile() {
        return super.getMapFile();
    }

    @Override
    public float getTextScale() {
        return super.getTextScale();
    }

    @Override
    public XmlRenderTheme getXmlRenderTheme() {
        return super.getXmlRenderTheme();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public synchronized void setDisplayModel(DisplayModel displayModel) {
        super.setDisplayModel(displayModel);
    }

    @Override
    public void setMapFile(File mapFile) {
        super.setMapFile(mapFile);
    }

    @Override
    public void setTextScale(float textScale) {
        super.setTextScale(textScale);
    }

    @Override
    public void setXmlRenderTheme(XmlRenderTheme xmlRenderTheme) {
        super.setXmlRenderTheme(xmlRenderTheme);
    }

    @Override
    protected RendererJob createJob(Tile tile) {
        return super.createJob(tile);
    }

    @Override
    protected void onAdd() {
        super.onAdd();
    }

    @Override
    protected void onRemove() {
        super.onRemove();
    }
}
