package com.buyucoin.buyucoin.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class BuyucoinPref {
    public static String ACCESS_TOKEN = "access_token";
    private String PREF_NAME = "BUYUCOIN_USER_PREFS";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public BuyucoinPref(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public String getPrefString(String key){
        return preferences.getString(key,null);
    }
    public int getPrefInt(String key){
        return preferences.getInt(key,0);
    }
    public float getPrefFloat(String key){
        return preferences.getFloat(key,0);
    }
    public boolean getPrefBoolean(String key){
        return preferences.getBoolean(key,false);
    }


    public void setEditpref(String key, String value){
        editor.putString(key,value).apply();

    }
    public void setEditpref(String key, int value){
        editor.putInt(key,value).apply();

    }
    public void setEditpref(String key, float value){
        editor.putFloat(key,value).apply();

    }
    public void setEditpref(String key, boolean value){
        editor.putBoolean(key,value).apply();
    }

    public SharedPreferences.Editor removePref(String key){
        return editor.remove(key);
    }

}
