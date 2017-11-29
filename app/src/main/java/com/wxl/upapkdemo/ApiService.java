package com.wxl.upapkdemo;

import android.icu.util.VersionInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by wxl on 2017/11/17.
 */

public interface ApiService {

    @Streaming //大文件时要加不然会OOM
    @GET()
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @GET("{fileUrl}")
    Call<VersionInfo> getVersionCode(@Path("fileUrl") String fileUrl);


}
