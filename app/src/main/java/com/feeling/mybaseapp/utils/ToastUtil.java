package com.feeling.mybaseapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 123 on 2018/1/9.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showToast(Context mContext, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static void showToast(Context mContext, int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, mContext.getResources().getString(resId), Toast
                    .LENGTH_LONG);
        } else {
            mToast.setText(mContext.getResources().getString(resId));
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

}
