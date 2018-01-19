package com.feeling.mybaseapp.interfac;

import io.reactivex.disposables.Disposable;

/**
 * Created by 123 on 2018/1/3.
 */

public interface NetworkCallback {
    /**
     * Disposable 用于管理本次请求任务
     * */
    void onSubscribe(Disposable d, int flag);
    /**
     * 网络请求成功
     * */
    void onSuccess(Object object, int flag);
    /**
     * 网络请求失败
     * */
    void onError(Throwable e , int code, int flag);
    /**
     * 网络请求完成
     * */
    void onComplete();
}
