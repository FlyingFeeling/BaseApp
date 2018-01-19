package com.feeling.mybaseapp.utils;

import android.content.Context;

/**
 * Created by 123 on 2018/1/9.
 */

public class ScreenUtil {
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAVIGATION_HEIGHT_RES_NAME = "navigation_bar_height";
    /**
     * 获取屏幕宽度
     *
     * @param context the c
     * @return the screen w
     */
    public static int getScreenW(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context the c
     * @return the screen h
     */
    public static int getScreenH(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param context the c
     * @return the status bar h
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int resourceId = Integer.parseInt(clazz.getField(STATUS_BAR_HEIGHT_RES_NAME).get(object).toString());
            if (resourceId > 0)
                result = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int resourceId = Integer.parseInt(clazz.getField(NAVIGATION_HEIGHT_RES_NAME).get(object).toString());
            if (resourceId > 0)
                result = context.getResources().getDimensionPixelSize(resourceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
