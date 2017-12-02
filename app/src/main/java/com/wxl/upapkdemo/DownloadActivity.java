package com.wxl.upapkdemo;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2017/12/2.
 */

public class DownloadActivity extends AppCompatActivity {

    private DownloadManager mDownloadManager;
    private long mId;
    //"content://downloads/my_downloads"必须这样写不可更改
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private DownloadChangeObserver mObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mId = getIntent().getLongExtra("id",-1);
        Log.e("===","=id="+mId);

        mObserver = new DownloadChangeObserver(null);
        getContentResolver().registerContentObserver(CONTENT_URI,true, mObserver);
    }

    //用于显示下载进度
    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mId);
            DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final Cursor cursor = dManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                int progress = Math.round(percent * 100);
                Log.e("===","progress="+progress);
                if(progress == 100) {

                }
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
