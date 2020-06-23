package com.example.kueue.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferenceUtil {

    private static SharedPreferenceUtil instance;
    private SharedPreferences SP;
    private SharedPreferences.Editor editor;
    private String spotify_client_id ="SpotifyClientID";

    private SharedPreferenceUtil(Context context)
    {
        SP = PreferenceManager.getDefaultSharedPreferences(context);
        editor = SP.edit();
    }

    public static SharedPreferenceUtil getInstance(Context context)
    {
        if(instance == null) {
            synchronized (SharedPreferenceUtil.class) {
                if (instance == null)
                {
                    instance = new SharedPreferenceUtil(context);
                }
            }
        }
        return instance;
    }

    public String getClientID()
    {
        return SP.getString(spotify_client_id,"");
    }

    public void setClientID(String value)
    {
        editor.putString(spotify_client_id,value);
        editor.commit();
    }

}
