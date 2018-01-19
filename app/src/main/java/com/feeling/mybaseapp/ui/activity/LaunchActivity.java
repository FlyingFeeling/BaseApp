package com.feeling.mybaseapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.manage.AppStatusConstant;
import com.feeling.mybaseapp.manage.AppStatusManager;
import com.feeling.mybaseapp.utils.AppUtil;
import com.feeling.mybaseapp.utils.SpUtil;

import java.lang.ref.WeakReference;

public class LaunchActivity extends AppCompatActivity {

    private LoadingThread loadingThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        AppStatusManager.getInstance().setAppStatus(AppStatusConstant.STATUS_NORMAL); //进入应用状态正常
        loadingThread=new LoadingThread(LaunchActivity.this);
        loadingThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onStop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private static class LoadingThread extends Thread{
        WeakReference<Context> context;
        String version="";
        public LoadingThread(Context context) {
            this.context=new WeakReference<Context>(context);
            SpUtil sp=SpUtil.getInstance(context,"PallanHarter");
            version=sp.getString("version");
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            try {
                Thread.sleep(1500);
                Intent intent;
                if(!AppUtil.getVersionName(context.get()).equals(version)){
                    intent=new Intent(context.get(),GuideActivity.class);
                }else {
                    intent=new Intent(context.get(),MainActivity.class);
                }
                context.get().startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
