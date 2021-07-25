package com.example.s_tools.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JsoupParser {

    public static final String TAG = "mtag";

    public static String parseText(String htmlcode){
        Document document =Jsoup.parse(htmlcode);
        String text=document.text();
        return text;
    }
    public static String parseMainImg(String htmlcode){
        Document document =Jsoup.parse(htmlcode);
        Elements elements = document.select("img");
        if (elements.isEmpty()){
            return "https://www.google.com";
        }
        return elements.get(0).attr("src");
    }
    public static List<String> parseAllImagesExpectMain(String htmlcode){
        Document document =Jsoup.parse(htmlcode);
        Elements elements = document.select("img");
        List<String> images= new ArrayList<>();
        for (int i = 1; i<elements.size(); i++){
            images.add(elements.get(i).attr("src"));
        }
        return images;
    }
}
