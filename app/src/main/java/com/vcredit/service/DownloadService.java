package com.vcredit.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import com.vcredit.app.BuildConfig;
import com.vcredit.global.SampleApplicationLike;
import com.vcredit.utils.DownloadUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.VerifyUtils;

/**
 * 下载服务
 *
 * @author zhuofeng
 */
public class DownloadService extends Service {

    /**
     * 接收当前下载的apk的downloadId 的ACTION
     */
    public final static String ACTION_DOWNLOAD = BuildConfig.APPLICATION_ID + "receiver";

    private DownloadManager downloadManager = null;
    private OnCompleteReceiver onComplete;
    private long downloadId;
    private String fileName;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        onComplete = new OnCompleteReceiver();
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

    }

    @Override
    public void onDestroy() {
        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getStringExtra("downloadUrl");

            download(url);
        }
        long lastDownLoadId = Long.valueOf(SharedPreUtils.getInstance(this).getValue(SharedPreUtils.DOWNLOADID, -1 + ""));
        if (-1 != lastDownLoadId) {
            //如果这个id  不为-1 那么说明还在继续下载
            downloadId = lastDownLoadId;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(String url) {

        if (isDownloading(url)) {
            return;
        }
        //校验url是否正确
        if (!VerifyUtils.isUrl(url)) {
            TooltipUtils.showToastS(getApplicationContext(), "下载地址不正确");
            return;
        }

        //获得系统下载器
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //设置下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置下载文件的类型
        request.setMimeType("application/vnd.android.package-archive");
        //设置下载存放的文件夹和文件名字
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "myApp.apk");
        //设置下载时或者下载完成时，通知栏是否显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        request.setVisibleInDownloadsUi(true);
        request.setTitle("下载新版本");
        //执行下载，并返回任务唯一id
        downloadId = downloadManager.enqueue(request);

        //把当前下载id发送给广播接收者
        Intent intent = new Intent();
        intent.putExtra("id", downloadId);
        intent.setAction(ACTION_DOWNLOAD);
        sendBroadcast(intent);
    }

    /**
     * 下载通知点击的监听器
     */
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            showDownloadManagerView();
        }
    };

    /**
     * 下载完成的接收器
     */
    private class OnCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            // 通过intent获取发广播的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(id));
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (id == downloadId && c.getInt(columnIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                    String localFilePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    SharedPreUtils.getInstance(DownloadService.this).saveValue(SharedPreUtils.DOWNLOADID, "-1");
                    DownloadUtils.installApkByGuide(DownloadService.this, localFilePath);
                    stopSelf();
                }
            }
            c.close();
            //下载完成  退出app
            SampleApplicationLike.getInstance().exit(getApplicationContext());
            // SharedPreUtils.getInstance(DownloadService.this).saveValue(SharedPreUtils.DOWNLOADID,"-1");
        }
    }

    /**
     * 跳转到系统下载界面
     */
    private void showDownloadManagerView() {
        Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 判断传入的url是否正在下载
     */
    private boolean isDownloading(String url) {
        Cursor c = downloadManager.query(new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING));
        if (c.moveToFirst()) {
            String tmpURI = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
            if (tmpURI.equals(url)) {
                if (c != null && !c.isClosed()) {
                    c.close();
                }
                return true;
            }
        }
        if (c != null && !c.isClosed()) {
            c.close();
        }
        return false;
    }

}
