package com.feeling.mybaseapp.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * Created by 123 on 2017/8/14.
 */

public class DeviceUtil {

    /**
     * 获取手机IMEI
     * */
    public static String getDeviceIMEI(Context context){
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    /**
     * 获取唯一的用户ID
     *
     * @param context
     * @return
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 获取手机唯一标识序列号
     *
     * @return
     */
    public static String getUniqueSerialNumber() {
        String phoneName = Build.MODEL;// Galaxy nexus 品牌类型
        String manuFacturer = Build.MANUFACTURER;//samsung 品牌
        return manuFacturer + "-" + phoneName + "-" + getSerialNumber();
    }

    /**
     * 获取ANDROID ID
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 手机序列号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 获取手机号
     *
     * @param context
     * @return
     */
    public static String getLine1Number(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }
}
