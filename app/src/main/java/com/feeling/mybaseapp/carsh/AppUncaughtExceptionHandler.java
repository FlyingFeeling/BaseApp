package com.feeling.mybaseapp.carsh;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.feeling.mybaseapp.manage.ApplicationManage;
import com.feeling.mybaseapp.utils.DeviceUtil;
import com.feeling.mybaseapp.utils.SdcardUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Retrofit;

/**
 * Created by 123 on 2018/1/4.
 */

public class AppUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    //程序的Context对象
    private Context context;

    private volatile boolean crashing;

    private Retrofit retrofit;

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 单例
     */
    private static AppUncaughtExceptionHandler sAppUncaughtExceptionHandler;

    public static synchronized AppUncaughtExceptionHandler getInstance() {
        if (sAppUncaughtExceptionHandler == null) {
            synchronized (AppUncaughtExceptionHandler.class) {
                if (sAppUncaughtExceptionHandler == null) {
                    sAppUncaughtExceptionHandler = new AppUncaughtExceptionHandler();
                }
            }
        }
        return sAppUncaughtExceptionHandler;
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 打印异常信息
        e.printStackTrace();
        if (crashing) {
            return;
        }
        crashing = true;
        // 我们没有处理异常 并且默认异常处理不为空 则交给系统处理
        if (!handlelException(e) && mDefaultHandler != null) {
            // 系统处理
            mDefaultHandler.uncaughtException(t, e);
        }
        byebye();
    }

    private boolean handlelException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            String crashData = getCrashReport(ex);
            saveLog(crashData);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void byebye() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context.getApplicationContext();
        crashing = false;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        PackageInfo pinfo = ApplicationManage.getPackageInfos();
        if (pinfo != null) {
            if (ex != null) {
                //获取当前时间
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                exceptionStr.append("time：" + time+"\n");
                //app版本信息
                exceptionStr.append("versionName：" + pinfo.versionName+"\n");
                exceptionStr.append("versionCode：" + pinfo.versionCode + "\n");

                //手机系统信息
                exceptionStr.append("OS Version：" + Build.VERSION.RELEASE+"\n");

                exceptionStr.append("SDK_INT："+Build.VERSION.SDK_INT + "\n");

                //手机制造商
                exceptionStr.append("Vendor: " + Build.MANUFACTURER+ "\n");

                //手机型号
                exceptionStr.append("Model: " + Build.MODEL+ "\n");

                String errorStr = ex.getLocalizedMessage();
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.getMessage();
                }
                if (TextUtils.isEmpty(errorStr)) {
                    errorStr = ex.toString();
                }
                exceptionStr.append(ex.getClass().getName() +":\n");
                exceptionStr.append(errorStr+"\n");
                StackTraceElement[] elements = ex.getStackTrace();
                if (elements != null) {
                    for (int i = 0; i < elements.length; i++) {
                        exceptionStr.append(elements[i].toString() + "\n");
                    }
                }
            } else {
                exceptionStr.append("no exception. Throwable is null\n");
            }
            return exceptionStr.toString();
        } else {
            return "";
        }
    }

    private void saveLog(String crashData){
        if (SdcardUtil.hasSDCard()) {
            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir, "Log/"+ DeviceUtil.getDeviceIMEI(context)+"_"+time+ ".log");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(crashData.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
