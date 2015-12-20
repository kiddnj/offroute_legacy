package com.dasunica.offroute.async_controller;

import android.os.AsyncTask;

import com.dasunica.offroute.ListMaps;
import com.dasunica.offroute.map_data.MapItem;
import com.dasunica.offroute.utils.Post;
import com.dasunica.offroute.utils.VariablesMobile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fran on 18/01/15.
 */
public class ListTask extends AsyncTask<String,Integer,Boolean> {

    private static JSONArray datos;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ListMaps.getInstance().getListProgressDialog().show();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try{
            ArrayList<String> params = new ArrayList<String>();
            publishProgress(0);
            params.add(strings[0]);
            params.add(strings[1]);
            Post post = new Post();
            publishProgress(50);
            datos = post.getServerData(params, VariablesMobile.getInstance().getProperties()
                    .getProperty("path") + "maps.php");
            publishProgress(100);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ListMaps.getInstance().getListProgressDialog().setIndeterminate(false);
        ListMaps.getInstance().getListProgressDialog().setMax(100);
        ListMaps.getInstance().getListProgressDialog().setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        ListMaps.getInstance().getListProgressDialog().dismiss();
        if(datos != null && datos.length() != 0){
            try{
                for(int i = 0;i < datos.length();i++){
                    JSONObject jsonObject = datos.getJSONObject(i);
                    String mapName = jsonObject.getString("file");
                    String mapPath = jsonObject.getString("ruta");
                    ListMaps.getInstance().getListaMaps().add(new MapItem(mapName,mapPath));
                }
                ListMaps.getInstance().ordenarMapas();
                ListMaps.getInstance().notificarCambios();
            }catch(Exception e){

            }
        }
    }
}
