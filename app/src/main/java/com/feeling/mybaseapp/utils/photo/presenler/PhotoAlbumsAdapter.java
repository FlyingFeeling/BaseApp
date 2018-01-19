package com.feeling.mybaseapp.utils.photo.presenler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.config.GlideApp;
import com.feeling.mybaseapp.utils.photo.model.PhotoAlbumsInfo;

import java.util.List;

/**
 * 相册适配器
 * Created by zcs on 2016/11/26.
 */

public class PhotoAlbumsAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhotoAlbumsInfo> mList;
    public PhotoAlbumsAdapter(Context context, List<PhotoAlbumsInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PhotoAlbumsInfo getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_photo_albums,null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(PhotoAlbumsInfo info, ViewHolder holder) {
        holder.tv_albums_name.setText(info.getPhotoAlbumsName());
        holder.tv_photo_num.setText(info.getPhotoCount()+"张");
        GlideApp.with(mContext).load(info.getFirstImagePath()).centerCrop().into(holder.iv_photo_albums);
    }
    class ViewHolder {
        ImageView iv_photo_albums;
        TextView tv_albums_name,tv_photo_num;
        CheckBox cb_albums_isCheck;
        public ViewHolder(View itemView) {
            iv_photo_albums = (ImageView) itemView.findViewById(R.id.iv_photo_albums);
            tv_albums_name = (TextView) itemView.findViewById(R.id.tv_albums_name);
            tv_photo_num = (TextView) itemView.findViewById(R.id.tv_photo_num);
            cb_albums_isCheck = (CheckBox) itemView.findViewById(R.id.cb_albums_isCheck);
        }
    }
}
