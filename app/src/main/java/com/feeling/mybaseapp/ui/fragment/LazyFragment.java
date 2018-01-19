package com.feeling.mybaseapp.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.base.BaseLazyFragment;
import com.feeling.mybaseapp.module.NewBean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LazyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LazyFragment extends BaseLazyFragment {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    private NewBean newBean;
    TextView textView;
    public LazyFragment() {
        // Required empty public constructor
    }

    public static LazyFragment newInstance(String param1) {
        LazyFragment fragment = new LazyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        fragment.setMyTag(param1);
        return fragment;
    }

    private void setMyTag(String param1){
        TAG=param1;
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
        return R.layout.fragment_lazy;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        textView = parentView.findViewById(R.id.text);
    }

    @Override
    protected void initData() {
        textView.setText(mParam1);
    }

    @Override
    public void onSuccess(Object object, int flag) {

    }

    @Override
    public void onError(Throwable e, int code, int flag) {

    }
}
