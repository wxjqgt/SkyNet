package com.weibo.skynet.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.weibo.skynet.util.MainService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private ArrayList<Activity> list = new ArrayList<>();
    private static ServiceConnection connection = null;
    public static MainService ms;
    private boolean isbind = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(this);
        connection = new MainServiceConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        findView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.clearCaches();
    }

    protected void findView(){}

    //绑定服务
    public void bindMainService(){
        Intent intent = new Intent(this, MainService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        isbind = true;
    }

    //解绑服务
    public void UnbindMainService(){
        if (isbind){
            unbindService(connection);
            isbind = false;
        }
    }

    private class MainServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ms = ((MainService.MainBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ms = null;
        }
    }

    public void exit(){
        for (Activity a:list) {
            a.finish();
        }
    }


    @Override
    public void finish() {
        super.finish();
        onDestroy();
    }

}
