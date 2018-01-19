package com.feeling.mybaseapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * package: com.easytools.tools.ActivityUtil
 * author: gyc
 * description: Activity的出/入栈管理
 * time: create at 2016/10/14 13:25
 */

public class ActivityUtil {

    private static Stack<Activity> activityStack;
    private static ActivityUtil instance;

    private ActivityUtil() {
    }

    /**
     * 单例模式获取实例
     *
     * @return ActivityUtil实例
     */
    public static ActivityUtil getInstance() {
        if (instance == null) {
            instance = new ActivityUtil();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity 活动
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前的Activity（栈中最后一个压入的）
     *
     * @return 活动
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void removeActivty(Activity activity){
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 活动
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls 类名
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出APP
     * @param context 上下文
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
