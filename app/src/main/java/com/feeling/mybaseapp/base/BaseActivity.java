package com.feeling.mybaseapp.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.immersive.ImmersionBar;
import com.feeling.mybaseapp.interfac.NetworkCallback;
import com.feeling.mybaseapp.manage.AppStatusConstant;
import com.feeling.mybaseapp.manage.AppStatusManager;
import com.feeling.mybaseapp.ui.activity.MainActivity;
import com.feeling.mybaseapp.utils.ActivityUtil;
import com.feeling.mybaseapp.utils.KeyboardUtil;

import io.reactivex.disposables.Disposable;

/**
 * Created by 123 on 2018/1/12.
 */

public abstract class BaseActivity extends AppCompatActivity implements NetworkCallback{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        switch (AppStatusManager.getInstance().getAppStatus()) {
            case AppStatusConstant.STATUS_FORCE_KILLED:
                onRestartApp();
                break;
            case AppStatusConstant.STATUS_NORMAL:
                if (isImmersive()) {
                    initImmersive();
                }
                initView();
                initData(savedInstanceState);
                break;
            default:
                break;
        }
    }


    /** 初始化View */
    protected abstract void initView();

    /** 初始化Data */
    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onSubscribe(Disposable d, int flag) {

    }

    @Override
    public void onComplete() {

    }

    /**
     * 是否启用沉浸式，默认是true；当不启用时在5.0以下默认状态栏为黑色，5.0以上为colorPrimaryDark
     * */
    @Override
    public boolean isImmersive() {
        return true;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void finish() {
        super.finish();
        KeyboardUtil.hideSoftInput(this);
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.getInstance().removeActivty(this);
        super.onDestroy();
        if(immersionBar!=null){
            immersionBar.destroy();
        }
    }

    /**
     * 当app进程在后台被杀死重新进入时，重新加载 Mainactivity，在 Mainactivity的 onNewIntent()
     * 里重新启动 启动页（LaunchActivity）
     * */
    protected void onRestartApp(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AppStatusConstant.KEY_HOME_ACTION,AppStatusConstant.ACTION_RESTART_APP);
        startActivity(intent);
    }

    protected ImmersionBar immersionBar;
    protected void initImmersive() {
        if(immersionBar==null){
            immersionBar=ImmersionBar.with(this);
        }
        immersionBar.keyboardEnable(true)
                .init();
    }

    public ImmersionBar getImmersionBar(){
        if(immersionBar!=null){
            return immersionBar;
        }
        return null;
    }
}
