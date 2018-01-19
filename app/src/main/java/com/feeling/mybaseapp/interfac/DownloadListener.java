package com.feeling.mybaseapp.interfac;

import java.io.File;

/**
 * Created by feeling on 2016/12/6.
 * 监听文件下载的接口
 */

public interface DownloadListener {
    /**
     * @param total        总字节数
     * @param progress     已经下载或上传字节数
     * @param done         是否完成
     */
    void onProgress(long total, long progress, boolean done);
    void onSuccess(File file, int flag);
    void onError(Throwable e, int flag);
}
