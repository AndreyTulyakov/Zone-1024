package com.mhyhre.zone_1024;

import android.app.Activity;
import android.content.SharedPreferences;

public class PreferenceManager {
    
    private static PreferenceManager instance = null;
    
    private final String preferenceId;
    private final Activity activity;
    
    private static boolean vibroEnabled = true;
    private static boolean soundEnabled = true;
    private static boolean gameWasNotEnded = false;

    
    public static PreferenceManager getInstance(Activity activity, String preferenceId) {
        if(instance == null) {
            instance = new PreferenceManager(activity, preferenceId);
        }
        return instance;
    }
    
    private PreferenceManager(Activity activity, String preferenceId) {
        if(activity == null || preferenceId == null) {
            throw new IllegalArgumentException();
        }
        
        this.activity = activity;
        this.preferenceId = preferenceId;
        
        loadPreferences();
    }
    
    public void loadPreferences() {

        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(preferenceId, mode);

        setVibroEnabled(mySharedPreferences.getBoolean("isVibroEnabled", true));
        setSoundEnabled(mySharedPreferences.getBoolean("isSoundEnabled", true));
        
        setGameWasNotEnded(mySharedPreferences.getBoolean("gameWasNotEnded", false));
    }
    
    protected void savePreferences() {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences mySharedPreferences = activity.getSharedPreferences(preferenceId, mode);

        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt("appVersion", 2);
        editor.putBoolean("isVibroEnabled", isVibroEnabled());
        editor.putBoolean("isSoundEnabled", isSoundEnabled());
        
        editor.putBoolean("gameWasNotEnded", isGameWasNotEnded());
        editor.commit();
    }

    
    public static boolean isVibroEnabled() {
        return vibroEnabled;
    }

    public static void setVibroEnabled(boolean enabled) {
        vibroEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isGameWasNotEnded() {
        return gameWasNotEnded;
    }

    public static void setGameWasNotEnded(boolean gameWasNotEnded) {
        PreferenceManager.gameWasNotEnded = gameWasNotEnded;
    }
}
