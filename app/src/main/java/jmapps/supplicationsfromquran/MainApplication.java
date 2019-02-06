package jmapps.supplicationsfromquran;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

public class MainApplication extends Application {

    private SharedPreferences.Editor mEditor;
    public final static String keyNightMode = "key_night_mode";
    private boolean isNightModeEnabled = false;
    private static MainApplication singleton = null;

    public static MainApplication getInstance() {
        if (singleton == null) {
            singleton = new MainApplication();
        }
        return singleton;
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        this.isNightModeEnabled = mPreferences.getBoolean(keyNightMode, false);

        if (isNightModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;
        mEditor.putBoolean(keyNightMode, isNightModeEnabled).apply();

        if (isNightModeEnabled) {

            mEditor.putInt("key_r_arabic", 255);
            mEditor.putInt("key_g_arabic", 233);
            mEditor.putInt("key_b_arabic", 33);

            mEditor.putInt("key_r_translation", 200);
            mEditor.putInt("key_g_translation", 200);
            mEditor.putInt("key_b_translation", 200);

            mEditor.putInt("key_progress_arabic", 1513).apply();
            mEditor.putInt("key_progress_translation", 1791).apply();
            mEditor.apply();

        } else {

            mEditor.putInt("key_r_arabic", 188);
            mEditor.putInt("key_g_arabic", 68);
            mEditor.putInt("key_b_arabic", 68);

            mEditor.putInt("key_r_translation", 0);
            mEditor.putInt("key_g_translation", 0);
            mEditor.putInt("key_b_translation", 0);

            mEditor.putInt("key_progress_arabic", 956);
            mEditor.putInt("key_progress_translation", 0);
            mEditor.apply();
        }
    }
}