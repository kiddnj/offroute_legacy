package com.dasunica.offroute.map_data;

/**
 * Created by fran on 19/01/15.
 */
public class MapItem implements Comparable<MapItem>{

    private String mapName;
    private String mapPath;

    public String getMapName() {
        return mapName;
    }

    public String getMapPath() {
        return mapPath;
    }

    public MapItem(String mapName,String mapPath){
        this.mapName = mapName;
        this.mapPath = mapPath;
    }

    @Override
    public int compareTo(MapItem mapItem) {
        return this.mapName.compareTo(mapItem.getMapName());
    }
}
