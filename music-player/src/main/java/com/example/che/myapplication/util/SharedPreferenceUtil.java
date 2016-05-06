package com.example.che.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    // SharedPreference文件的文件名
    public final static String XML_Settings = "settings";
    private static SharedPreferences settings;

    /**
     * @param context 上下文
     * @param key     键
     * @param value   值
     * @category 保存String键值对
     */
    public static void saveSharedPreString(Context context, String key, String value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        settings.edit().putString(key, value).commit();
    }

    /**
     * @param context 上下文
     * @param key     键
     * @category 获取String键值对
     */
    public static String getSharedPreString(Context context, String key) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }

    /**
     * @param context 上下文
     * @param key     键
     * @return
     * @category 获取boolean键值对
     */
    public static boolean getSharedPreBoolean(Context context, String key) {
        settings = context.getSharedPreferences(XML_Settings, 0);
        return settings.getBoolean(key, false);
    }

    /**
     * @param context 上下文
     * @param key     键
     * @param value   值
     * @category 保存boolean键值对
     */
    public static void saveSharedPreBoolean(Context context, String key, boolean value) {
        settings = context.getSharedPreferences(XML_Settings, 0);
        settings.edit().putBoolean(key, value).commit();
    }

    /**
     * @param context 上下文
     * @param key     键
     * @param value   值
     * @category 保存int类型的数据到SharedPreference配置文件
     */
    public static void saveSharedPreInteger(Context context, String key, int value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        settings.edit().putInt(key, value).commit();
    }

    /**
     * @param context 上下文
     * @param key     键
     * @return 返回int类型的value值
     * @category 从SharedPreference配置文件中获取int类型的值
     */
    public static int getSharedPreInteger(Context context, String key, int defaultValue) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        return settings.getInt(key, 0);
    }

    /**
     * 保存Long类型的数据到SharedPreference配置文件
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveSharedPreLong(Context context, String key, Long value) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        settings.edit().putLong(key, value).commit();
    }

    /**
     * @param context 上下文
     * @param key     键
     * @return 返回int类型的value值
     * @category 从SharedPreference配置文件中获取int类型的值
     */
    public static long getSharedPreLong(Context context, String key, long defaultValue) {
        settings = context.getSharedPreferences(XML_Settings, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);

    }

    /**
     * @param context 上下文
     * @category 清空SharedPreference中的所有String类型的数值
     */
    public static void clearSave(Context context) {
        settings = context.getSharedPreferences(XML_Settings, 0);
        for (String name : settings.getAll().keySet()) {
            saveSharedPreString(context, name, "");
        }
    }


}
