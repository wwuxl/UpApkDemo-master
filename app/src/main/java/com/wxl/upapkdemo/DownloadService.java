package com.wxl.upapkdemo;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wxl on 2017/11/29.
 */

public class DownloadService extends IntentService {
    private String baseUrl="http://192.168.120.26:8080/";
    public DownloadService() {
        super("DownloadService");
        //Log.e("===","DownloadService thread= "+Thread.currentThread().getName());


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("===","thread= "+Thread.currentThread().getName());


    }


}
