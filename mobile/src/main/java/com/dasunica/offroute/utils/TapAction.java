package com.dasunica.offroute.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.dasunica.offroute.R;
import com.dasunica.offroute.track_data.Waypoint;


/**
 * Created by fran on 15/10/14.
 */
public class TapAction implements Runnable{

    private AlertDialog alertDialog;
    private Context context;
    private Waypoint wpt;

    private void setDialog(Context context, Waypoint wpt){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.waypoint) + ": " + wpt.getName().toLowerCase());
        String message = wpt.getLatlong().toString() +
                "\n" + context.getString(R.string.tap_elevation) + ": " + wpt.getElevation();
        if(!wpt.getDesc().equals("")){
            message += "\n\n" + context.getString(R.string.tap_description) + ": \n" + wpt.getDesc();
        }
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,context.getString(R.string.button_close),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void run() {
        setDialog(context,wpt);
    }

    public TapAction(Context context, Waypoint wpt){
        this.context = context;
        this.wpt = wpt;
    }
}
