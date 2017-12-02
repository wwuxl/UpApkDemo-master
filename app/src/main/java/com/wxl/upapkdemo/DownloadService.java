package com.wxl.upapkdemo;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.wxl.upapkdemo.receiver.ClickReceiver;
import com.wxl.upapkdemo.receiver.CompleteReceiver;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zlc.season.rxdownload.DownloadStatus;
import zlc.season.rxdownload.RxDownload;

/**
 * Created by wxl on 2017/11/29.
 */

public class DownloadService extends Service {
    private Intent updateIntent;
    private PendingIntent pendingIntent;
    private int notification_id = 0;
    private Notification mNotification;
    private String baseUrl = "http://192.168.120.26:8080/";
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mManager;
    private DownloadManager mDownloadManager;
    private long reference;
    private BroadcastReceiver receiver;
    private BroadcastReceiver clickedReceiver;
    private String fileName;
    private String statusMsg;
    private String fileUri;


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("===", "DownloadService thread= " + Thread.currentThread().getName());
        //方式一
        //downloadApk1();
        //方式二 使用下载管理器下载(apk)
        downloadApk();

        return super.onStartCommand(intent, flags, startId);
    }


    private void downloadApk1(){
        //方式一 自己实现下载apk(通知)
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("下载");
        mBuilder.setContentText("正在下载0%");
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setProgress(100, 0, false);
        mManager.notify(notification_id, mBuilder.build());



        //方式一 自己实现下载apk(下载过程)
        String baseUrl1 = baseUrl + "jingdong_53712.apk";
        final Subscription subscription = RxDownload.getInstance()
                .defaultSavePath(createFile())
                .download(baseUrl1, "123456789.apk", null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DownloadStatus>() {
                    @Override
                    public void onCompleted() {
                        //Log.e("===", "onCompleted thread" + Thread.currentThread().getName());
                        //打开安装界面
                        File file = new File(savePath + "123456789.apk");
                        //startActivity(file);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(DownloadService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(final DownloadStatus status) {
                        Log.e("==", "onNext thread=" + Thread.currentThread().getName());
                        Log.e("===", "=progress==" + status.getDownloadSize());
                        int progress = (int) ((double) status.getDownloadSize() / (double) status.getTotalSize() * 100d + 0.5d);
                        Log.e("===", "progress=" + progress);
                        mBuilder.setProgress(100, progress, false);
                        mBuilder.setContentText("正在下载" + progress + "%");
                        if (progress == 100) {
                            mBuilder.setContentText("下载完成" + progress + "%");
                            mManager.cancel(notification_id);
                        }
                        mManager.notify(notification_id, mBuilder.build());
                    }
                });

    }

    String savePath;

    private String createFile() {

        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {

            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        } else {
            savePath = getCacheDir().getAbsolutePath() + "/Download/";
        }

        return savePath;

    }

    private void startActivity(File targetFile) {
        //1. 创建 Intent 并设置 action
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判断是否是AndroidN以及更高的版本

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = DownLoadProvider.getUriForFile(getApplication(), "com.wxl.upapkdemo.fileprovider", targetFile);
            Log.d("===", "Uri Path " + contentUri.getPath());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(targetFile), "application/vnd.android.package-archive");

        }
        //4. 启动 activity
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("===", "service onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void downloadApk() {
        String baseUrl2 = baseUrl + "jingdong_53712.apk";
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(baseUrl2));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,"huge.jpg");//保存到公共图片文件夹
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "1234567.apk");//公共Download文件夹
        request.allowScanningByMediaScanner();//允许被扫描
        request.setVisibleInDownloadsUi(true);//通知栏一直显示
        request.setTitle("1234567.apk");
        request.setDescription(getPackageName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);//下载完成也会持续显示
        reference = mDownloadManager.enqueue(request);//得到下载文件的唯一id

        initNotificationReceiver();
    }

    /**
     * 接收DownloadManager发出的的广播
     */
    private void initNotificationReceiver() {
        //接收通知栏点击后发出的的广播
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        ClickReceiver clickReceiver =new ClickReceiver(reference,mDownloadManager);
        registerReceiver(clickReceiver, intentFilter);
        //接收下载完成后的广播
        IntentFilter intentFilter1 = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        CompleteReceiver completeReceiver = new CompleteReceiver(reference);
        registerReceiver(completeReceiver, intentFilter1);
    }

}
