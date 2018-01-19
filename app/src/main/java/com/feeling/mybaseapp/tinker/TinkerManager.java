/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feeling.mybaseapp.tinker;

import com.feeling.mybaseapp.carsh.AppUncaughtExceptionHandler;
import com.feeling.mybaseapp.tinker.reporter.LoadReporter;
import com.feeling.mybaseapp.tinker.reporter.PatchListener;
import com.feeling.mybaseapp.tinker.reporter.PatchReporter;
import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.app.ApplicationLike;



/**
 * Created by zhangshaowen on 16/7/3.
 */
public class TinkerManager {
    private static final String TAG = "Tinker.TinkerManager";

    private static ApplicationLike                applicationLike;
    private static boolean isInstalled = false;

    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    public static void initFastCrashProtect() {
        AppUncaughtExceptionHandler crashHandler = AppUncaughtExceptionHandler.getInstance();
        crashHandler.init(applicationLike.getApplication());
    }

    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }


    /**
     * all use default class, simply Tinker install method
     */
    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;

    }

    /**
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     *
     * @param appLike
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        //or you can just use DefaultLoadReporter
        com.tencent.tinker.lib.reporter.LoadReporter loadReporter = new LoadReporter(appLike.getApplication());
        //or you can just use DefaultPatchReporter
        com.tencent.tinker.lib.reporter.PatchReporter patchReporter = new PatchReporter(appLike.getApplication());
        //or you can just use DefaultPatchListener
        com.tencent.tinker.lib.listener.PatchListener patchListener = new PatchListener(appLike.getApplication());
        //you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(appLike,
            loadReporter, patchReporter, patchListener,
            ResultService.class, upgradePatchProcessor);

        isInstalled = true;
    }
}
