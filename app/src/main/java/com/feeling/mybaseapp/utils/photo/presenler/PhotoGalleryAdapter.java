package com.feeling.mybaseapp.utils.photo.presenler;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.config.GlideApp;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Created by feeling on 2016/12/14.
 */

public class PhotoGalleryAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mList;
    private int picNum;

    public PhotoGalleryAdapter(Context mContext, List<String> mListonPhotoTapListener, int picNum) {
        this.mContext = mContext;
        this.mList = mList;
        this.picNum=picNum;
    }

    @Override
    public int getCount() {
        return picNum;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_gallery_viewpager_item, null);
        PhotoView view1 = (PhotoView) view.findViewById(R.id.photoview);
        if(mList!=null){
            if(!mList.get(position).trim().equals("")){
                GlideApp.with(mContext).load(mList.get(position)).centerCrop().into(view1);
            }
        }
        container.addView(view);
        return view;
    }
}
