package com.feeling.mybaseapp.utils;

import android.os.Environment;

/**
 * Created by 123 on 2018/1/4.
 */

public class SdcardUtil {
    /**
     * 判断是否存在SDCard
     *
     * @return
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
}
