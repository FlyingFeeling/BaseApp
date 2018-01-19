package com.feeling.mybaseapp.utils.photo.presenler;

import android.content.Context;
import android.net.Uri;

import com.feeling.mybaseapp.utils.photo.myInterface.IHasSelectPhoto;

import java.io.IOException;
import java.util.ArrayList;


/**
 * 已有图片控制器
 * Created by zcs on 2016/12/1.
 */

public class ShowHasPhotos {
    private IHasSelectPhoto mIHasSelectPhoto;
    private ArrayList<String> selectPhotoPaths;
    private ArrayList<String> cancelIndexs;

    public ShowHasPhotos(ArrayList<String> selectPhotoPaths, IHasSelectPhoto iHasSelectPhoto) {
        this.selectPhotoPaths = selectPhotoPaths;
        this.mIHasSelectPhoto = iHasSelectPhoto;
        cancelIndexs = new ArrayList<>();
    }

    /**
     * 修改图片路径保存集合数据
     *
     * @param position  要修改的集合数据的下标
     * @param photoPath 要修改为的图片路径
     */
    public void selectPhotoListChange(int position, String photoPath) {
        selectPhotoPaths.remove(position);
        selectPhotoPaths.add(position, photoPath);
        mIHasSelectPhoto.selectPhotoListsChange(selectPhotoPaths);
    }

    /**
     * 相片选中状态发生改变
     *
     * @param isChecked  是否选中
     * @param canclePath 当前图片路径
     */
    public void selectChange(boolean isChecked, String canclePath) {
        if (!isChecked && !cancelIndexs.contains(canclePath)) {
            cancelIndexs.add(canclePath);
        } else if (isChecked && cancelIndexs.contains(canclePath)) {
            String str = canclePath;
            cancelIndexs.remove(str);
        }
    }

    /**
     * 设置选择控件
     *
     * @param position 当前显示图片在集合中的下标
     */
    public void setSelectView(int position) {
        if (cancelIndexs.contains(position)) {
            mIHasSelectPhoto.setSelectView(false);
        } else {
            mIHasSelectPhoto.setSelectView(true);
        }
    }

    /**
     * 确认选择
     */
    public void confirmChoose() {
        selectPhotoPaths.removeAll(cancelIndexs);
        mIHasSelectPhoto.selectResult(selectPhotoPaths);
    }

    /**
     * 得到剪切图片保存Uri
     *
     * @return
     */
    public Uri getNewFileUri(Context context) throws IOException {
        return Uri.parse(new FileUtils().getNewFileUri(context).getAbsolutePath());
    }
}
