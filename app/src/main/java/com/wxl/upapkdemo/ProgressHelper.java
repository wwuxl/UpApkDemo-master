package com.wxl.upapkdemo;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/11/29.
 */

public class ProgressHelper {

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder){
        if (builder==null) {
            builder=new OkHttpClient.Builder();
        }
         final DownloadCallBack callBack=new DownloadCallBack() {
            @Override
            public void onLoading(long progress, long totalLength, boolean done) {
                Log.e("===","progress=="+progress);
            }
        };
        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                Log.e("===","intercept");
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), callBack))
                        .build();

            }
        });

        return builder;
    }
}
