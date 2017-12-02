package com.wxl.upapkdemo.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by wxl on 2017/12/2.
 * 下载完成接收广播
 */

public class CompleteReceiver extends BroadcastReceiver {
    private long mReference;

    public CompleteReceiver(long reference){

        mReference = reference;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("===","完成了调用");
        long references = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (references == mReference) {
            Log.e("===","完成了");
        }
    }
}
