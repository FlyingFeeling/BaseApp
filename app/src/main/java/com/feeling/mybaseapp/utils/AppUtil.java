package com.feeling.mybaseapp.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.security.auth.x500.X500Principal;

/**
 * Created by 123 on 2018/1/9.
 */

public class AppUtil {
    /**
     * 获取应用版本名称
     *
     * @param context     the context
     * @return the app version name
     */
    public static String getVersionName(Context context) {
        String appVersion = null;
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * 获取应用版本号
     *
     * @param context     the context
     * @return the app version code
     */
    public static int getVersionCode(Context context) {
        int appVersionCode = 0;
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            appVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionCode;
    }

    /**
     * 判断当前App处于前台还是后台
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
     * <p>并且必须是系统应用该方法才有效</p>
     *
     * @param context 上下文
     * @return {@code true}: 后台<br>{@code false}: 前台
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测当前应用是否是Debug版本
     *
     * @param context 上下文
     * @return 是否是Debug版本
     */
    private final static X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");
    public static boolean isDebug(Context context) {
        boolean debuggable = false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context
                    .getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = packageInfo.signatures;
            for (int i = 0; i < signatures.length; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream bis = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate certificate = (X509Certificate) cf.generateCertificate(bis);
                debuggable = certificate.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) break;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return debuggable;
    }

    /**
     * 清除应用内部缓存
     *
     * @param context the context
     */
    public static void cleanCache(Context context) {
        FileUtil.deleteAllChild(context.getCacheDir());
    }

    public static void killProcess(Context context){
        ShareTinkerInternals.killAllOtherProcess(context.getApplicationContext());
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
