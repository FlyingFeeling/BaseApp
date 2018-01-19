package com.feeling.mybaseapp.manage;

/**
 * Created by 李松斌 on 2017/3/31.
 * Function:
 * Desc:
 */

public class AppStatusManager {
    private static int appStatus = AppStatusConstant.STATUS_FORCE_KILLED; //APP状态 初始值为没启动 不在前台状态
    public static AppStatusManager instance;

    private AppStatusManager() {
    }

    public static AppStatusManager getInstance() {
        if (instance == null) {
            synchronized (AppStatusManager.class) {
                if (instance == null)
                    instance = new AppStatusManager();
            }
        }
        return instance;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}
