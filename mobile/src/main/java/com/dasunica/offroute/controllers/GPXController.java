package com.dasunica.offroute.controllers;

import android.util.Log;

import com.dasunica.offroute.R;
import com.dasunica.offroute.track_data.Track;
import com.dasunica.offroute.track_data.Trackpoint;
import com.dasunica.offroute.track_data.Waypoint;
import com.dasunica.offroute.utils.VariablesMobile;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by fran on 9/10/14.
 */
public class GPXController {

    protected Document dom;
    private VariablesMobile variablesMobile;

    public Track parseXmlFile(File file){
        //get the factory
        Track track = new Track();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {

            //Using factory get an instance of document builder
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            FileInputStream fileInputStream = new FileInputStream(file);

            //parse using builder to get DOM representation of the GPX file
            dom = documentBuilder.parse(fileInputStream);

            Element element = dom.getDocumentElement();

            //Parse the gpx file to track structure
            NodeList nodeListTrack = element.getElementsByTagName("trk");
            NodeList nodeListWaypoint = element.getElementsByTagName("wpt");
            NodeList nodeListTrackpoint = element.getElementsByTagName("trkpt");

            for(int i = 0;i < nodeListTrack.getLength();i++){
                track = readTrack(nodeListTrack.item(i));
            }

            ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
            for(int i = 0;i < nodeListWaypoint.getLength();i++){
                waypoints.add(readWaypoint(nodeListWaypoint.item(i)));
            }
            track.setWaypoints(waypoints);

            ArrayList<Trackpoint> trackpoints = new ArrayList<Trackpoint>();
            for(int i = 0;i < nodeListTrackpoint.getLength();i++){
                trackpoints.add(readTrackpoint(nodeListTrackpoint.item(i)));
            }
            track.setTrackpoints(trackpoints);

            Log.i("Ruta",track.getName());
            fileInputStream.close();
        }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
        }catch(SAXException se) {
            se.printStackTrace();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }finally{
            return track;
        }
    }

    private Waypoint readWaypoint(Node node){
        NamedNodeMap nodeMap = node.getAttributes();
        double latitude = Double.parseDouble(nodeMap.getNamedItem("lat").getTextContent());
        double longitude = Double.parseDouble((nodeMap.getNamedItem("lon").getTextContent()));
        double elevation = 0;
        String name = "",desc = "";
        NodeList nList = node.getChildNodes();
        for(int i = 0;i < nList.getLength();i++){
            if(nList.item(i).getNodeName().equals("ele")){
                elevation = Double.parseDouble(nList.item(i).getTextContent());
            }else if(nList.item(i).getNodeName().equals("name")){
                name = nList.item(i).getTextContent();
            }else if(nList.item(i).getNodeName().equals("desc")){
                desc = nList.item(i).getTextContent();
            }
        }
        LatLong latLong = new LatLong(latitude,longitude);
        Bitmap bitmap = variablesMobile.getLayerController().drawBitmap(R.drawable.ic_map_marker);
        return new Waypoint(latLong,bitmap,0,-bitmap.getHeight()/2,name,desc,elevation);
    }

    private Trackpoint readTrackpoint(Node node){
        NamedNodeMap nodeMap = node.getAttributes();
        double latitude = Double.parseDouble(nodeMap.getNamedItem("lat").getTextContent());
        double longitude = Double.parseDouble((nodeMap.getNamedItem("lon").getTextContent()));
        double elevation = 0;
        NodeList nList = node.getChildNodes();
        for(int i = 0;i < nList.getLength();i++){
            if(nList.item(i).getNodeName().equals("ele")){
                elevation = Double.parseDouble(nList.item(i).getTextContent());
            }
        }
        return new Trackpoint(latitude,longitude,elevation);
    }

    private Track readTrack(Node node){
        Track track = new Track();
        NodeList nList = node.getChildNodes();
        for(int i =0;i < nList.getLength();i++){
            if(nList.item(i).getNodeName().equals("name")){
                track.setName(nList.item(i).getTextContent());
            }else if(nList.item(i).getNodeName().equals("desc")){
                track.setDescription(nList.item(i).getTextContent());
            }
        }
        return track;
    }

    public GPXController(){
        variablesMobile = VariablesMobile.getInstance();
    }
}
