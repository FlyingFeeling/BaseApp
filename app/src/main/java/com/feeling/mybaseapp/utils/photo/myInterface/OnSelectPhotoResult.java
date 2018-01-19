package com.feeling.mybaseapp.utils.photo.myInterface;

import java.util.ArrayList;

/**
 * 照片选择结果回调
 * Created by zcs on 2016/12/2.
 */

public interface OnSelectPhotoResult {
    /**
     * 获取照片选择结果
     * @param photoPaths    所选择照片路径集合
     */
    void selectPhotoResult(ArrayList<String> photoPaths);
}
