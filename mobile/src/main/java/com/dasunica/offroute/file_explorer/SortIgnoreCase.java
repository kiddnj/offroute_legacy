package com.dasunica.offroute.file_explorer;

import java.io.File;
import java.util.Comparator;

/**
 * Created by fran on 5/10/14.
 */
public class SortIgnoreCase implements Comparator<File> {

    @Override
    public int compare(File lhs, File rhs) {
        return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
    }

}
