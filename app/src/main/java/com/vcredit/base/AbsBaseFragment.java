package com.vcredit.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vcredit.app.main.login.LoginActivity;
import com.vcredit.global.App;
import com.vcredit.global.OnClickFragmentListenner;
import com.vcredit.global.Updateable;
import com.vcredit.utils.CommonUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by shibenli on 2016/5/30.
 */
public abstract class AbsBaseFragment extends BaseFragment implements View.OnClickListener, Updateable{
    final protected int REQUESTCODE_APPLYCREDIT = 0x0040;

    protected OnClickFragmentListenner fragmentListenner;

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnClickFragmentListenner){
            fragmentListenner = (OnClickFragmentListenner) fragment;
        }else if (activity instanceof OnClickFragmentListenner){
            fragmentListenner = (OnClickFragmentListenner) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(layout(), container, false);
        } else {
            removeViewFromParent(mainView);
        }
        unbinder = ButterKnife.bind(this, mainView);
        initView();
        return mainView;
    }

    @LayoutRes
    protected abstract int layout();

    /**
     * 初始化界面方法
     */
    private void initView() {
        // step 1 初始化标题栏（如果代码较多，可创建一个新方法。如标题栏内容需根据入口页定置请在step 2完成后再初始化标题栏）
        initTitleBar();

        // step 2 初始化数据（如果该页需从其他界面或接口获得数据请在此方法中获取，无需要则无需该方法）
        initData();
        // step 3 数据绑定（获取数据后在此方法中对控件进行赋值）
        dataBind();
    }

    /**
     * 初始化标题栏
     */
    protected void initTitleBar(){}

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 数据绑定
     */
    protected void dataBind(){}

    protected void refreshView(){

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    protected void requestLogin(int requestCode){
        requestLogin(null, requestCode);
    }

    protected void requestLogin(Class clazz, int requestCode){
        if (null != clazz && App.isLogined) {
  //          AddMainActivity.launch(activity, clazz);
        }else {
            startActivityForResult(new Intent(activity, LoginActivity.class), requestCode);
        }
    }

    @Override
    public void onClick(View view) {
        CommonUtils.LOG_D(getClass(), view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void updateFragmentsStatus() {

    }
}