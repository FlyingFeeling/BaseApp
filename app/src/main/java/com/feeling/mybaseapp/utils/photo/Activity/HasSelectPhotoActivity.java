package com.feeling.mybaseapp.utils.photo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.ui.view.MyToolbar;
import com.feeling.mybaseapp.utils.photo.myInterface.IHasSelectPhoto;
import com.feeling.mybaseapp.utils.photo.presenler.PreviewVPAdapter;
import com.feeling.mybaseapp.utils.photo.presenler.ShowHasPhotos;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 已选中图片展示页面
 */
public class HasSelectPhotoActivity extends AppCompatActivity implements View.OnClickListener,
        ViewPager.OnPageChangeListener, IHasSelectPhoto {
    private ArrayList<String> hasSelects = new ArrayList<>();
    private MyToolbar mToolbar;
    private ViewPager mViewPager;
    private CheckBox isSelect;
    private Button btn_tailoring;
    private int nowPosition = 0;
    private ShowHasPhotos mShowHasPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//设置全屏但不隐藏状态栏
        decorView.setSystemUiVisibility(option);
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        setContentView(R.layout.activity_has_select_photo);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.vp_hasSelect);
        isSelect = (CheckBox) findViewById(R.id.isSelect);
        btn_tailoring = (Button) findViewById(R.id.btn_tailoring);
        btn_tailoring.setOnClickListener(this);
        isSelect.setOnClickListener(this);
        mToolbar.tv_toolbar_right.setOnClickListener(this);
        mToolbar.mNavButtonView.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        hasSelects = getIntent().getStringArrayListExtra("hasSelect");
        mShowHasPhotos = new ShowHasPhotos(hasSelects, this);
        mViewPager.setAdapter(new PreviewVPAdapter(HasSelectPhotoActivity.this, hasSelects));
//        boolean needTailoring = PhotoUtils.getPhotoUtils().isNeedTailoring();
        if (!true) {
            btn_tailoring.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            mShowHasPhotos.selectPhotoListChange(nowPosition, UCrop.getOutput(data).getPath());
        } else if (resultCode == UCrop.RESULT_ERROR) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navButton:
                finish();
                break;
            case R.id.tv_toolbar_right:
                mShowHasPhotos.confirmChoose();
                break;
            case R.id.btn_tailoring:
                try {
                    cropPhoto(hasSelects.get(nowPosition));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.isSelect:
                mShowHasPhotos.selectChange(isSelect.isChecked(), hasSelects.get(nowPosition));
                break;
        }
    }

    /**
     * 剪切图片
     *
     * @param source 要剪切的图片路径
     */
    private void cropPhoto(String source) throws IOException {
        Uri destinationUri = mShowHasPhotos.getNewFileUri(getApplicationContext());
        if (destinationUri != null) {
            File sourceFile = new File(source);
            Uri uri = Uri.fromFile(sourceFile);
            UCrop uCrop = UCrop.of(uri, destinationUri)
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(1000, 600);
            UCrop.Options options = new UCrop.Options();
            options.setToolbarColor(ActivityCompat.getColor(this, R.color.theme));
            options.setStatusBarColor(ActivityCompat.getColor(this, R.color.theme));
            uCrop.withOptions(options);
            uCrop.start(this);
        } else {
            Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        nowPosition = position;
        mShowHasPhotos.setSelectView(position);
    }

    @Override
    public void selectPhotoListsChange(ArrayList<String> photoPaths) {
        mViewPager.setAdapter(new PreviewVPAdapter(this, photoPaths));
        mViewPager.setCurrentItem(nowPosition);
    }

    @Override
    public void setSelectView(boolean shouldCheck) {
        isSelect.setChecked(shouldCheck);
    }

    @Override
    public void selectResult(ArrayList<String> photoPaths) {
//        Intent intent = new Intent();
        Intent intent = HasSelectPhotoActivity.this.getIntent();
        intent.putStringArrayListExtra("paths", photoPaths);
        HasSelectPhotoActivity.this.setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
