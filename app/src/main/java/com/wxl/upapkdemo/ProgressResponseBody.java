package com.wxl.upapkdemo;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by wxl on 2017/11/29.
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private DownloadCallBack mCallBack;
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, DownloadCallBack callBack) {

        mResponseBody = responseBody;
        mCallBack = callBack;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        Log.e("===","source");
        return bufferedSource;
    }

    private Source source(BufferedSource source) {
        return new ForwardingSource(source) {
            long progress=0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long byteRead = super.read(sink, byteCount);
                progress += byteRead != -1 ? byteRead : 0;
                Log.e("===","byteRead=="+byteRead);
                mCallBack.onLoading(progress,mResponseBody.contentLength(),byteRead==-1);
                return byteRead ;
            }
        };
    }
}
