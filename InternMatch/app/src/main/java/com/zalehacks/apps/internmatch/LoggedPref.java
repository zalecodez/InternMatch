package com.zalehacks.apps.internmatch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by zale on 4/03/17.
 */

public class LoggedPref {

    private static final String PREF_LOGGED = "IsLoggedIn";
    private static final String PREF_LOGGED_USER_ID = "LoggedInUserId";

    public static int getLoggedId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isLogged = prefs.getBoolean(PREF_LOGGED, false);

        if(isLogged){
            int userId = prefs.getInt(PREF_LOGGED_USER_ID, -1);
            return userId;
        }

        return -1;
    }

    public static boolean isLoggedIn(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isLogged = prefs.getBoolean(PREF_LOGGED, false);

        return isLogged;
    }



    public static void setLogged(Context context, boolean logged, int id){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit()
                .putBoolean(PREF_LOGGED, logged)
                .apply();

        if(logged)
            prefs.edit()
            .putInt(PREF_LOGGED_USER_ID, id)
            .apply();
    }

}
