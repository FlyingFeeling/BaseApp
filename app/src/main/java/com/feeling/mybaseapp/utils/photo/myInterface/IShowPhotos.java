package com.feeling.mybaseapp.utils.photo.myInterface;


import com.feeling.mybaseapp.utils.photo.model.PhotoAlbumsInfo;

import java.util.ArrayList;


/**
 * 图片选择接口
 * Created by zcs on 2016/11/30.
 */

public interface IShowPhotos {
    /**绑定图片适配器*/
    void bindPhotoAdapter(ArrayList<String> photoPaths);
    /**绑定相册适配器*/
    void bindPhotoAlbumsAdapter(ArrayList<PhotoAlbumsInfo> photoAlbums);
}
