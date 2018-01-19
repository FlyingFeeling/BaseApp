package com.feeling.mybaseapp.utils.photo.presenler;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.config.GlideApp;
import com.feeling.mybaseapp.utils.photo.myInterface.ISelectPhotoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片选择适配器
 * Created by zcs on 2016/11/25.
 */

public class PhotoSelectAdapter extends RecyclerView.Adapter<PhotoSelectAdapter.ViewHolder>{
    private List<String> photoPaths;
    private Context mContext;
    private LayoutInflater mInflater;
    private int wh;
    private ArrayList<String> checkImagePath;
    private int maxSelectPhotoCount;
    private boolean enabled = true;
    private ISelectPhotoItem mISelectPhotoItem;

    /**
     * 构造函数
     * @param context   上下文
     * @param photoPaths    照片路径集合
     * @param iSelectPhotoItem  图片选择回调接口
     * @param maxSelectPhotoCount   最大选择图片数量
     */
    public PhotoSelectAdapter(Context context, List<String> photoPaths,
                              ISelectPhotoItem iSelectPhotoItem,
                              int maxSelectPhotoCount) {
        this.photoPaths = photoPaths;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.maxSelectPhotoCount = maxSelectPhotoCount;
        this.mISelectPhotoItem = iSelectPhotoItem;
        getScreenWidth();
        checkImagePath = new ArrayList<>();
    }

    /**获取屏幕宽度*/
    private void getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        wh = metrics.widthPixels/3;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.add_up_img_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public String getItem(int position) {
        return photoPaths.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position == 0) {
            cameraView(holder);
        } else {
            photoView(holder, position);
        }
    }

    /**
     * 图片显示控件
     * @param holder
     * @param position
     */
    private void photoView(ViewHolder holder, final int position) {
        holder.mCheckBox.setVisibility(View.VISIBLE);
        holder.mImageView.setOnClickListener(null);
        GlideApp.with(mContext).load(getItem(position)).centerCrop().into(holder.mImageView);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selectChange(isChecked,position);
            }
        });
        if (checkImagePath.contains(getItem(position))) {
            holder.mCheckBox.setChecked(true);
            holder.mCheckBox.setEnabled(true);
        } else {
            if (!enabled){
                holder.mCheckBox.setEnabled(false);
            }else{
                holder.mCheckBox.setEnabled(true);
            }
            holder.mCheckBox.setChecked(false);
        }
    }

    /**拍照控件*/
    private void cameraView(ViewHolder holder) {
        holder.mCheckBox.setOnCheckedChangeListener(null);
        holder.mImageView.setScaleType(ImageView.ScaleType.CENTER);
        holder.mImageView.setImageResource(R.mipmap.ic_camera);
        holder.mCheckBox.setVisibility(View.GONE);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mISelectPhotoItem.startCamera();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoPaths.size() + 1;
    }

    /**
     * 图片选择发生改变
     * @param isChecked 控件是否选中
     * @param position  控件ID
     */
    private void selectChange(boolean isChecked,int position) {
        if (isChecked && !checkImagePath.contains(getItem(position))) {
            checkImagePath.add(getItem(position));
            mISelectPhotoItem.selectPhoto(checkImagePath);
            if (enabled&&checkImagePath.size()>=maxSelectPhotoCount){
                enabled = false;
                notifyDataSetChanged();
            }
        } else if (!isChecked && checkImagePath.contains(getItem(position))) {
            checkImagePath.remove(getItem(position));
            mISelectPhotoItem.selectPhoto(checkImagePath);
            if (!enabled&&checkImagePath.size()<maxSelectPhotoCount){
                enabled = true;
                notifyDataSetChanged();
            }
        }
    }

    /**
     * VIewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.add_up_img1);
            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
            params.width = wh;
            params.height = wh;
            mImageView.setLayoutParams(params);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.add_up_img_box1);
        }
    }
}
