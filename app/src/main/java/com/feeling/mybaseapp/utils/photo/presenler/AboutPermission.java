package com.feeling.mybaseapp.utils.photo.presenler;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.feeling.mybaseapp.utils.photo.myInterface.IAboutPermission;


/**
 * 权限确认和请求
 * Created by zcs on 2016/12/1.
 */

public class AboutPermission {
    private IAboutPermission mIAboutPermission;
    private Context mContext;
    private static final int CAMERAANDWRITE_EXTERNAL_STORAGE = 101;//照相机和文件权限

    /**
     * 构造函数
     *
     * @param context          上下文
     * @param iAboutPermission 权限请求接口
     */
    public AboutPermission(Context context, IAboutPermission iAboutPermission) {
        mContext = context;
        mIAboutPermission = iAboutPermission;
        requestPermission();
    }

    /**
     * 确认是否已有权限
     */
    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission
                (mContext, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向系统请求权限，
     * 请求前需先确认是否已有权限
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasPermission()) {
                mIAboutPermission.hasPermission();
            } else {
                ((Activity) mContext).requestPermissions(
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERAANDWRITE_EXTERNAL_STORAGE);
            }
        } else {
            mIAboutPermission.hasPermission();
        }
    }

    /**
     * 请求权限结果处理
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERAANDWRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mIAboutPermission.hasPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA) &&
                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    mIAboutPermission.showDiaLog(requestPermissionAgain());
                } else {
                    mIAboutPermission.errorHasPermission();
                }
            }
        }
    }

    /**
     * 再次让用户选择是否允许权限
     *
     * @return 弹窗选择
     */
    private Dialog requestPermissionAgain() {
        return new AlertDialog.Builder(mContext)
                .setTitle("权限申请")
                .setMessage("我们需要您同意此申请，否则将无法进行")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIAboutPermission.errorHasPermission();
                    }
                })
                .create();
    }
}
