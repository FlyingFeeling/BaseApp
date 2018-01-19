package com.feeling.mybaseapp.net;


import com.feeling.mybaseapp.module.NewBean;
import com.feeling.mybaseapp.module.ResultData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by 123 on 2018/1/3.
 */

public interface APIService {

    /**
     * POST请求
     * */
    @FormUrlEncoded
    @POST(ConnectUrls.NEWS_LIST)
    Observable<ResultData<List<NewBean>>> getNews(@FieldMap Map<String, String> params);

    /**
     * 上传文件
     * */
    @Multipart
    @POST(ConnectUrls.UPLOAD_CRASH)
    Observable<ResponseBody> uploadCrash(@PartMap Map<String, RequestBody> imgParams);

    /**
     * 上传图文
     * */
    @Multipart
    @POST(ConnectUrls.ENTERPRISE_AUTHENTICATE)
    Observable<ResultData<String>> enterpriseAuthen(@PartMap Map<String, RequestBody> imgParams);

    /**
     * 文件下载
     * */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
