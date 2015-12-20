package com.dasunica.offroute.async_controller;

import android.os.AsyncTask;

import com.dasunica.offroute.controllers.GPXController;
import com.dasunica.offroute.track_data.Track;

import java.io.File;

/**
 * Created by fran on 8/10/14.
 */
public class GPXToTrackTask extends AsyncTask<File,Void,Track>{

    @Override
    protected Track doInBackground(File... params) {
        GPXController gpxController = new GPXController();
        return gpxController.parseXmlFile(params[0]);
    }
}
