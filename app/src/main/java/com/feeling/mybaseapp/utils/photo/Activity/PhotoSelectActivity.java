package com.feeling.mybaseapp.utils.photo.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.ui.view.MyToolbar;
import com.feeling.mybaseapp.utils.photo.Utils.PhotoUtils;
import com.feeling.mybaseapp.utils.photo.model.PhotoAlbumsInfo;
import com.feeling.mybaseapp.utils.photo.myInterface.ISelectPhotoItem;
import com.feeling.mybaseapp.utils.photo.myInterface.IShowPhotos;
import com.feeling.mybaseapp.utils.photo.myInterface.OnSelectPhotoResult;
import com.feeling.mybaseapp.utils.photo.presenler.PhotoAlbumsAdapter;
import com.feeling.mybaseapp.utils.photo.presenler.PhotoSelectAdapter;
import com.feeling.mybaseapp.utils.photo.presenler.ShowPhotos;
import com.feeling.mybaseapp.utils.photo.view.PhotoGridItemDecoration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class PhotoSelectActivity extends AppCompatActivity implements View.OnClickListener
        , AdapterView.OnItemClickListener, IShowPhotos, ISelectPhotoItem,EasyPermissions.PermissionCallbacks{
    private RecyclerView mRecyclerView;
    private MyToolbar mToolbar;
    private TextView tv_albums_select, tv_photo_preview;
    private FrameLayout fl_photo_albums;
    private ListView lv_photo_albums;
    private List<PhotoAlbumsInfo> mAlbumsInfos;
    private int maxLastImgCount = 100;//最近照片最多显示多少
    private int columnsNum = 3;//照片按几列显示
    private boolean needTailoring = true;//是否需要裁剪
    private int maxSelectPhotoCount;//图片最大选择数量
    private static final int STARTCAMERA = 300;//开启照相机的请求码
    private String cameraPath; //照相机所拍照片路径
    private ArrayList<String> checkImgPath;//已选择的照片路径集合
    private static final int STARTPRIVIEW = 200;//开启预览页面的请求码
    private ShowPhotos mShowPhotos;//界面展示类
    private OnSelectPhotoResult mSelectPhotoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            cameraPath = savedInstanceState.getString("cameraPath");
            maxSelectPhotoCount = savedInstanceState.getInt("maxSelectPhotoCount");
        } else {
            maxSelectPhotoCount = getIntent().getIntExtra("Count", 10);
        }
        mShowPhotos = new ShowPhotos(this, this);
        if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            initView();
        }else {
            EasyPermissions.requestPermissions(this,
                    "接下来需要获取存储权限、定位、拍照权限",
                    R.string.yes,
                    R.string.no,
                    1,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA);
        }

        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//设置全屏但不隐藏状态栏
        decorView.setSystemUiVisibility(option);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        setContentView(R.layout.activity_photo_select);
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
        mToolbar.tv_toolbar_right.setText("确定(0/" + maxSelectPhotoCount + ")");
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
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.feeling.mybaseapp.FileProvider", file);
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
            tv_photo_preview.setText("预览(" + size + ")");
        } else {
            tv_photo_preview.setText("预览");
            tv_photo_preview.setEnabled(false);
        }
        mToolbar.tv_toolbar_right.setText("确定(" + size + "/" + maxSelectPhotoCount + ")");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == STARTCAMERA) {
                File file = new File(cameraPath);
                if (file.exists()) {
                    ArrayList<String> list = new ArrayList<>();

                    list.add(cameraPath);
                    startPreview(list);
                }
            } else if (requestCode == STARTPRIVIEW) {
                setResult(Activity.RESULT_OK, data);
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
                startPreview(checkImgPath);
                break;
            case R.id.fl_photo_albums:
                fl_photo_albums.setVisibility(View.GONE);
                break;
            case R.id.tv_toolbar_right:
                if (checkImgPath != null && checkImgPath.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("paths", checkImgPath);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    /**
     * 进入预览界面
     *
     * @param list 选中照片路径集合
     */
    private void startPreview(ArrayList<String> list) {
        Intent intent = new Intent(this, HasSelectPhotoActivity.class);
        intent.putStringArrayListExtra("hasSelect", list);
        startActivityForResult(intent, STARTPRIVIEW);
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
    protected void onDestroy() {
//        PhotoUtils.getPhotoUtils().finish();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("cameraPath", cameraPath);
        outState.putInt("maxSelectPhotoCount", maxSelectPhotoCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        initView();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        StringBuffer sb = new StringBuffer();
        for (String str : perms){
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2,sb.length(),"");
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("允许")
                    .setNegativeButton("拒绝")
                    .build()
                    .show();
        }
        finish();
    }
}
