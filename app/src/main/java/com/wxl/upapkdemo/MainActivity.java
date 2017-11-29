package com.wxl.upapkdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zlc.season.rxdownload.DownloadStatus;
import zlc.season.rxdownload.RxDownload;

public class MainActivity extends AppCompatActivity {
    private String baseUrl = "http://192.168.23.1:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent=new Intent(this,DownloadService.class);
//
//        startService(intent);

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

    }

    String savePath;
    private String createFile() {

        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {

            savePath =Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        } else {
            savePath = getCacheDir().getAbsolutePath() + "/Download/";
        }

        Log.d("===", "savePath " + savePath);

        return savePath;

    }

    public void onClick(View view) {
        baseUrl = baseUrl + "com.sibu.socialelectronicbusiness-release-1.1.0.apk";
        final Subscription subscription = RxDownload.getInstance()
                .defaultSavePath(createFile())
                .download(baseUrl, "123456789.apk", null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DownloadStatus>() {
                    @Override
                    public void onCompleted() {
                        Log.e("===", "onCompleted "+Thread.currentThread().getName());
                        //打开安装界面
                        File file=new File(savePath+"123456789.apk");
                        Log.e("===","exists="+file.exists());
                        Log.e("===","length="+file.length());
                        startActivity(file);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("===", "onError=="+e.getMessage());
                    }

                    @Override
                    public void onNext(final DownloadStatus status) {
                       // Log.e("==", "thread=" + Thread.currentThread().getName());
                        //Log.e("===", "=progress==" + status.getDownloadSize());
                    }
                });


    }

    private void startActivity(File targetFile) {
        //1. 创建 Intent 并设置 action
        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判断是否是AndroidN以及更高的版本

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
            Uri contentUri = DownLoadProvider.getUriForFile(getApplication(),"com.wxl.upapkdemo.fileprovider",targetFile);
            Log.d("===", "Uri Path " + contentUri.getPath());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");

        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(targetFile),"application/vnd.android.package-archive");

        }
        //4. 启动 activity
        startActivity(intent);
    }
}
