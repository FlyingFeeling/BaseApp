package com.feeling.mybaseapp.utils;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by 123 on 2018/1/10.
 */

public class OSUtil {
    //MIUI标识
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    //EMUI标识
    private static final String KEY_EMUI_VERSION_CODE = "ro.build.version.emui";
    //Flyme标识
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";

    public enum ROM_TYPE {
        MIUI,
        FLYME,
        EMUI,
        OTHER
    }

    public static ROM_TYPE getRomType() {
        ROM_TYPE romType = ROM_TYPE.OTHER;
        if(isEMUI()){
            romType = ROM_TYPE.EMUI;
        }else if(isMIUI()){
            romType = ROM_TYPE.MIUI;
        }else if(isFLYME()){
            romType = ROM_TYPE.FLYME;
        }
        return romType;
    }

    /**
     * 判断是否为 EMUI
     * Is EMUI boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI() {
        String property = getSystemProperty(KEY_EMUI_VERSION_CODE, "");
        Log.e("property: ",property );
        return !TextUtils.isEmpty(property);
    }

    /**
     * 得到 EMUI 的版本
     * Gets EMUI version.
     *
     * @return the EMUI version
     */
    public static String getEMUIVersion() {
        return isEMUI() ? getSystemProperty(KEY_EMUI_VERSION_CODE, "") : "";
    }

    /**
     * 判断是否为EMUI3.1版本
     * Is EMUI 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_1() {
        String property = getEMUIVersion();
        if ("EmotionUI 3".equals(property) || property.contains("EmotionUI_3.1")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为EMUI3.0版本
     * Is EMUI 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI3_0() {
        String property = getEMUIVersion();
        if (property.contains("EmotionUI_3.0")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为EMUI3.0版本
     * Is EMUI 3 1 boolean.
     *
     * @return the boolean
     */
    public static boolean isEMUI5_0() {
        String property = getEMUIVersion();
        if (property.contains("EmotionUI_5.0")) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否为 MIUI
     * Is MIUI boolean.
     *
     * @return the boolean
     */
    public static boolean isMIUI() {
        String property = getSystemProperty(KEY_MIUI_VERSION_NAME, "");
        return !TextUtils.isEmpty(property);
    }

    /**
     * 判断 MIUI 版本是否大于等于6
     * Is MIUI 6 later boolean.
     *
     * @return the boolean
     */
    public static boolean isMIUI6Later() {
        String version = getMIUIVersion();
        int num;
        if ((!version.isEmpty())) {
            try {
                num = Integer.valueOf(version.substring(1));
                return num >= 6;
            } catch (NumberFormatException e) {
                return false;
            }
        } else
            return false;
    }

    /**
     * 获得 MIUI 的版本
     * Gets MIUI version.
     *
     * @return the MIUI version
     */
    public static String getMIUIVersion() {
        return isMIUI() ? getSystemProperty(KEY_MIUI_VERSION_NAME, "") : "";
    }

    /**
     * 判断是否为 FLYME
     * Is FLYME boolean.
     *
     * @return the boolean
     */
    public static boolean isFLYME() {
        String property = getSystemProperty(KEY_FLYME_ID_FALG_KEY, "");
        return property.toLowerCase().contains("flyme");
    }

    /**
     * 判断flymeOS的版本是否大于等于4
     * Is flyme os 4 later boolean.
     *
     * @return the boolean
     */
    public static boolean isFlymeOS4Later() {
        String version = getFlymeOSVersion();
        int num;
        if (!version.isEmpty()) {
            try {
                if (version.toLowerCase().contains("os")) {
                    num = Integer.valueOf(version.substring(9, 10));
                } else {
                    num = Integer.valueOf(version.substring(6, 7));
                }
                return num >= 4;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断flymeOS的版本是否等于5
     * Is flyme os 5 boolean.
     *
     * @return the boolean
     */
    public static boolean isFlymeOS5() {
        String version = getFlymeOSVersion();
        int num;
        if (!version.isEmpty()) {
            try {
                if (version.toLowerCase().contains("os")) {
                    num = Integer.valueOf(version.substring(9, 10));
                } else {
                    num = Integer.valueOf(version.substring(6, 7));
                }
                return num == 5;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }


    /**
     * 得到flymeOS的版本
     * Gets flyme os version.
     *
     * @return the flyme os version
     */
    public static String getFlymeOSVersion() {
        return isFLYME() ? getSystemProperty(KEY_FLYME_ID_FALG_KEY, "") : "";
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }
}
