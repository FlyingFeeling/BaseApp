package com.feeling.mybaseapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioGroup;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.base.BaseActivity;
import com.feeling.mybaseapp.manage.AppStatusConstant;
import com.feeling.mybaseapp.ui.fragment.FirstFragment;
import com.feeling.mybaseapp.ui.fragment.SecondFragment;
import com.feeling.mybaseapp.ui.fragment.ThirdFragment;
import com.feeling.mybaseapp.utils.ToastUtil;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    public static final int REQUEST_PERMISSION=0x123;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        radioGroup=findViewById(R.id.tab_group);
        firstFragment=FirstFragment.newInstance("FirstFragment");
        secondFragment=SecondFragment.newInstance("SecondFragment");
        thirdFragment=ThirdFragment.newInstance("ThirdFragment");
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.add(R.id.content_view,firstFragment);
        transaction.add(R.id.content_view,secondFragment);
        transaction.add(R.id.content_view,thirdFragment);
        transaction.hide(firstFragment);
        transaction.hide(secondFragment);
        transaction.hide(thirdFragment);
        transaction.commit();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        permission();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                switch (checkedId){
                    case R.id.first:
                        transaction.show(firstFragment);
                        transaction.hide(secondFragment);
                        transaction.hide(thirdFragment);
                        break;
                    case R.id.second:
                        transaction.show(secondFragment);
                        transaction.hide(firstFragment);
                        transaction.hide(thirdFragment);
                        break;
                    case R.id.third:
                        transaction.show(thirdFragment);
                        transaction.hide(firstFragment);
                        transaction.hide(secondFragment);
                        break;
                    default:
                            break;
                }
                transaction.commit();
            }
        });
        radioGroup.check(R.id.first);
    }

    @Override
    public boolean isImmersive() {
        return false;
    }

    @Override
    protected void onRestartApp() {
        startActivity(new Intent(this, LaunchActivity.class));
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int action = intent.getIntExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_BACK_TO_HOME);
        switch (action) {
            case AppStatusConstant.ACTION_RESTART_APP:
                onRestartApp();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode ",requestCode+" resultCode "+resultCode );
        if(requestCode==0x001){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    private void uploadPatch(){
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/patch_signed_7zip.apk");
    }

    @Override
    public void onSuccess(Object object, int flag) {

    }

    @Override
    public void onError( Throwable e, int code,int flag) {

    }

    @AfterPermissionGranted(REQUEST_PERMISSION)
    private void permission(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            ToastUtil.showToast(this,"已获取存储权限");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    new PermissionRequest
                            .Builder(this,REQUEST_PERMISSION,perms)
                            .setRationale("尊敬的用户\n为了您能更好的使用本应用\n需要申请存储权限")
                            .setNegativeButtonText("拒绝")
                            .setPositiveButtonText("好的")
                            .build()
            );
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e("Granted", "onPermissionsGranted:" + requestCode + ":" + perms.toString());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e("Denied", "onPermissionsDenied:" + requestCode + ":" + perms.toString());
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .setTitle("温馨提示")
                    .setRationale("尊敬的用户为了您能更好的使用本应用需要申请存储权限")
                    .setNegativeButton("拒绝")
                    .setPositiveButton("去设置")
                    .setRequestCode(0x001)
                    .build()
                    .show();
        }
    }
}
