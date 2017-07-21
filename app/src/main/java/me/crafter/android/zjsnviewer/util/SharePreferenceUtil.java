package me.crafter.android.zjsnviewer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

import me.crafter.android.zjsnviewer.ZjsnApplication;

/**
 * @author traburiss
 * @date 2016/6/30
 * @info ZjsnViewer
 * @desc
 */

public class SharePreferenceUtil {

    private SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    private static SharePreferenceUtil instance;

    private SharePreferenceUtil(){

        Context context = ZjsnApplication.getAppContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = mSharedPreferences.edit();
    }

    public static SharePreferenceUtil getInstance(){

        if (null == instance) instance = new SharePreferenceUtil();
        return instance;
    }

    private SharedPreferences.Editor getEditor() {

        return editor;
    }

    private SharedPreferences getmSharedPreferences() {

        return mSharedPreferences;
    }

    public String getValue(String key, String defaultValue){

        return getmSharedPreferences().getString(key,defaultValue);
    }
    public Set<String> getValue(String key, Set<String> defaultValue){

        return getmSharedPreferences().getStringSet(key, defaultValue);
    }
    public boolean getValue(String key, boolean defaultValue){

        return getmSharedPreferences().getBoolean(key,defaultValue);
    }
    public int getValue(String key, int defaultValue){

        return getmSharedPreferences().getInt(key,defaultValue);
    }
    public float getValue(String key, float defaultValue){

        return getmSharedPreferences().getFloat(key,defaultValue);
    }
    public long getValue(String key, long defaultValue){

        return getmSharedPreferences().getLong(key,defaultValue);
    }

    public boolean writeValue(String key, String value){

        return editor.putString(key, value).commit();
    }
    public boolean writeValue(String key, Set<String> value){

        return editor.putStringSet(key, value).commit();
    }
    public boolean writeValue(String key, boolean value){

        return editor.putBoolean(key, value).commit();
    }
    public boolean writeValue(String key, long value){

        return editor.putLong(key, value).commit();
    }
    public boolean writeValue(String key, float value){

        return editor.putFloat(key, value).commit();
    }
    public boolean writeValue(String key, int value){

        return editor.putInt(key, value).commit();
    }
}
