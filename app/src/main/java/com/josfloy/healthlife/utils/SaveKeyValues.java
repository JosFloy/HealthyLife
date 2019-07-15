package com.josfloy.healthlife.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Jos on 2019/7/14 0014.
 */
public class SaveKeyValues {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    /**
     * 初始化SP
     *
     * @param context
     */
    public static void createSharedPreferences(Context context) {
        String appName = context.getPackageName();
        Log.e("存储的文件名---->", appName);
        sharedPreferences = context.getSharedPreferences(appName, Context.BIND_ABOVE_CLIENT);
        editor = sharedPreferences.edit();
    }

    /**
     * 判断SharedPreferences是否被创建
     *
     * @return
     */
    public static boolean isUnCreate() {
        boolean result = sharedPreferences == null;
        if (result) {
            Log.e("提醒", "sharedPreferences未被创建！");
        }
        return result;
    }

    /**
     * 存入String类型的值
     *
     * @param key
     * @param values
     * @return
     */
    public static boolean putStringValues(String key, String values) {
        if (isUnCreate()) {
            return false;
        }
        editor.putString(key, values);
        return editor.commit();
    }

    /**
     * 取出String类型的值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringValues(String key, String defValue) {
        if (isUnCreate()) {
            return null;
        }
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 存入int类型的值
     *
     * @param key
     * @param values
     * @return
     */
    public static boolean putIntValues(String key, int values) {
        if (isUnCreate()) {
            return false;
        }
        editor.putInt(key, values);
        return editor.commit();
    }

    /**
     * 取出int类型的值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static int getIntValues(String key, int defValue) {
        if (isUnCreate()) {
            return 0;
        }
        int int_value = sharedPreferences.getInt(key, defValue);
        return int_value;
    }

    /**
     * 存入long类型的值
     *
     * @param key
     * @param values
     * @return
     */
    public static boolean putLongValues(String key, long values) {
        if (isUnCreate()) {
            return false;
        }
        editor.putLong(key, values);
        return editor.commit();
    }

    /**
     * 取出long类型的值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static long getLongValues(String key, long defValue) {
        if (isUnCreate()) {
            return 0;
        }
        long long_value = sharedPreferences.getLong(key, defValue);
        return long_value;
    }

    /**
     * 存入float类型的值
     *
     * @param key
     * @param values
     * @return
     */
    public static boolean putFloatValues(String key, float values) {
        if (isUnCreate()) {
            return false;
        }
        editor.putFloat(key, values);
        return editor.commit();
    }

    /**
     * 取出float类型的值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloatValues(String key, float defValue) {
        if (isUnCreate()) {
            return 0;
        }
        float float_value = sharedPreferences.getFloat(key, defValue);
        return float_value;
    }

    /**
     * 存入boolean类型的值
     *
     * @param key
     * @param values
     * @return
     */
    public static boolean putBooleanValues(String key, boolean values) {
        if (isUnCreate()) {
            return false;
        }
        editor.putBoolean(key, values);
        return editor.commit();
    }

    /**
     * 取出boolean类型的值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getFloatValues(String key, boolean defValue) {
        if (isUnCreate()) {
            return false;
        }
        boolean boolean_value = sharedPreferences.getBoolean(key, defValue);
        return boolean_value;
    }

    /**
     * 清空数据
     */
    public static boolean deleteAllValues() {
        if (isUnCreate()) {
            return false;
        }
        editor.clear();
        return editor.commit();
    }

    /**
     * 删除指定数据
     *
     * @param key
     * @return
     */
    public static boolean removeKeyForValues(String key) {
        if (isUnCreate()) {
            return false;
        }
        editor.remove(key);
        return editor.commit();
    }
}
