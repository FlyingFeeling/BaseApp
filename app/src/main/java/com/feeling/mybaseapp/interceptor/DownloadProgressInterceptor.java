package com.feeling.mybaseapp.interceptor;


import com.feeling.mybaseapp.interfac.DownloadListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by 李松斌 on 2016/12/6.
 */

public class DownloadProgressInterceptor implements Interceptor {

    private DownloadListener listener;

    public DownloadProgressInterceptor(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse=chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownloadProgressResponseBody(originalResponse.body(),listener))
                .build();
    }
}
