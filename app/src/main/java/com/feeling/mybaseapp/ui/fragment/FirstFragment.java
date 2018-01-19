package com.feeling.mybaseapp.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.base.BaseFragment;
import com.feeling.mybaseapp.module.NewBean;
import com.feeling.mybaseapp.net.Network;
import com.feeling.mybaseapp.utils.photo.Activity.PhotoSelectActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends BaseFragment implements OnRefreshListener, OnLoadmoreListener {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.refresh_view)
    SmartRefreshLayout refreshView;
    @BindView(R.id.button)
    Button button;
    Unbinder unbinder;

    private int page = 1;
    private String mParam1;

    public FirstFragment() {
        // Required empty public constructor
    }

    public static FirstFragment newInstance(String param1) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_first;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, parentView);
        Log.e(TAG, "initView:");
    }

    @Override
    protected void initData() {
        refreshView.setOnRefreshListener(this);
        refreshView.setOnLoadmoreListener(this);
    }

    @Override
    @OnClick(R.id.button)
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button:
                intent = new Intent(getContext(), PhotoSelectActivity.class);
                intent.putExtra("Count", 6);
                startActivityForResult(intent,0x002);
                break;
        }
    }

    @Override
    public void onSuccess(Object object, int flag) {
        List<NewBean> temp = (List<NewBean>) object;
    }

    @Override
    public void onError(Throwable e, int code, int flag) {
        Log.e("onError: ", e.getMessage());
    }

    private void post() {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        Network.with(getContext()).getNews(params, this, 0);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 1500);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 1500);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshView.finishRefresh();
            refreshView.finishLoadmore();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
