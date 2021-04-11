package com.example.copywechatmoments;

import android.app.Application;
import android.content.Context;
/**
 * @Description: java类作用描述
 * @Author: jakezhang
 * @CreateDate: 2021/4/11
 */
public class App extends Application {
    //全局Context
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }


    public static Context getContext() {
        return sContext;
    }
}
