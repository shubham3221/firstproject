package com.example.s_tools.tools;

import java.text.DecimalFormat;

public class Kbtomb {
    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( (((size/1024.0)/1024.0)/1024.0)>1 ) {
            hrSize = dec.format((((size/1024.0)/1024.0)/1024.0)).concat(" GB");
        } else if ( ((size/1024.0)/1024.0)>1 ) {
            hrSize = dec.format(((size/1024.0)/1024.0)).concat(" MB");
        } else if ( size/1024.0>1 ) {
            hrSize = dec.format(size/1024.0).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}

