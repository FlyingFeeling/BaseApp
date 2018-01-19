package com.feeling.mybaseapp.utils.photo.presenler;

import android.content.Context;

import com.feeling.mybaseapp.utils.photo.Utils.ReadPhotoUtils;
import com.feeling.mybaseapp.utils.photo.myInterface.IShowPhotos;

import java.io.File;
import java.io.IOException;

/**
 * 图片展示控制器
 * Created by zcs on 2016/12/1.
 */

public class ShowPhotos {
    private IShowPhotos mIShowPhotos;
    private Context mContext;
    private ReadPhotoUtils mReadPhotoUtils;

    /**
     * 构造函数
     *
     * @param context     上下文
     * @param iShowPhotos 图片展示接口
     */
    public ShowPhotos(Context context, IShowPhotos iShowPhotos) {
        mContext = context;
        mIShowPhotos = iShowPhotos;
        mReadPhotoUtils = new ReadPhotoUtils(mContext);
    }

    /**
     * 读取最近照片
     *
     * @param maxLastPhotoCount 读取的最大值
     */
    public void readLastPhoto(int maxLastPhotoCount) {
        mIShowPhotos.bindPhotoAdapter(mReadPhotoUtils.getLastImagePaths(maxLastPhotoCount));
    }

    /**
     * 读取所有照片
     *  按最新修改读取
     */
    public void readAllPhoto(){
        mIShowPhotos.bindPhotoAdapter(mReadPhotoUtils.getAllImagePaths());
    }

    /**
     * 读取指定相册下的所以图片
     * @param albumPath 相册路径
     */
    public void readPhotoByAlbum(String albumPath){
        mIShowPhotos.bindPhotoAdapter(mReadPhotoUtils.getAllImagePathsByFolder(albumPath));
    }
    /**
     * 读取相册
     */
    public void readPhotoAlbums(){
        mIShowPhotos.bindPhotoAlbumsAdapter(mReadPhotoUtils.getPhotoAlbums());
    }

    /**
     * 开启照相机
     * @return  如果SD卡可用，则返回图片保存路径Uri，不可用则返回null
     */
    public File startCamera(Context context) throws IOException {
//        FileUtils utils = new FileUtils();
//        Uri uri = utils.getNewFileUri();
//        return uri;
        return new FileUtils().getNewFileUri(context);
    }
}
