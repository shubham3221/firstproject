package com.example.s_tools.tools;

import android.util.Log;

import java.util.Locale;

public class ContentSplit {

    public static final String TAG = "mtag";
    public static String makeFirstLineBold(String s){
        if (s == null || s.length() == 0) {
            return s;
        }
        String lines[] = s.substring(3).split("\n");
        String fl = String.format(Locale.ENGLISH, "<b>%s</b><br>", lines[0]);
        Log.i(TAG, "makeFirstLineBold: "+fl);
        StringBuilder sb = new StringBuilder();
        sb.append(fl);
        for(int i = 1; i < lines.length; i++){
            if (i==lines.length-1){
                Log.i(TAG, "makeFirstLineBold: called");
                sb.append(lines[i]);
            }else{
                sb.append(lines[i] + "<br>");
            }
        }
        return sb.toString().trim();
    }
    public static String southmovieContent(String s){
        s.replace("southfreak","Buddy");
        try {
            if (s.contains("Released")){
                Log.e(TAG, "movieContent: first" );

                if (s.contains("IMDb")){
                    Log.e(TAG, "movieContent: matched" );
                    String qualities[] = s.split("IMDb");
                    String q = qualities[1];
                    if (!s.contains("Stars")){
                        return "";
                    }
                    String language[] = q.split("Stars");
                    return language[0];
                }
                String qualities[] = s.split("IMDB");
                String q = qualities[1];
                if (!s.contains("Languages")){
                    return "";
                }
                String language[] = q.split("Languages");
                return language[0];
            }
            if (s.contains("Title")){
                Log.e(TAG, "movieContent: sec" );
                String imDbs[] = s.split("Title:");
                String a = imDbs[1];
                String genres[] = a.split("All Genres");
                if (!s.contains("All Genres")){
                    return "";
                }
                return genres[0];
            }
            if (s.contains("File Size:")){
                if (s.contains("IMDb")){
                    String imDbs[] = s.split("IMDb");
                    String a = imDbs[1];
                    String imDbs2[] = a.split("Stars");
                    if (!s.contains("Stars")){
                        return "";
                    }
                    return imDbs2[0];
                }
                Log.e(TAG, "movieContent: third" );
                String imDbs[] = s.split("IMDB");
                String a = imDbs[1];
                String imDbs2[] = a.split("Languages");
                if (!s.contains("Languages")){
                    return "";
                }
                return imDbs2[0];
            }
        }catch (Exception e){
            Log.e(TAG, "movieContent: exception" );
            return "";
        }




        return "";
    }
    public static String MovieRushSplit(String s){
        String replace=s.replace("moviesrush", "Buddy");

        try {
            if (replace.contains("IMDB")){
                String[] imbds=replace.split("IMBD");
                if (imbds.length == 1){
                    if (!replace.contains("Plot")){
                        String[] plots=imbds[0].split("Size");
                        return plots[0];
                    }
                    String[] plots=imbds[0].split("Plot");
                    Log.e(TAG, "MovieRushSplit: content split else part" );
                    return plots[0];
                }
                String[] plots=imbds[1].split("Plot");
                return plots[0];

            }
        }catch (Exception e){
            return "";
        }
        return "";
    }
}
//entertainment_old
//    public static String removeFirstLastChar(String s) {
//        if (s == null || s.length() == 0) {
//            return s;
//        }
//        String removingfirst = s.substring(3);
//        return removingfirst.substring(0, removingfirst.length()-6);
//    }
//    public static String makeFirstLineBold(String s){
//        String lines[] = s.split("\n");
//        String fl = String.format(Locale.ENGLISH, "<b>%s</b><br>", lines[0]);
//        Log.i(TAG, "makeFirstLineBold: "+fl);
//        StringBuilder sb = new StringBuilder();
//        sb.append(fl);
//        for(int i = 1; i < lines.length; i++){
//            if (i==lines.length-1){
//                Log.i(TAG, "makeFirstLineBold: called");
//                sb.append(lines[i]);
//            }else{
//                sb.append(lines[i] + "<br>");
//            }
//        }
//        return sb.toString().trim();
//    }
