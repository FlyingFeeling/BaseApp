package com.feeling.mybaseapp.net;

import android.content.Context;

/**
 * Created by 123 on 2018/1/3.
 */

public class Network {
    public static NetworkManager with(Context context){
        NetworkManager networkManager = NetworkManager.getInstance();
        networkManager.setContext(context.getApplicationContext());
        return networkManager;
    }
}
