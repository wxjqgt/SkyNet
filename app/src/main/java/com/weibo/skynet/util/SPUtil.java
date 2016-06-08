package com.weibo.skynet.util;

import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/6/4.
 */
public class SPUtil {

    private static SharedPreferences getShardPreferences() {
        return App_mine.sp;
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getShardPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getShardPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        String value = getShardPreferences().getString(key, "default");
        return value;
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = getShardPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key) {
        int value = getShardPreferences().getInt(key, 0);
        return value;
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = getShardPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key) {
        long value = getShardPreferences().getLong(key, 0);
        return value;
    }

}
