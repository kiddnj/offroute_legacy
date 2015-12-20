package com.dasunica.offroute;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;

import com.dasunica.offroute.utils.PropertiesManager;
import com.dasunica.offroute.utils.VariablesMobile;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class Launcher extends Activity {

    private VariablesMobile variablesMobile;
    private Timer timer;

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variablesMobile = VariablesMobile.getInstance();

        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launcher);
        Log.i("External Storage",Environment.isExternalStorageRemovable() + "");
        Log.i("Emulated Storage",Environment.isExternalStorageEmulated() + "");

        if(Environment.isExternalStorageRemovable() || Environment.isExternalStorageEmulated()){
            variablesMobile.setProperties(PropertiesManager.readProperties("offroute.xml"));
            //Search the directory, create if not exists
            File root = new File(Environment.getExternalStorageDirectory() + "/offroute/");
            File maps = new File(Environment.getExternalStorageDirectory() + "/offroute/maps/");
            File track = new File(Environment.getExternalStorageDirectory() + "/offroute/tracks/");
            if(root.exists()){
                if(!maps.exists()){
                    maps.mkdirs();
                }else if(!track.exists()){
                    track.mkdirs();
                }
            }else{
                root.mkdirs();
                maps.mkdirs();
                track.mkdirs();
            }

            if(new File(Environment.getExternalStorageDirectory() + "/offroute/maps/")
                    .list().length == 0){
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent = new Intent().setClass(Launcher.this,ListMaps.class);
                        startActivity(intent);
                        finish();
                    }
                };
                timer = new Timer();
                timer.schedule(task,SPLASH_SCREEN_DELAY);

            }else{
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent().setClass(Launcher.this, Principal.class);
                        startActivity(mainIntent);
                        finish();
                    }
                };
                // Simulate a long loading process on application startup.
                timer = new Timer();
                timer.schedule(task, SPLASH_SCREEN_DELAY);
            }
        }else{
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.alert_noSD_title));
            // Setting Dialog Message
            alertDialog.setMessage(getString(R.string.alert_SD_text));
            // On pressing the Settings button.
            alertDialog.setPositiveButton(getString(R.string.alert_accept), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    finish();
                }
            });
            // Showing Alert Message
            alertDialog.show();
        }
    }
}
