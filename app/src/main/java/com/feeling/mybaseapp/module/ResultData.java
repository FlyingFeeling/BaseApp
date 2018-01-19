package com.feeling.mybaseapp.module;

/**
 * Created by 123 on 2018/1/3.
 */

public class ResultData<T> {
    public int code;
    public String msg;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
