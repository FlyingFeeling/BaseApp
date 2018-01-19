package com.feeling.mybaseapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.base.BaseFragment;
import com.feeling.mybaseapp.config.GlideApp;
import com.feeling.mybaseapp.utils.DensityUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private ImageView imageView;

    public ThirdFragment() {
        // Required empty public constructor
    }

    public static ThirdFragment newInstance(String param1) {
        ThirdFragment fragment = new ThirdFragment();
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
        return R.layout.fragment_third;
    }

    @Override
    protected void initView() {
        imageView=parentView.findViewById(R.id.image);
        GlideApp.with(getContext()).load("http://192.168.0.189/jpg_temp.jpg")
                .override(DensityUtil.dp2px(getContext(),80),DensityUtil.dp2px(getContext(),80))
                .circleCrop()
                .into(imageView);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(Object object, int flag) {

    }

    @Override
    public void onError(Throwable e, int code, int flag) {

    }
}
