package com.example.uhf_bluetoothclient.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.uhf_bluetoothclient.constants.Constants;

public class SharedPreferencesUtils {
    private static volatile SharedPreferencesUtils INSTANCE;
    private static final String TAG = SharedPreferencesUtils.class.getSimpleName();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SharedPreferencesUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.SP_TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesUtils getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SharedPreferencesUtils(context);
                }
            }
        }
        return INSTANCE;
    }

    public int getFrequencyBandSelection() {
        return sharedPreferences.getInt(Constants.SP_FREQUENCY_BAND, 0);
    }

    public int getFrequencyMinSelection() {
        return sharedPreferences.getInt(Constants.SP_FREQUENCY_MIN, 0);
    }

    public int getFrequencyMaxSelection() {
        return sharedPreferences.getInt(Constants.SP_FREQUENCY_MAX, 0);
    }

    public int getPowerSelection() {
        return sharedPreferences.getInt(Constants.SP_POWER, 33);
    }

    public int getSessionSelection() {
        return sharedPreferences.getInt(Constants.SP_SESSION, 0);
    }

    public boolean getAntenna1IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA1, true);
    }

    public boolean getAntenna2IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA2, true);
    }

    public boolean getAntenna3IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA3, true);
    }

    public boolean getAntenna4IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA4, true);
    }

    public boolean getAntenna5IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA5, false);
    }

    public boolean getAntenna6IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA6, false);
    }

    public boolean getAntenna7IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA7, false);
    }

    public boolean getAntenna8IsChecked() {
        return sharedPreferences.getBoolean(Constants.SP_ANTENNA8, false);
    }

    public void setFrequencyBandSelection(int selection) {
        editor.putInt(Constants.SP_FREQUENCY_BAND, selection).commit();
    }

    public void setFrequencyMinSelection(int selection) {
        editor.putInt(Constants.SP_FREQUENCY_MIN, selection).commit();
    }

    public void setFrequencyMaxSelection(int selection) {
        editor.putInt(Constants.SP_FREQUENCY_MAX, selection).commit();
    }

    public void setPowerSelection(int selection) {
        editor.putInt(Constants.SP_POWER, selection).commit();
    }

    public void setSessionSelection(int selection) {
        editor.putInt(Constants.SP_SESSION, selection).commit();
    }

    public void setAntenna1IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA1, isChecked);
    }

    public void setAntenna2IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA2, isChecked);
    }

    public void setAntenna3IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA3, isChecked);
    }

    public void setAntenna4IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA4, isChecked);
    }

    public void setAntenna5IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA5, isChecked);
    }

    public void setAntenna6IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA6, isChecked);
    }

    public void setAntenna7IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA7, isChecked);
    }

    public void setAntenna8IsChecked(boolean isChecked) {
        editor.putBoolean(Constants.SP_ANTENNA8, isChecked);
    }
}
