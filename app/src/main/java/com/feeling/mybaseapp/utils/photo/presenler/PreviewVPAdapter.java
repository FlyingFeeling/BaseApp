package com.feeling.mybaseapp.utils.photo.presenler;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.config.GlideApp;

import java.util.List;

/**
 * 预览图片适配器
 * Created by zcs on 2016/11/28.
 */

public class PreviewVPAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mList;

    public PreviewVPAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_viewpager_item, null);
        ImageView view1 = (ImageView) view.findViewById(R.id.imageView);
        GlideApp.with(mContext).load(mList.get(position)).centerCrop().into(view1);
        container.addView(view);
        return view;
    }

}
