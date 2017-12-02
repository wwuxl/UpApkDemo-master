package com.wxl.upapkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private String baseUrl = "http://192.168.120.26:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




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
