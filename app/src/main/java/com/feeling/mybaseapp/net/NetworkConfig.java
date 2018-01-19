package com.feeling.mybaseapp.net;

import com.feeling.mybaseapp.interceptor.DownloadProgressInterceptor;
import com.feeling.mybaseapp.interfac.DownloadListener;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 123 on 2018/1/3.
 */

public class NetworkConfig {
    private static OkHttpClient getOkHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    /**
     * 获取下载的OkHttpClient实例
     */
    private static OkHttpClient getOkHttpDownloadClient(DownloadListener listener) {
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    private static Retrofit getRetrofit(OkHttpClient okHttpClient){
        return new Retrofit.Builder().client(okHttpClient).baseUrl(ConnectUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

    /**
     * 获取APIService</>
     *
     * @return
     */
    public static APIService getService() {
        return getRetrofit(getOkHttpClient()).create(APIService.class);
    }


    /**
     * 获取下载的 APIService
     */
    public static APIService getDownloadService(DownloadListener listener) {
        return getRetrofit(getOkHttpDownloadClient(listener)).create(APIService.class);
    }

    public static Retrofit getDefault(){
        return new Retrofit.Builder().baseUrl(ConnectUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    }

}
