package com.feeling.mybaseapp.utils.photo.myInterface;

import java.util.ArrayList;

/**
 * 已选图片展示接口
 * Created by zcs on 2016/11/30.
 */

public interface IHasSelectPhoto {
    /**
     * 已选择图片的数据发生改变时调用
     *
     * @param photoPaths 已选择的图片路径保存集合
     */
    void selectPhotoListsChange(ArrayList<String> photoPaths);

    /**
     * 设置选择控件是否要选中
     *
     * @param shouldCheck
     */
    void setSelectView(boolean shouldCheck);

    /**
     * 图片选择确认
     *
     * @param photoPaths 选中的图片集合
     */
    void selectResult(ArrayList<String> photoPaths);
}
