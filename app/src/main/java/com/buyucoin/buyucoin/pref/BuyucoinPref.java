package com.buyucoin.buyucoin.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.buyucoin.buyucoin.cipher.CipherAES;

public class BuyucoinPref {
    public static String ACCESS_TOKEN = "access_token";
    public static String REFRESH_TOKEN = "refresh_token";
    private String PREF_NAME = "BUYUCOIN_USER_PREFS";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String KEY = "qwaserdftyghuijkoplzxcvbnmlkjhgf";
    public BuyucoinPref(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        this.editor = preferences.edit();

    }

    public void removeAllPref(){
        this.preferences.edit().clear().apply();
    }

    public String getPrefString(String key){
        String encrypted_data = "";
        if(key.equals("inr_amount")){ encrypted_data  = preferences.getString(key,"0"); }
        else{ encrypted_data  = preferences.getString(key,null);
        }
        try {
            return new CipherAES(KEY).decrypt(encrypted_data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        try {
            editor.putString(key,new CipherAES(KEY).encrypt(value)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
