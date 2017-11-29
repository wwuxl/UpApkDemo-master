package com.wxl.upapkdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/11/29.
 */

public class FileUtil {

    /**
     * 写入文件
     * @param response
     * @param file
     * @param callBack
     */
    public static void writeFile2Disk(Response<ResponseBody> response, File file, DownloadCallBack callBack) {
        long currentLength = 0;
        long totalLength = response.body().contentLength();
        InputStream is = response.body().byteStream();
        OutputStream os=null;
        try {
            os = new FileOutputStream(file);
            int len = 0;
            byte[] bytes = new byte[512];
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
                currentLength += len;
                //实时回调

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

}
