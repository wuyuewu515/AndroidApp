package com.vcredit.global;


import android.os.Environment;

import com.vcredit.app.BuildConfig;

/**
 * Created by wangzhengji on 2016/1/26.
 */
public class AppConfig {
    /** 应用是否开启debug模式 */
    public static final boolean DEBUG = BuildConfig.DEBUG;
    /** 应用内部名称 */
    public static final String APPNAME = "myApp";

    /** app文件存放路径 */
    public static final String FILEPATH = Environment.DIRECTORY_DOWNLOADS+"app";
    /** app图片存放路径 */
    public static final String IMAGEPATH = "/app/image/";

    /** 字体设置 */
    public static final String ICONFONT_PATH = "iconfont/iconfont.ttf";
    /**
     * 服务端App版本号
     */
    public static int netVer = 0;
}
