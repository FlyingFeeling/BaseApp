package com.feeling.mybaseapp.base;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feeling.mybaseapp.interfac.NetworkCallback;

import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener,NetworkCallback {

    public static final String TAG="Fragment";

    protected View parentView;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(setContentView()==0){
            throw new NullPointerException("请实现setContentView方法，传入正确的layout视图");
        }
        parentView = inflater.inflate(setContentView(), container, false);
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract  @LayoutRes int setContentView();

    /**
     * 初始化视图
     * */
    protected abstract void initView();

    /**
     * 初始化数据
     * */
    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSubscribe(Disposable d, int flag) {

    }

    @Override
    public void onComplete() {

    }
}
