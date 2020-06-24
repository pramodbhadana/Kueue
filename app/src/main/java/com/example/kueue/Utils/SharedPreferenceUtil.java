package com.example.kueue.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SharedPreferenceUtil {

    private static SharedPreferenceUtil instance;
    private SharedPreferences SP;
    private SharedPreferences.Editor editor;
    private String spotify_auth_token ="SpotifyAuthToken";

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

    public String getAuthToken()
    {
        return SP.getString(spotify_auth_token,"");
    }

    public void setAuthToken(String value)
    {
        editor.putString(spotify_auth_token,value);
        editor.commit();
    }

}
