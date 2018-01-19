package com.feeling.mybaseapp.utils.photo.presenler;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件写入工具类
 * Created by zcs on 2016/12/1.
 */

public class FileUtils {
    /**
     * 开启照相机
     *
     * @return 如果SD卡可用，则返回图片保存路径Uri，不可用则返回null
     */
    public File getNewFileUri(Context context) throws IOException {
       String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //Create an image file name
            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss", Locale.CHINA).format(new
                    Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            //.getExternalFileDir()方法可以获取到 SDCard/Android/data/应用包名/files/ 目录，
            //一般放一些长时间保存的数据
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            //创建临时文件，文件前缀不能少于三个字符，后缀如果为空默认为".tmp"
            File image = File.createTempFile(
                    imageFileName,/*前缀*/
                    ".jpg",       /*后缀*/
                    storageDir   /*文件夹*/
            );
            String cameraPath = "file:" + image.getAbsolutePath();
            return image;
        } else {
            return null;
        }
    }

    public String getFilePath() {
        String path = Environment.getExternalStorageDirectory() + "/ChinaYTO/";
        String fileName = String.valueOf(System.currentTimeMillis() + ".png");
        String cameraPath = path + fileName;
        return cameraPath;
    }
}
