package com.dasunica.offroute.track_data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

/**
 * Created by fran on 7/10/14.
 */
public class Track {

    private ArrayList<Trackpoint> trackpoints;
    private ArrayList<Waypoint> waypoints;
    private String name,description;
    private AlertDialog alertDialog;

    public Track(String name, String description) {
        this.name = name;
        this.description = description;
        this.trackpoints = new ArrayList<Trackpoint>();
        this.waypoints = new ArrayList<Waypoint>();
    }

    public Track(){
        this.trackpoints = new ArrayList<Trackpoint>();
        this.waypoints = new ArrayList<Waypoint>();
    }

    public ArrayList<Trackpoint> getTrackpoints() {
        return trackpoints;
    }

    public void setTrackpoints(ArrayList<Trackpoint> trackpoints) {
        this.trackpoints = trackpoints;
    }

    public ArrayList<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDialog(Context context){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(getName());
        alertDialog.setMessage(getDescription());
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                "Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
