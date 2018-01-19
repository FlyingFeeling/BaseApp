package com.feeling.mybaseapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by 123 on 2018/1/9.
 */

public class SystemUtil {

    public static void startSetting(Context context){
        Uri packageURI = Uri.fromParts("package", context.getPackageName(), null);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 安装apk
     *
     * @param context the context
     * @param apkfile the apkfile
     */
    public static void installApk(Context context, File apkfile) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.package-archive");
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName()+"installApk", apkfile);
            intent.setData(apkUri);
        }else {
            intent.setData(Uri.fromFile(apkfile));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
