package com.feeling.mybaseapp.utils;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

import java.io.File;

/**
 * Created by 123 on 2018/1/8.
 */

public class UriUtil {

    /**
     * 获取真实的路径
     * @param context   上下文
     * @param uri       uri
     * @return          文件路径
     */
    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String realPath = cursor.getString(index);
            cursor.close();
            return realPath;
        }
    }

    /**
     * 获取文件名称
     * @param context   上下文
     * @param uri       uri
     * @return          文件名称
     */
    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /**
     * 更新相册图片
     * @param imgFile 图片文件
     * */
    public static void updataImages(Application application, File imgFile){
        ContentValues values = new ContentValues();
        ContentResolver resolver = application.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, imgFile.getAbsolutePath());
        values.put(MediaStore.Images.ImageColumns.TITLE, imgFile.getName());
        Cursor cursor=resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns._ID},
                MediaStore.Images.ImageColumns.DATA+"=? and "+MediaStore.Images.ImageColumns.TITLE+"=?",
                new String[]{imgFile.getAbsolutePath(),imgFile.getName()},null);
        Uri uri=null;
        long time=System.currentTimeMillis();
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                String id=cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED,time/1000);
                values.put(MediaStore.Images.ImageColumns.SIZE, imgFile.length());
                resolver.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values, MediaStore.Images.ImageColumns._ID+"=?", new String[]{id});
                // 最后通知图库更新
                application.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file:/"+imgFile.getAbsolutePath())));
            }
        }else {
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN,time);
            values.put(MediaStore.Images.ImageColumns.DATE_ADDED,time/1000);
            values.put(MediaStore.Images.ImageColumns.SIZE, imgFile.length());
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        cursor.close();
    }
}
