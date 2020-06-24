package com.example.kueue.Utils;

import android.content.Context;

public class GeneralUtil {

    private static GeneralUtil instance;
    public static final int REQUEST_LOGIN = 7589;


    public static GeneralUtil getInstance(Context context)
    {
        if(instance == null) {
            synchronized (SharedPreferenceUtil.class) {
                if (instance == null)
                {
                    instance = new GeneralUtil();
                }
            }
        }
        return instance;
    }
    public static  String convertToURI(String url) {
        int index = -1;
        String URI =  null;
        if(-1 != url.indexOf("/track/")) {
            URI = "spotify"+url.substring(url.indexOf("/track/"),url.indexOf("/track/")+22+7);
        }
        else if(-1 != url.indexOf("/artist/")) {
            URI = "spotify"+url.substring(url.indexOf("/artist/"),url.indexOf("/artist/")+22+8);
        }
        else if(-1 != url.indexOf("/album/")) {
            URI = "spotify"+url.substring(url.indexOf("/album/"),url.indexOf("/album/")+22+7);
        }
        else if(-1 != url.indexOf("/playlist/")) {
            URI = "spotify"+url.substring(url.indexOf("/user/"),url.indexOf("/track/")+10+6)+
                    ":"+url.substring(url.indexOf("/playlist/"),url.indexOf("/playlist/")+22+10);
        }
        return URI.replace('/',':');
    }
}