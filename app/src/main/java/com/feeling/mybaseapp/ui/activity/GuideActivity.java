package com.feeling.mybaseapp.ui.activity;

import android.os.Bundle;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.base.BaseActivity;
import com.feeling.mybaseapp.utils.AppUtil;
import com.feeling.mybaseapp.utils.SpUtil;

public class GuideActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        SpUtil sp=SpUtil.getInstance(GuideActivity.this,"PallanHarter");
        sp.putString("version", AppUtil.getVersionName(GuideActivity.this));
        super.onDestroy();
    }

    @Override
    public void onSuccess(Object object, int flag) {

    }

    @Override
    public void onError(Throwable e, int code, int flag) {

    }
}
