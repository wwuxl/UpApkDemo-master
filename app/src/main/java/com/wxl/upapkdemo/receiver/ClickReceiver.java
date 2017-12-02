package com.wxl.upapkdemo.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.wxl.upapkdemo.DownloadActivity;

/**
 * Created by Administrator on 2017/12/2.
 * 点击时接收广播
 */

public class ClickReceiver extends BroadcastReceiver {

    private long mReference;
    private DownloadManager mManager;
    private Cursor mCursor;
    private Context mContext;

    public ClickReceiver(long reference,DownloadManager manager) {
        mReference = reference;
        mManager = manager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.e("===","点击了调用");
        initDownLoadUri();
        String extraId = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
        long[] references = intent.getLongArrayExtra(extraId);
        for (long refer : references) {

        }
    }
    /**
     * 查询文件下载地址和下载进度 * * @param re
     */
    private void initDownLoadUri() {
        //下载管理查询,得到文件下载地址
        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
        myDownloadQuery.setFilterById(mReference);
        mCursor = mManager.query(myDownloadQuery);

        if (mCursor.moveToFirst()) {
            int fileNameIdx = mCursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            int fileUriIdx = mCursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            //文件名称/storage/sdcard0/DCIM/huge-5.jpg
//            String fileName = mCursor.getString(fileNameIdx);
//            //文件地址   file:///storage/sdcard0/DCIM/huge-5.jpg
//            Log.e("===","fileName="+fileName);
//
//            String fileUri = mCursor.getString(fileUriIdx);        //得到当前状态
//            Log.e("===","fileUri="+fileUri);

            int status = mCursor.getInt(mCursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    //下载暂停
                    Log.e("===","暂停");
                    openDownloadInfo();
                    break;
                case DownloadManager.STATUS_PENDING:
                    //下载延迟
                    Log.e("===","下载延迟");
                    openDownloadInfo();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    //正在下载
                    Log.e("===","正在下载");
                    openDownloadInfo();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载成功
                    Log.e("===","下载成功");

                    break;
                case DownloadManager.STATUS_FAILED:
                    //下载失败
                    Log.e("===","下载失败");
                    openDownloadInfo();
                    break;
                default:

                    break;
            }
            //System.out.println("下载完成" + fileName + ": " + fileUri + "Uri.fromFile(new File(fileName)" + Uri.parse(fileUri));
        }
    }

    private void openDownloadInfo(){
//        String packageName = "com.android.providers.downloads";
//        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + packageName));
//        mContext.startActivity(intent);
//        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage("com.android.providers.downloads");
//        Log.e("===","intents="+intent);
//        if(intent!=null){
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        }
        Intent it = new Intent(mContext,DownloadActivity.class);
        it.setAction("com.wxl.download");
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("id",mReference);
        mContext.startActivity(it);

    }

}
