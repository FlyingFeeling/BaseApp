package com.feeling.mybaseapp.manage;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by 123 on 2018/1/12.
 */

public class ApplicationManage {
    public static Application application = null;
    public static PackageInfo getPackageInfos(){
        PackageInfo info = null;
        try {
            info = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }
}
