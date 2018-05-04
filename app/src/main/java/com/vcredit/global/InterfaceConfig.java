package com.vcredit.global;


import com.vcredit.app.BuildConfig;

/**
 * Created by wangzhengji on 2016/3/22.
 */
public class InterfaceConfig {

    /**
     * 请求超时时间
     */
    public static final int SOCKET_TIMEOUT = 60 * 1000;
    /**
     * 最大重新请求次数
     */
    public static final int MAX_RETRIES = 0;
    /**
     * 重新请求权重
     */
    public static final float BACK_OFF = 1.0f;
    /**
     * 字符编码
     */
    public static final String ENCODEING = "UTF-8";


    /**
     * 请求的域名
     */
    public static final String BASE_URL = BuildConfig.BASE_URL;

    /**
     * 当前是否为调试模式
     */
    public static final boolean ISDEBUG = BuildConfig.IS_DEBUG;

    /**
     * 带版本的请求的域名
     */
    public static final String BASE_URL_V = BASE_URL + BuildConfig.BASE_SERVER;


    /** web接口地址 */

    /**
     * 用户登陆
     **/
    public static final String LOGIN = "?app_act=user/user_login";

    /**
     * 获取首页数据
     **/
    public static final String HOME = "?app_act=index/home_page";

    /**
     * 检查更新
     */
    public static final String CHECK_UPDATE = "?app_act=index/check_update";


    /**
     * 获得系统枚举
     */
    public static final String GETAPPENUMS = "version/getAppEnums";

}
