package com.feeling.mybaseapp.utils.photo.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.ui.view.PhotoViewPager;
import com.feeling.mybaseapp.utils.photo.presenler.PhotoGalleryAdapter;

import java.util.List;


public class PhotoGalleryActivity extends AppCompatActivity {

    private PhotoViewPager viewPager;
    private PhotoGalleryAdapter adapter;
    private List<String> photo_urls;
    private int picNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 21) {
            //在android5.0之后才会执行
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//设置全屏但不隐藏状态栏
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//设置状态栏透明
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        if(getIntent()!=null){
            picNum=getIntent().getIntExtra("picNum",1);
            if(getIntent().getStringArrayListExtra("urls")!=null){
                photo_urls=getIntent().getStringArrayListExtra("urls");
                if(photo_urls.size()!=picNum){
                    picNum=photo_urls.size();
                }
            }
        }
        viewPager= (PhotoViewPager) findViewById(R.id.photo_viewpager);
        adapter=new PhotoGalleryAdapter(this, photo_urls,picNum);
        viewPager.setAdapter(adapter);
    }


}
