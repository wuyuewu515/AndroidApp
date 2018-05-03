package com.vcredit.app.main.launch;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;

import com.vcredit.app.main.common.BaseLoginActivity;
import com.vcredit.global.AppConfig;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.global.SampleApplicationLike;
import com.vcredit.service.DownloadObserver;
import com.vcredit.service.DownloadService;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.JsonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.net.AbsRequestListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by shibenli on 2016/3/7.
 */
public class LaunchActivity extends BaseLoginActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                progress = (int) msg.obj;
                if (progressDialog != null) {
                    progressDialog.setProgress(progress);
                    if (progress == 100) {
                        progressDialog.dismiss();
                    }
                }
            }

        }
    };
    private boolean flag = false;
    private Intent mIntent;
    //是否要更新app
    private boolean versionFlag = false;
    /*** 系统枚举版本号*/
    private int sysEnumsVer;
    /*** 当前App版本号*/
    private int versionNo;

    private boolean isDownloadFlag = false;
    private String downloadUrl;
    private boolean isDownloaded = false;
    // 启动时间
    private long startSeconds;
    // 结束时间
    private long endSeconds;
    // 最小延迟毫秒数
    private final int minDelaySeconds = 2000;
    private int progress;
    private ProgressDialog progressDialog;
    private DownLoadIdBroadCast broadcast;
    private DownloadObserver downloadObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
        instantiation();
        initData();

     //   openHomePage();
    }

    //注册动态广播，用于接收当前下载的apk的downloadId
    private void register() {
        broadcast = new DownLoadIdBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_DOWNLOAD);
        registerReceiver(broadcast, filter);
    }

    private void instantiation() {
        mIntent = new Intent();
        httpUtil.setIsOpenProgressbar(false);
    }

    protected void initData() {
        startSeconds = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!HttpUtil.checkNetState(this)) {
            TooltipUtils.showDialog(this, "当前无网络，是否设置？", "", positiveListener, negativeListener, "设置", "退出", false, false);
            flag = true;
        }
        if (!flag && !versionFlag) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                checkUpdate();
                }
            }, 200);
        }
    }

    // dialog确定按钮监听事件
    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            flag = false;
        }
    };

    // dialog取消按钮监听事件
    DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            LaunchActivity.this.finish();
            SampleApplicationLike.getInstance().exit(LaunchActivity.this);
        }
    };


    private void finishLaunch() {
        if (versionFlag && !isDownloadFlag) {
            endSeconds = System.currentTimeMillis();
            // 　判断如果时间间隔小于2秒，最多2秒延时，否则不延时
            long seconds = 0;
            if ((endSeconds - startSeconds) < minDelaySeconds) {
                seconds = minDelaySeconds - (endSeconds - startSeconds);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!instance.getValue(SharedPreUtils.APP_PACKAGE_VERSION, "").equals(versionNo + "")) {
                        IntroduceActivity.launch(LaunchActivity.this);
                        instance.saveValue(SharedPreUtils.APP_PACKAGE_VERSION, versionNo + "");
                        finish();
                    } else {
                        //不是自动登录
                        if (!mAutoLogin) {
                            openHomePage();
                        } else {
                            //如果是自动登录
                            login();
                        }
                    }
                }
            }, seconds);
        }
    }


    /**
     * 检测app是否有版本更新，如有则弹窗提示用户
     */
    private boolean isNewVersion(String loadUrl) {

        if (!TextUtils.isEmpty(loadUrl)) {
            StringBuilder filePath = new StringBuilder();
            filePath.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
            filePath.append(File.separator);
            filePath.append(AppConfig.APPNAME);
            //     filePath.append("_v");
            //     filePath.append(versionsUpdateInfo.getVersionName());
            filePath.append(".apk");

            File file = new File(filePath.toString());
            if (file.exists()) {
                if (isDownloading(loadUrl)) {
                    String id = instance.getValue(SharedPreUtils.DOWNLOADID, "-1");
                    if (Long.parseLong(id) != -1) {
                        displayProgressDialog();
                        downloadObserver = new DownloadObserver(handler, LaunchActivity.this, Long.parseLong(id));
                        getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, downloadObserver);
                    }
                    isDownloadFlag = true;
                    return true;
                }
                downloadUrl = filePath.toString();
                isDownloaded = true;
            } else {
                downloadUrl = loadUrl;
                isDownloaded = false;
            }
            Intent intent = new Intent(this, UpdateViewActivity.class);
            intent.putExtra("downloadUrl", downloadUrl);
            intent.putExtra("verNo", "1005");
            intent.putExtra("isDownloaded", isDownloaded);
            intent.putExtra("appSize", 10);
            intent.putExtra("updateInfo", "这个是更新信息展示");
            startActivityForResult(intent, 0);
            overridePendingTransition(0, 0);
            return false;
        }
        return true;
    }

    /**
     * 是否正在下载
     *
     * @param url
     * @return
     */
    private boolean isDownloading(String url) {
        Cursor c = ((DownloadManager) getSystemService(DOWNLOAD_SERVICE))
                .query(new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING));
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


    /**
     * 检查更新
     */
    private void checkUpdate() {

        //由于部分手机在安装下载包的时候不会自动删除安装包，所以在使用的会存在需要更新的时候一直弹窗提示
        //已经下载了app需要安装，所以需要服务器端回传最新包的版本号，在updateactivity中的 58行代码处


        Map<String, String> pramas = new HashMap<>();
        pramas.put("platform_type", "0");
        pramas.put("version", app.getVersionName());
        pramas.put("channel_code", SampleApplicationLike.channel);
        httpUtil.doPostByString(HttpUtil.getServiceUrl(InterfaceConfig.CHECK_UPDATE), pramas, new AbsRequestListener(mActivity) {
            @Override
            public void onSuccess(String result) {

                String downLoad_url = JsonUtils.getJSONObjectKeyVal(result, "download_url");
                isNewVersion(downLoad_url);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            versionFlag = true;
            isDownloadFlag = data.getBooleanExtra("isFinish", true);
            if (!isDownloaded) {
                displayProgressDialog();
                //TooltipUtils.showDialog(this, "正在下载新版本，请稍后...", null, null, null, null, false);
            }
            finishLaunch();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        SampleApplicationLike.getInstance().exitBy2Click(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (null != broadcast) {
            unregisterReceiver(broadcast);
        }
        if (null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
        if (downloadObserver != null) {
            getContentResolver().unregisterContentObserver(downloadObserver);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    /**
     * 用于接收下载的id
     */
    public class DownLoadIdBroadCast extends BroadcastReceiver {
        public DownLoadIdBroadCast() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            long downLoadId = intent.getLongExtra("id", -1);
            if (downLoadId != -1) {
                downloadObserver = new DownloadObserver(handler, LaunchActivity.this, downLoadId);
                getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, downloadObserver);
            }
        }
    }

    /**
     * 进度对话框
     */
    private void displayProgressDialog() {
        // 创建ProgressDialog对象
        progressDialog = new ProgressDialog(this);
        // 设置进度条风格，风格为长形
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle("下载提示");
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage("当前下载进度:");
        // 设置ProgressDialog 标题图标
        //progressDialog.setIcon(R.drawable.a);
        // 设置ProgressDialog 进度条进度
        progressDialog.setProgress(100);
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        // 让ProgressDialog显示
        progressDialog.show();

    }

}
