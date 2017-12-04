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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/2.
 */

public class DownloadActivity extends AppCompatActivity {

    private DownloadManager mDownloadManager;
    private long mId;
    //"content://downloads/my_downloads"必须这样写不可更改
    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    private DownloadChangeObserver mObserver;
    private ProgressBar mProgressBar;
    private TextView mDownStatus;
    private TextView mDownDes;
    private DownloadManager mManager;
    private DownloadManager.Query mQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mDownDes = findViewById(R.id.des);
        mDownStatus = findViewById(R.id.down_status);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setMax(100);

        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mId = getIntent().getLongExtra("id",-1);
        Log.e("===","=id="+mId);
        mQuery = new DownloadManager.Query();
        mQuery.setFilterById(mId);
        mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        mDownStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.remove(mId);
            }
        });

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
            final Cursor cursor = mManager.query(mQuery);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);
                float percent = (float) currentSize / (float) totalSize;
                int progress = Math.round(percent * 100);
                Log.e("===","progress = "+progress);
                mProgressBar.setProgress(progress);

                if(progress == 100) {

                }
                //下载文件的URL链接
                String url =cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));

                //下载的文件到本地的目录
                String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                mDownDes.setText("正在下载 "+progress+"%");

                Log.e("===","下载文件的URL链接="+url);
                Log.e("===","address="+address);
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                Log.e("","");

            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
