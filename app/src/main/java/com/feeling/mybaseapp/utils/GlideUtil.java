package com.feeling.mybaseapp.utils;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.feeling.mybaseapp.config.GlideApp;

import java.io.File;

import static com.feeling.mybaseapp.utils.FileUtil.copyFile;
import static com.feeling.mybaseapp.utils.UriUtil.updataImages;

/**
 * Created by 123 on 2018/1/18.
 */

public class GlideUtil {
    /**
     * @param imageUrl 图片网络地址
     * @param fileName 保存的文件名
     * 文件默认保存在 storage/sdcard/Android/data/包名/file/pictures
     * */
    public static File save(Application application, String imageUrl, String fileName){
        if(TextUtils.isEmpty(imageUrl))return null;
        // 首先保存图片
        File appDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File file = new File(appDir, fileName);
        save(application,imageUrl,file);
        return file;
    }

    /**
     * @param imageUrl 图片网络地址
     * @param saveFile 保存的文件
     * */
    public static void save(final Application application, String imageUrl, final File saveFile){
        if(TextUtils.isEmpty(imageUrl))return;
        GlideApp.with(application).downloadOnly().load(imageUrl).into(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                copyFile(resource,saveFile);
                updataImages(application,saveFile);
                if(resource.exists()&&resource.isFile()){
                    resource.delete();
                }
            }
        });
    }
}
