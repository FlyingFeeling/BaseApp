package com.feeling.mybaseapp.interceptor;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 123 on 2017/8/11.
 */

public class BodyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request oldRequest = chain.request();
        RequestBody body = oldRequest.body();
        if(body==null){
            return chain.proceed(chain.request());
        }
        if(body instanceof FormBody){
            if(!isAdd((FormBody) body)){
                return chain.proceed(chain.request());
            }
        }

        // 添加新的参数
        String sign = "";
        RequestBody newBody = null;
        if (body instanceof FormBody) {
            newBody = addToFormBody((FormBody) body, sign);
        } else if (body instanceof MultipartBody) {
            newBody = addToMultipartBody((MultipartBody) body, sign);
        }

        if (null != newBody) {
            Request newRequest = oldRequest.newBuilder()
                    .url(oldRequest.url())
                    .method(oldRequest.method(), newBody)
                    .build();
            return chain.proceed(newRequest);
        }
        return chain.proceed(oldRequest);
    }

    private RequestBody addToFormBody(FormBody body, String sign) {

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("sign", sign);

        for (int i = 0; i < body.size(); i++) {
            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
        }
        RequestBody newBody = builder.build();
        return newBody;
    }

    private RequestBody addToMultipartBody(MultipartBody body,  String sign) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("sign", sign);
        for (int i = 0; i < body.size(); i++) {
            builder.addPart(body.part(i));
        }

        RequestBody newBody = builder.build();
        return newBody;
    }
    private boolean isAdd(FormBody body){
        for (int i = 0; i < body.size(); i++) {
            if(body.encodedName(i).equals("sign")||body.encodedName(i).equals("equipment")){
                return false;
            }
        }
        return true;
    }
}
