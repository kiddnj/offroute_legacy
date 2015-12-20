package com.dasunica.offroute.file_explorer;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by fran on 5/10/14.
 */
public class ExtensionFilter implements FileFilter {

    private String extension;

    public ExtensionFilter(String extension){
        this.extension = extension;
    }

    @Override
    public boolean accept(File pathname) {
        if(pathname.isDirectory()){
            return true;
        }else if(pathname.getName().endsWith(extension)){
            return true;
        }else {
            return false;
        }
    }
}
