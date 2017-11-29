package com.wxl.upapkdemo;

/**
 * Created by Administrator on 2017/11/29.
 */

public interface DownloadCallBack {
    void onLoading(long progress, long totalLength,boolean done);
}
