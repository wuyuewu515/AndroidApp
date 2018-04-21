package com.vcredit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.vcredit.service.DownloadService;

import java.io.File;

/**
 * Created by wangzhengji on 2016/3/9.
 */
public class DownloadUtils {

    /**
     * 判断存储空间大小是否满足条件
     *
     * @param sizeByte
     * @return
     */
    public static boolean isAvaiableSpace(float sizeByte) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            float availableSpare = blocks * blockSize;
            if (availableSpare > (sizeByte + 1024 * 1024)) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 开始安装apk文件
     *
     * @param context
     * @param localFilePath
     */
    public static void installApkByGuide(Context context, String localFilePath) {
        //通过指定严格模式来实现安装操作
//        if (Build.VERSION.SDK_INT >23) {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            builder.detectFileUriExposure();
//        }


        //https://blog.csdn.net/github_2011/article/details/74297460
        //https://blog.csdn.net/yulianlin/article/details/52775160
        File file = new File(localFilePath);
        String path = file.getAbsolutePath();
        File dataDirectory = Environment.getDataDirectory();

        Log.i("TAG", "path路径是" + path);
        Log.i("TAG", "dataDirectory路径是" + dataDirectory);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) { // 安卓7.0以后采取新的更新模式

            // 参数2 清单文件中provider节点里面的authorities ; 参数3  共享的文件,即apk包的file类
            Uri apkUri = FileProvider.getUriForFile(context, "com.vcredit.app.fileprovider", file);
            //对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");


        } else { //7.0以下依然按照原来的方法安装apk
            intent.setDataAndType(Uri.parse("file://" + localFilePath),
                    "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 下载
     *
     * @param url      下载链接
     * @param context
     * @param fileName 文件名
     * @param size     文件大小（b）
     */
    public static void startDownload(String url, Context context,
                                     String fileName, Float size) {
        if (CommonUtils.detectSdcardIsExist()) {
            if (isAvaiableSpace(size)) {
                Intent intent = new Intent(context, DownloadService.class);
                intent.putExtra("downloadUrl", url);
                intent.putExtra("fileName", fileName);
                context.startService(intent);
            } else {
                TooltipUtils.showToastS((Activity) context, "存储卡空间不足");
            }
        } else {
            TooltipUtils.showToastS((Activity) context, "请检查存储卡是否安装");
        }
    }
}
