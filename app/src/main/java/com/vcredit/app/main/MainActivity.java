package com.vcredit.app.main;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vcredit.app.R;
import com.vcredit.app.credit.CreditFragment;
import com.vcredit.app.discover.BillFragment;
import com.vcredit.app.entities.UserData;
import com.vcredit.app.home.HomeFragment;
import com.vcredit.app.main.login.LoginActivity;
import com.vcredit.app.mine.MineFragment;
import com.vcredit.base.AbsBaseActivity;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.global.OnTabItemClickListenner;
import com.vcredit.global.SampleApplicationLike;
import com.vcredit.global.Updateable;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.view.NavigationItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by shibenli on 2016/7/6.
 */
public class MainActivity extends AbsBaseActivity implements Updateable {


    @BindView(R.id.main_tabBill)
    NavigationItem mainTabBill;

    @BindView(R.id.main_tabCredit)
    NavigationItem mainTabCredit;

    @BindView(R.id.main_tabDiscover)
    NavigationItem mainTabDiscover;

    @BindView(R.id.main_tabMine)
    NavigationItem mainTabMine;

    @BindView(R.id.rl_guide)
    RelativeLayout guideLayout;

    @BindView(R.id.iv_guide_part1)
    ImageView ivGuidePartOne;

    @BindView(R.id.iv_guide_part2)
    ImageView ivGuidePartTwo;

    private SharedPreUtils instance;
    private int versionNo;

    @Override
    protected int layout() {
        return R.layout.main_navigation_activity;
    }

    @Override
    protected void initData() {
        instantiation();


    }


    /**
     * 初始化引导
     */
    private void initGuide() {
        instance = SharedPreUtils.getInstance(this);
        versionNo = CommonUtils.getAppVersionCode(this);
        if (!instance.getValue(SharedPreUtils.GUIDE_VERSION, "").equals(versionNo + "")) {
            guideLayout.setVisibility(View.VISIBLE);
            guideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                //获取StatusBar高度，使引导图片不覆盖掉StatusBar
                int statusBarHeight = CommonUtils.getStatusBarHeight(mActivity);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) guideLayout.getLayoutParams();
                lp.setMargins(0, statusBarHeight, 0, 0);
            }
            guideLayout.setBackgroundResource(R.mipmap.guide_one);
            ivGuidePartOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivGuidePartOne.setVisibility(View.GONE);
                    ivGuidePartTwo.setVisibility(View.GONE);
                    guideLayout.setBackgroundResource(R.mipmap.guide_two);
                    guideLayout.setOnClickListener(closeGuideListener);
                }
            });
            ivGuidePartTwo.setOnClickListener(closeGuideListener);
        }
    }

    private View.OnClickListener closeGuideListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            guideLayout.setVisibility(View.GONE);
            instance.saveValue(SharedPreUtils.GUIDE_VERSION, versionNo + "");
        }
    };


    /**
     * tab页初始化
     */
    protected void instantiation() {
        // tab按钮 与 关联模块
        tabMap = new HashMap<>();
        //账单
        tabMap.put(R.id.main_tabBill, new Submodule(mainTabBill,
                new BillFragment()));
        // 信用
        tabMap.put(R.id.main_tabCredit, new Submodule(mainTabCredit,
                new CreditFragment()));
        // 首页
        tabMap.put(R.id.main_tabDiscover, new Submodule(mainTabDiscover,
                new HomeFragment()));
        //我的
        tabMap.put(R.id.main_tabMine, new Submodule(mainTabMine,
                new MineFragment()));


        updateFromGOTO(getIntent());
    }

    /**
     * 处理GOTO参数
     */
    private void updateFromGOTO(Intent intent) {
        if (intent != null) {
            int mGoto = intent.getIntExtra("GOTO", 0);
            int mGotoExt = intent.getIntExtra("GOTO_EXT", -1);
            if (mGoto == 1) {//手机认证的返回需要，跳转到信用首页
                toggleFragment(tabMap.get(R.id.main_tabCredit));
                //清除标志
                intent.removeExtra("GOTO");
            } else if (mGoto == 2) {//账单
                toggleFragment(tabMap.get(R.id.main_tabBill));
                //清除标志
                intent.removeExtra("GOTO");
            } else if (mGoto == 3) {//我的
                toggleFragment(tabMap.get(R.id.main_tabMine));
                //清除标志
                intent.removeExtra("GOTO");

                if (mGotoExt == 1) {//跳转到登录
                    LoginActivity.launch(this, LoginActivity.class);
                }
            } else { // 向fragment容器中添加首页
                toggleHomeFragment(false);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void toggleFragment(Submodule submodule) {
        toggleFragment(submodule, true);
    }

    /**
     * tab切换
     */
    private void toggleFragment(Submodule submodule, boolean withClick) {
        if (submodule == null) {
            return;
        }

        if (submodule.fragmentView != null) {
            if (curSubmodule == submodule) {
                if (withClick && submodule.fragmentView instanceof OnTabItemClickListenner) {
                    ((OnTabItemClickListenner) submodule.fragmentView).onTabItemClick();
                    return;
                }
            }

            replaceFragment(R.id.main_fragment_container, submodule.fragmentView);
        }

        // Tab活动状态切换
        if (curSubmodule != null)
            curSubmodule.setTabStatus(false);
        submodule.setTabStatus(true);

        // 更新选中项
        curSubmodule = submodule;
    }

    /**
     * 切换页面到HomeFragment
     */
    private void toggleHomeFragment(boolean withClick) {
        Submodule submodule = tabMap.get(R.id.main_tabDiscover);
        toggleFragment(submodule, withClick);
    }

    @OnClick({R.id.main_tabBill, R.id.main_tabCredit, R.id.main_tabMine, R.id.main_tabDiscover})
    public void onClick(View view) {
        int tabId = view.getId();
        switch (tabId) {
            case R.id.main_tabBill: {//账单
                Submodule submodule = tabMap.get(tabId);
                toggleFragment(submodule);
            }
            break;
            case R.id.main_tabCredit: {//信用
                Submodule submodule = tabMap.get(tabId);
                toggleFragment(submodule);
            }
            break;
            case R.id.main_tabDiscover: {//发现
                Submodule submodule = tabMap.get(tabId);
                toggleFragment(submodule);
            }
            break;
            case R.id.main_tabMine: {//我的
                Submodule submodule = tabMap.get(tabId);
                toggleFragment(submodule);
            }
            break;
        }
    }

    /**
     * 监听UserData的修改
     *
     * @param userData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateByUserInfo(@NonNull UserData userData) {
        //刷新我的模块的红点
        Submodule submodule = tabMap.get(R.id.main_tabMine);
        // submodule.title.setNewCount(userData.getUserInfo().getMessageQty());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean clickPush = intent.getBooleanExtra("click_push", false);
            boolean returnHome = intent.getBooleanExtra("return_home", false);
            if (clickPush) {
                //           doPushResult(JPushHelper.getInstance(this).getPushExtras());
            } else if (returnHome) {
                toggleFragment(tabMap.get(R.id.main_tabBill));
            } else {
                updateFromGOTO(intent);
            }
        }
    }

    private void doPushResult(String pushExtras) {
        if (null == pushExtras) {
            return;
        }

        String[] split = pushExtras.split("]", 2);
        if (split.length < 1) {
            return;
        }

    }

    @Override
    public void onBackPressed() {
        //App.getInstance().exitBy2Click(this);
        SampleApplicationLike.getInstance().exitBy2Click(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateFragmentsStatus() {
        for (Submodule submodule : tabMap.values()) {
            if (submodule.fragmentView != null)
                submodule.fragmentView.updateFragmentsStatus();
        }
    }

    /**
     * 子模块
     */
    private class Submodule implements Serializable {
        /**
         * 构造函数
         */
        public Submodule(NavigationItem title, AbsBaseFragment fragmentView) {
            this.title = title;
            this.fragmentView = fragmentView;
        }

        private NavigationItem title;

        // tab内容
        public AbsBaseFragment fragmentView;

        // 设置tab图片
        public Submodule setTabStatus(boolean isOpen) {
            title.setSelected(isOpen);
            return this;
        }

        public Submodule setTabCount(int cout) {
            title.setNewCount(cout);
            return this;
        }
    }

    /**
     * 将tab按钮与子模块建立关联
     */
    private HashMap<Integer, Submodule> tabMap;

    public Fragment getFragment(int id) {
        return tabMap.get(id).fragmentView;
    }

    /**
     * 当前模块
     */
    private Submodule curSubmodule;
}
