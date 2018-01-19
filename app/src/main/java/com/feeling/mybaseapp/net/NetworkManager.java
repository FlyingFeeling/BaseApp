package com.feeling.mybaseapp.net;

import android.content.Context;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.interfac.DownloadListener;
import com.feeling.mybaseapp.interfac.NetworkCallback;
import com.feeling.mybaseapp.module.NewBean;
import com.feeling.mybaseapp.module.ResultData;
import com.feeling.mybaseapp.utils.NetWorkUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by 123 on 2018/1/3.
 */

public class NetworkManager {

    private Context context;

    private static final NetworkManager INSTANCE = new NetworkManager();

    private NetworkManager(){}

    public static final NetworkManager getInstance() {
        return NetworkManager.INSTANCE;
    }

    public void setContext(Context context) {
        WeakReference<Context> weakReference=new WeakReference<Context>(context);
        this.context=weakReference.get();
    }

    public void getNews(HashMap<String,String> params, NetworkCallback callback, int flag){
        Observable<ResultData<List<NewBean>>> observable=NetworkConfig.getService().getNews(params);
        invoke(observable , callback , flag);
    }

    public void download(String url, File file, DownloadListener listener, int flag){
        Observable<ResponseBody> observable=NetworkConfig.getDownloadService(listener).download(url);
        downLoad(observable,listener,file,flag);
    }

    public void postData(HashMap<String,String> tvParams,NetworkCallback callback ,int flag){
        MediaType textType = MediaType.parse("text/plain");
        Map<String, RequestBody> params = new HashMap<>();
        for (Map.Entry<String, String> entry : tvParams.entrySet()) {
            if (entry.getKey().equals("file")) {
                File file = new File(entry.getValue());
                RequestBody requestFile = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                params.put(entry.getKey() + "\";filename=\"" + file.getName(), requestFile);
            } else {
                RequestBody txParam = RequestBody.create(textType, entry.getValue());
                params.put(entry.getKey(), txParam);
            }
        }
        Observable<ResultData<String>> observable = NetworkConfig.getService().enterpriseAuthen(params);
        invoke(observable,callback,flag);
    }

    private  <T> void invoke(final Observable<T> observable, final NetworkCallback callback, final int flag){
        Observable isNetwork=Observable.create(new ObservableOnSubscribe<ResultData<T>>() {
            @Override
            public void subscribe(ObservableEmitter<ResultData<T>> emitter) throws Exception {
                if(NetWorkUtil.isAvailable(context)){
                    emitter.onComplete();
                }else {
                    ResultData<T> resultData =new ResultData<>();
                    resultData.code=0;
                    resultData.msg=context.getString(R.string.no_net_work);
                    emitter.onNext(resultData);
                }
            }
        });

        Observable.concat(isNetwork,observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultData<T>>() {
            @Override
            public void onSubscribe(Disposable d) {
                callback.onSubscribe(d,flag);
            }

            @Override
            public void onNext(ResultData<T> result) {
                if(result.code==10000){
                    callback.onSuccess(result.getData(),flag);
                }else {
                    Throwable e=new Throwable(result.msg);
                    callback.onError(e,result.code,flag);
                }
            }

            @Override
            public void onError(Throwable e) {
                callback.onError(e,-1,flag);
            }

            @Override
            public void onComplete() {
                callback.onComplete();
            }
        });
    }

    private void downLoad(Observable<ResponseBody> observable , final DownloadListener listener, final File file, final int flag){
        Observable isNetwork=Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(ObservableEmitter<ResponseBody> emitter) throws Exception {
                if(NetWorkUtil.isAvailable(context)){
                    emitter.onComplete();
                }else {
                    Throwable e = new Throwable(context.getString(R.string.no_net_work));
                    emitter.onError(e);
                }
            }
        });

        Observable.concat(isNetwork,observable)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody,File>() {

            @Override
            public File apply(ResponseBody responseBody) throws Exception {
                InputStream in =responseBody.byteStream();
                FileOutputStream fos=new FileOutputStream(file);
                int byteread = 0;
                byte[] buffer = new byte[1024];
                while ((byteread = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteread);
                }
                in.close();
                fos.flush();
                fos.close();
                return file;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                listener.onSuccess(file,flag);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                listener.onError(throwable,flag);
            }
        });
    }
}
