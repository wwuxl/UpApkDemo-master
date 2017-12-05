package com.wxl.upapkdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private String baseUrl = "http://192.168.120.26:8080/";
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_PRE = 1; //上一首
    private static final int NOTIFICATION_NEXT = 2; //下一首
    private static final int NOTIFICATION_OPEN = 3; //打开歌曲
    private RemoteViews mRemoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //添加OkHttp
//        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
//
//        ApiService service = new Retrofit.Builder()
//                .client(builder.build())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(baseUrl)
//                .build().create(ApiService.class);
//
//
//        Call<ResponseBody> responseBodyCall = service.downloadFile("com.sibu.socialelectronicbusiness-release-1.1.0.apk");
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.e("===", "onResponse" + response.body().contentLength());
////                try {
////                    InputStream is = response.body().byteStream();
////                    File savePath = new File(Environment.getExternalStorageDirectory(), "12345.apk");
////                    FileOutputStream fos = new FileOutputStream(savePath);
////                    BufferedInputStream bis = new BufferedInputStream(is);
////                    byte[] buffer = new byte[1024];
////                    int len;
////                    while ((len = bis.read(buffer)) != -1) {
////                        fos.write(buffer, 0, len);
////                        fos.flush();
////                    }
////                    fos.close();
////                    bis.close();
////                    is.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("===", "onFailure " + t.getMessage());
//            }
//        });
        //createCustomNotification();
    }
    private void createCustomNotification() {
        Intent intent = new Intent(this, DownloadActivity.class);
        //如果第二次获取并且请求码相同,如果原来已解决创建了这个PendingIntent,则复用这个类,并更新intent
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 3, intent, flag);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("当前正在播放..")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("歌曲名")
                .setContentText("歌手") //以上的设置是在为了兼容3.0之前的版本
                .setContentIntent(contentIntent)
                .setContent(getRemoteView()); //自定义通知栏view的api是在3.0之后生效
        Notification notification = builder.build();
        //打开通知
        mNotificationManager.notify(111, notification);
    }
    /**
     * 创建RemoteViews,3.0之后版本使用
     *
     * @return
     */
    public RemoteViews getRemoteView() {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        mRemoteViews.setTextViewText(R.id.tv_content_title, "歌曲名1");
        mRemoteViews.setTextViewText(R.id.tv_content_text, "歌手1");
        mRemoteViews.setProgressBar(R.id.progressBar,100,20,false);
        //打开上一首
        //mRemoteViews.setOnClickPendingIntent(R.id.btn_pre, getClickPendingIntent(NOTIFICATION_PRE));
        //打开下一首
        mRemoteViews.setOnClickPendingIntent(R.id.btn_next, getClickPendingIntent(NOTIFICATION_NEXT));
        //点击整体布局时,打开播放器
        mRemoteViews.setOnClickPendingIntent(R.id.ll_root, getClickPendingIntent(NOTIFICATION_OPEN));
        return mRemoteViews;
    }

    /**
     * 获取点击自定义通知栏上面的按钮或者布局时的延迟意图
     *
     * @param what 要执行的指令
     * @return
     */
    public PendingIntent getClickPendingIntent(int what) {
        Intent intent = new Intent(this, DownloadActivity.class);
        intent.putExtra("cmd", what);
        int flag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent clickIntent = PendingIntent.getActivity(this, what, intent, flag );


        return clickIntent;
    }



    public void onClick(View view) {
        String[] perms=new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
        if (EasyPermissions.hasPermissions(this,perms)) {

            Intent intent=new Intent(this,DownloadService.class);
            startService(intent);
        }else{
            EasyPermissions.requestPermissions(this, "下载文件需要的权限",
                    100, perms);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(requestCode==100){
            Intent intent=new Intent(this,DownloadService.class);

            startService(intent);
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
