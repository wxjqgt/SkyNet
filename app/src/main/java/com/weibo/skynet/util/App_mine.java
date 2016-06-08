package com.weibo.skynet.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wxjqgt on 2015/12/17.
 */

public class App_mine extends Application {

    public static Context context_app;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        context_app = getApplicationContext();
        sp = getSharedPreferences("MyLibrary",MODE_PRIVATE);
    }

}
