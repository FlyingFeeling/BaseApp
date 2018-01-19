package com.feeling.mybaseapp.utils.photo.Utils;

import android.content.Context;
import android.content.Intent;

import com.feeling.mybaseapp.utils.photo.Activity.PhotoSelectActivity;
import com.feeling.mybaseapp.utils.photo.myInterface.OnSelectPhotoResult;


/**
 * 相册工具类
 * Created by zcs on 2016/11/26.
 */

public class PhotoUtils {
    private int maxLastImgCount;//最近照片最多显示多少
    private int columnsNum;//照片按几列显示
    private Context mContext;
    private boolean needTailoring;//是否需要裁剪
    private int maxSelectPhotoCount = 9;//最多选取几张图片
    private OnSelectPhotoResult mSelectPhotoResult;
    private static PhotoUtils mPhotoUtils;
    private PhotoUtils(Builder builder) {
        mContext = builder.mContext;
        maxLastImgCount = builder.maxLastImgCount;
        needTailoring = builder.needTailoring;
        columnsNum = builder.columnsNum;
        maxSelectPhotoCount = builder.maxSelectPhotoCount;
        mSelectPhotoResult = builder.mSelectPhotoResult;
    }

    /**
     * 获取实例
     * @return
     */
    public static PhotoUtils getPhotoUtils() {
        return mPhotoUtils;
    }

    /**
     * 获取最近照片最大显示数量
     * @return  int
     */
    public int getMaxLastImgCount() {
        return maxLastImgCount;
    }

    /**
     * 获取照片按几列显示
     * @return  int
     */
    public int getColumnsNum() {
        return columnsNum;
    }

    /**
     *  获取是否需要剪裁
     * @return  boolean
     */
    public boolean isNeedTailoring() {
        return needTailoring;
    }

    /**
     * 获取最大图片选择数量
     * @return  int
     */
    public int getMaxSelectPhotoCount() {
        return maxSelectPhotoCount;
    }

    /**
     *  获取照片选择回调接口
     * @return  OnSelectPhotoResult
     */
    public OnSelectPhotoResult getSelectPhotoResult() {
        return mSelectPhotoResult;
    }

    /**
     * 跳转到照片选择页面
     */
    public void start(){
        Intent intent = new Intent(mContext,PhotoSelectActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 关闭相册选择器
     */
    public void finish(){
        mPhotoUtils = null;
    }
    public static class Builder{
        private Context mContext;
        private int maxLastImgCount;
        private int columnsNum;
        private boolean needTailoring;
        private int maxSelectPhotoCount;
        private OnSelectPhotoResult mSelectPhotoResult;
        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置照片显示的列数
         * @param columnsNum    默认为3
         * @return
         */
        public Builder setColumnsNum(int columnsNum) {
            this.columnsNum = columnsNum;
            return this;
        }

        /**
         * 设置最近照片最多显示多少
         * @param maxLastImgCount   默认为100
         * @return
         */
        public Builder setMaxLastImgCount(int maxLastImgCount) {
            this.maxLastImgCount = maxLastImgCount;
            return this;
        }

        /**
         * 设置是否需要剪切
         * @param needTailoring 默认为false
         * @return
         */
        public Builder setNeedTailoring(boolean needTailoring) {
            this.needTailoring = needTailoring;
            return this;
        }

        /**
         * 设置最大选择几张照片
         * @param maxSelectPhotoCount   默认为9
         * @return
         */
        public Builder setMaxSelectPhotoCount(int maxSelectPhotoCount) {
            this.maxSelectPhotoCount = maxSelectPhotoCount;
            return this;
        }

        /**
         * 设置返回选择结果
         * @param selectPhotoResult
         * @return
         */
        public Builder setSelectPhotoResult(OnSelectPhotoResult selectPhotoResult) {
            mSelectPhotoResult = selectPhotoResult;
            return this;
        }

        public PhotoUtils build(){
            if (mPhotoUtils==null){
                mPhotoUtils = new PhotoUtils(this);
            }
            return mPhotoUtils;
        }
    }
}
