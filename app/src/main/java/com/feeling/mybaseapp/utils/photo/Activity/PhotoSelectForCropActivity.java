package com.feeling.mybaseapp.utils.photo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.ui.view.MyToolbar;
import com.feeling.mybaseapp.utils.photo.Utils.PhotoUtils;
import com.feeling.mybaseapp.utils.photo.model.PhotoAlbumsInfo;
import com.feeling.mybaseapp.utils.photo.myInterface.IAboutPermission;
import com.feeling.mybaseapp.utils.photo.myInterface.ISelectPhotoItem;
import com.feeling.mybaseapp.utils.photo.myInterface.IShowPhotos;
import com.feeling.mybaseapp.utils.photo.myInterface.OnSelectPhotoResult;
import com.feeling.mybaseapp.utils.photo.presenler.AboutPermission;
import com.feeling.mybaseapp.utils.photo.presenler.FileUtils;
import com.feeling.mybaseapp.utils.photo.presenler.PhotoAlbumsAdapter;
import com.feeling.mybaseapp.utils.photo.presenler.PhotoSelectAdapter;
import com.feeling.mybaseapp.utils.photo.presenler.ShowPhotos;
import com.feeling.mybaseapp.utils.photo.view.PhotoGridItemDecoration;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhotoSelectForCropActivity extends AppCompatActivity implements View.OnClickListener
        , AdapterView.OnItemClickListener, IShowPhotos, IAboutPermission, ISelectPhotoItem {

    private RecyclerView mRecyclerView;
    private MyToolbar mToolbar;
    private TextView tv_albums_select, tv_photo_preview;
    private FrameLayout fl_photo_albums;
    private ListView lv_photo_albums;
    private List<PhotoAlbumsInfo> mAlbumsInfos;
    private int maxLastImgCount = 100;//最近照片最多显示多少
    private int columnsNum = 3;//照片按几列显示
    private boolean needTailoring = true;//是否需要裁剪
    private int maxSelectPhotoCount=1;//图片最大选择数量
    private static final int STARTCAMERA = 300;//开启照相机的请求码
    private String cameraPath; //照相机所拍照片路径
    private ArrayList<String> checkImgPath;//已选择的照片路径集合
    private static final int STARTPRIVIEW = 200;//开启预览页面的请求码
    private AboutPermission mAboutPermission;//权限请求类
    private ShowPhotos mShowPhotos;//界面展示类
    private OnSelectPhotoResult mSelectPhotoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            cameraPath = savedInstanceState.getString("cameraPath");
            maxSelectPhotoCount = savedInstanceState.getInt("maxSelectPhotoCount");
        }
        mShowPhotos = new ShowPhotos(this, this);
        mAboutPermission = new AboutPermission(this, this);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//设置全屏但不隐藏状态栏
        decorView.setSystemUiVisibility(option);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        setContentView(R.layout.activity_photo_select_for_crop);
        findView();
        bindListner();
        initData();
    }

    /**
     * 添加监听事件
     */
    private void bindListner() {
        tv_albums_select.setOnClickListener(this);
        tv_photo_preview.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.tv_toolbar_right.setOnClickListener(this);
        fl_photo_albums.setOnClickListener(this);
        lv_photo_albums.setOnItemClickListener(this);
    }

    /**
     * 绑定View
     */
    private void findView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);
        tv_albums_select = (TextView) findViewById(R.id.tv_albums_select);
        tv_photo_preview = (TextView) findViewById(R.id.tv_photo_preview);
        fl_photo_albums = (FrameLayout) findViewById(R.id.fl_photo_albums);
        lv_photo_albums = (ListView) findViewById(R.id.lv_photo_albums);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        PhotoUtils photoUtils = PhotoUtils.getPhotoUtils();
        if (photoUtils == null) {

        } else {
            maxLastImgCount = photoUtils.getMaxLastImgCount();
            needTailoring = photoUtils.isNeedTailoring();
            columnsNum = photoUtils.getColumnsNum();
            maxSelectPhotoCount = photoUtils.getMaxSelectPhotoCount();
            mSelectPhotoResult = photoUtils.getSelectPhotoResult();
        }
        tv_photo_preview.setEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columnsNum));
        mRecyclerView.addItemDecoration(new PhotoGridItemDecoration());
        mShowPhotos.readLastPhoto(maxLastImgCount);//获取最近图片
        mShowPhotos.readPhotoAlbums();//获取相册
    }

    @Override
    public void startCamera() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        try {
            file = mShowPhotos.startCamera(getApplicationContext());
            cameraPath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.pallanharter" +
                        ".environmentalprotection.FileProvider", file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            } else {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            startActivityForResult(takePictureIntent, STARTCAMERA);
        }
    }

    @Override
    public void selectPhoto(ArrayList<String> photoPaths) {
        checkImgPath = photoPaths;
        int size = checkImgPath.size();
        checkImgPath = photoPaths;
        if (size > 0) {
            tv_photo_preview.setEnabled(true);
            tv_photo_preview.setText("选 择");
        } else {
            tv_photo_preview.setText("选 择");
            tv_photo_preview.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == STARTCAMERA) {
                File file = new File(cameraPath);
                if (file.exists()) {
                    try {
                        cropPhoto(cameraPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if (requestCode == UCrop.REQUEST_CROP && data != null) {
                if(checkImgPath==null){
                    checkImgPath=new ArrayList<>();
                }
                checkImgPath.clear();
                checkImgPath.add(UCrop.getOutput(data).getPath());
                Intent intent = new Intent();
                intent.putExtra("paths", checkImgPath);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navButton:
                finish();
                break;
            case R.id.tv_albums_select:
                fl_photo_albums.setVisibility(
                        fl_photo_albums.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.tv_photo_preview:
                try {
                    cropPhoto(checkImgPath.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fl_photo_albums:
                fl_photo_albums.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 剪切图片
     *
     * @param source 要剪切的图片路径
     */
    private void cropPhoto(String source) throws IOException {
        Uri destinationUri = Uri.parse(new FileUtils().getNewFileUri(this).getAbsolutePath());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fl_photo_albums.getVisibility() == View.VISIBLE) {
                fl_photo_albums.setVisibility(View.GONE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mAboutPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            mShowPhotos.readLastPhoto(maxLastImgCount);
        } else {
            mShowPhotos.readPhotoByAlbum(mAlbumsInfos.get(position).getPhotoAlbumsPath());
        }
        fl_photo_albums.setVisibility(View.GONE);
        tv_albums_select.setText(mAlbumsInfos.get(position).getPhotoAlbumsName());
        if (checkImgPath != null) {
            checkImgPath.clear();
            tv_photo_preview.setText("预览");
        }
    }

    @Override
    public void bindPhotoAdapter(ArrayList<String> photoPaths) {
        mRecyclerView.setAdapter(new PhotoSelectAdapter(this, photoPaths, this, maxSelectPhotoCount));
    }

    @Override
    public void bindPhotoAlbumsAdapter(ArrayList<PhotoAlbumsInfo> photoAlbums) {
        mAlbumsInfos = photoAlbums;
        lv_photo_albums.setAdapter(new PhotoAlbumsAdapter(this, photoAlbums));
    }

    @Override
    public void hasPermission() {
        initView();
    }

    @Override
    public void errorHasPermission() {
        Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showDiaLog(Dialog dialog) {
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("cameraPath", cameraPath);
        outState.putInt("maxSelectPhotoCount", maxSelectPhotoCount);
        super.onSaveInstanceState(outState);
    }

}
