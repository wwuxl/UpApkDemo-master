package com.wxl.upapkdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private String baseUrl = "http://192.168.120.26:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(this,DownloadService.class);

        startService(intent);

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



    }


}
