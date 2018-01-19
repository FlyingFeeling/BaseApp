package com.feeling.mybaseapp.base;


import android.content.Context;
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
public abstract class BaseLazyFragment extends Fragment implements NetworkCallback {
    protected String TAG="BaseLazyFragment";

    protected boolean isInitView = false;//视图是否已经加载
    protected boolean isLoadData = false;//数据是否已经加载
    protected boolean isVisible = false;//视图是否可见

    protected View parentView;
    protected Disposable disposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(setContentView()==0){
            throw new NullPointerException("请实现setContentView方法，传入正确的layout视图");
        }
        if(parentView==null){
            parentView = inflater.inflate(setContentView(), container, false);
        }
        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isInitView=true;
        initView(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCanLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible=isVisibleToUser;
        isCanLoadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInitView = false;
        isVisible  = false;
    }

    protected abstract  @LayoutRes int setContentView();

    /**
     * 初始化视图
     * */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     * */
    protected abstract void initData();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {}

    @Override
    public void onSubscribe(Disposable d, int flag) {
        disposable=d;
    }

    @Override
    public void onComplete() {

    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInitView) {
            return;
        }
        if (isVisible && !isLoadData) {
            initData();
            isLoadData = true;
            return;
        }

        if (isLoadData) {
            stopLoad();
        }

    }
}
