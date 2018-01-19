package com.feeling.mybaseapp.utils.photo.myInterface;

import android.app.Dialog;

/**
 * 权限确认和请求接口
 * Created by zcs on 2016/12/1.
 */

public interface IAboutPermission {
    /**已有权限，或者请求权限成功*/
    void hasPermission();
    /**系统拒绝权限请求*/
    void errorHasPermission();
    /**请求失败再次请求*/
    void showDiaLog(Dialog dialog);
}
