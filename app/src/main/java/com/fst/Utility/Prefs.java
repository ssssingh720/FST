package com.fst.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by surabhi on 12/16/2015.
 */
public class Prefs {

    private final String MLP_DATA = "MLP_DATA";

    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MLP_DATA, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void setBooleanPreferences(Context context, String key, boolean value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MLP_DATA, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    public void setIntegerPreferences(Context context, String key, int value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MLP_DATA, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();

    }


    public String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(MLP_DATA, Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

    public boolean getBooleanPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(MLP_DATA, Context.MODE_PRIVATE);
        boolean position = prefs.getBoolean(key, false);
        return position;
    }

    public int getIntegerPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(MLP_DATA, Context.MODE_PRIVATE);
        int position = prefs.getInt(key, 0);
        return position;
    }
}
