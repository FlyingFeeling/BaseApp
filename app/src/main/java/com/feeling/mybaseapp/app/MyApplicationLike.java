package com.feeling.mybaseapp.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.manage.ApplicationManage;
import com.feeling.mybaseapp.tinker.TinkerManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by 123 on 2018/1/11.
 */

@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.feeling.mybaseapp.app.MyApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class MyApplicationLike extends DefaultApplicationLike {

    public static final String TAG= "MyApplicationLike";
    public MyApplicationLike(Application application,
                             int tinkerFlags,
                             boolean tinkerLoadVerifyFlag,
                             long applicationStartElapsedTime,
                             long applicationStartMillisTime,
                             Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        ApplicationManage.application=getApplication();
        TinkerManager.setTinkerApplicationLike(this);

//        TinkerManager.initFastCrashProtect();//****************release 打开**************
        //should set before tinker is installed
        TinkerManager.setUpgradeRetryEnable(true);
        //installTinker after load multiDex
        //or you can put com.tencent.tinker.** to main dex
        TinkerManager.installTinker(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context)
                        .setTextSizeTitle(14)
                        .setTextSizeTime(11)
                        .setSpinnerStyle(SpinnerStyle.Translate)
                        .setProgressResource(R.drawable.progress_loading);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setTextSizeTitle(14)
                        .setSpinnerStyle(SpinnerStyle.Translate)
                        .setProgressResource(R.drawable.progress_loading);
            }
        });
    }
}
