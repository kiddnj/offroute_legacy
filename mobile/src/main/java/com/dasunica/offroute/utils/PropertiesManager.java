package com.dasunica.offroute.utils;

import android.os.Environment;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by fran on 16/10/14.
 */
public class PropertiesManager {

    public static Properties readProperties(String file){
        Properties properties = new Properties();
        try{
            InputStream inputStream = new FileInputStream(Environment
                    .getExternalStorageDirectory() + "/offroute/" + file);
            properties.loadFromXML(inputStream);
            inputStream.close();
        }catch (Exception e){
            //e.printStackTrace();
            Log.i("Info", "Cargando valores predeterminados");
            properties.setProperty("latitude",Double.toString(41.656301));
            properties.setProperty("longitude",Double.toString(-0.878705));
            properties.setProperty("zoom",Integer.toString(15));
            properties.setProperty("path","http://maps.dasunica.com/");
        }finally{
            return properties;
        }
    }

    public static void writeProperties(String file, Properties properties) throws IOException{
        FileOutputStream fos = new FileOutputStream(Environment
                .getExternalStorageDirectory() + "/offroute/" + file);
        properties.storeToXML(fos, "Propiedades de Offroute");
        fos.close();
    }
}