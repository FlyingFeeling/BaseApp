package com.feeling.mybaseapp.utils.photo.myInterface;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 选中图片后的回调接口，
 * 包括选择拍照
 * Created by zcs on 2016/12/1.
 */

public interface ISelectPhotoItem {
    /**拍照*/
    void startCamera() throws IOException;

    /**
     * 选中图片回调
     * @param photoPaths 选中的图片集合
     */
    void selectPhoto(ArrayList<String> photoPaths);
}
