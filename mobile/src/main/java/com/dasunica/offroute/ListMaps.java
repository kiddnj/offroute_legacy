package com.dasunica.offroute;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dasunica.offroute.async_controller.DownloadTask;
import com.dasunica.offroute.async_controller.ListTask;
import com.dasunica.offroute.map_data.MapAdapter;
import com.dasunica.offroute.map_data.MapItem;

import java.util.ArrayList;
import java.util.Collections;


public class ListMaps extends Activity {

    private ProgressDialog mProgressDialog,listProgress;
    private ListView listView;
    private ArrayList<MapItem> listaMaps=null;
    private MapAdapter mapAdapter;
    private static ListMaps instance;

    public ProgressDialog getmProgressDialog(){
        return mProgressDialog;
    }
    public ProgressDialog getListProgressDialog(){
        return listProgress;
    }
    public ArrayList<MapItem> getListaMaps(){ return listaMaps; }
    public static ListMaps getInstance(){ return instance; }

    public void notificarCambios(){
        if(listaMaps != null && listaMaps.size() != 0){
            mapAdapter.notifyDataSetChanged();
        }
    }

    public void ordenarMapas(){
        Collections.sort(listaMaps);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listProgress = new ProgressDialog(ListMaps.this);
        listProgress.setMessage(getString(R.string.maps_spinner));
        listProgress.setIndeterminate(true);
        listProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        listProgress.setCancelable(false);
        if(listaMaps == null){
            listaMaps = new ArrayList<MapItem>();
        }
        setContentView(R.layout.activity_list_maps);
        instance = this;
        ListTask listTask = new ListTask();
        listTask.execute("PATH","offroute/");
        mapAdapter = new MapAdapter(this,listaMaps);
        listView = (ListView)findViewById(R.id.grid_list);
        listView.setAdapter(mapAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("name",listaMaps.get(i).getMapPath());
                String path = listaMaps.get(i).getMapPath();
                if(listaMaps.get(i).getMapPath().endsWith(".map")){
                    final ConnectivityManager connMgr = (ConnectivityManager)
                            ListMaps.this.getSystemService(ListMaps.this.CONNECTIVITY_SERVICE);
                    final android.net.NetworkInfo wifi =
                            connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    Log.i("Wifi state", wifi.isConnected() + "");
                    if(wifi.isConnected()){
                        mProgressDialog = new ProgressDialog(ListMaps.this);
                        mProgressDialog.setMessage(getString(R.string.downloading_spinner) + listaMaps.get(i).getMapName());
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mProgressDialog.setCancelable(true);

                        // execute this when the downloader must be fired
                        final DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(listaMaps.get(i).getMapPath());

                        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                downloadTask.cancel(true);
                            }
                        });
                    }else{
                        Toast.makeText(ListMaps.this, getString(R.string.wifi_error), Toast.LENGTH_LONG).show();
                    }
                }else{
                    ListMaps.getInstance().getListaMaps().clear();
                    ListTask listTask = new ListTask();
                    listTask.execute("PATH",path + "/");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
