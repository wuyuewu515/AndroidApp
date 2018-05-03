package com.vcredit.global;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.analytics.MobclickAgent;
import com.vcredit.app.BuildConfig;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.TooltipUtils;

import org.apache.commons.lang3a.StringUtils;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 伍跃武
 * 时间： 2018/5/3
 * 描述：
 */
public class SampleApplicationLike extends DefaultApplicationLike {
    public static final String TAG = "Tinker.SampleApplicationLike";

    /**
     * 注销队列
     */
    private static Stack<Activity> activityStack = new Stack<>();

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activity != null && !activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    private static SampleApplicationLike sampleApplicationLike;

    public static SampleApplicationLike getInstance() {
        return sampleApplicationLike;
    }

    /**
     * app退出
     */
    public void exit(Context context) {
        try {
            finishAllActivity();
            MobclickAgent.onKillProcess(appInstance);
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 双击退出标志位
    private static Boolean isExit = false;


    /**
     * 双击退出app
     *
     * @param activity
     */
    public boolean exitBy2Click(Activity activity) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            TooltipUtils.showToastS(activity, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            return false;
        } else {
            //退出app
            exit(activity);
            return true;
        }
    }

    public static boolean isLogined = false;
    public static String channel = "unknown";
    /**
     * 使用的字体图标
     */
    public static Typeface iconFont;

    public SampleApplicationLike(Application application, int tinkerFlags,
                                 boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                                 long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    /**
     * 获得Application对象
     */
    private static Application appInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sampleApplicationLike = this;
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(getApplication(), "f056b0e9ac", InterfaceConfig.ISDEBUG);

        appInstance = getApplication();

        iconFont = Typeface.createFromAsset(appInstance.getAssets(), AppConfig.ICONFONT_PATH);

        MobclickAgent.setDebugMode(AppConfig.DEBUG);

        ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(appInstance.getApplicationContext());
        if (channelInfo != null) {
            String apkChannel = channelInfo.getChannel();
            channel = StringUtils.isEmpty(apkChannel) ? "dev" : apkChannel;
        } else {
            channel = "dev";
        }

        // 之后就可以使用了，比如友盟可以这样设置
        //     AnalyticsConfig.setChannel(channel);

        if (AppConfig.DEBUG) {
            LeakCanary.install(appInstance);// 内存泄露检测工具
        }
    }


    /**
     * 获得当前App版本号
     */
    public int getVersionCode() {
        return CommonUtils.getPackageInfo(appInstance).versionCode;
    }

    /**
     * 获得当前App版本号
     */
    public String getVersionName() {
        return CommonUtils.getPackageInfo(appInstance).versionName;
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

}