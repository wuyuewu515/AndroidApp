package com.vcredit.app.home;

import com.vcredit.app.R;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.view.TitleBar;

import butterknife.BindView;

/**
 * 作者: 伍跃武
 * 时间： 2018/4/20
 * 描述：首页fragmentMent
 */

public class HomeFragment extends AbsBaseFragment {
    @BindView(R.id.title_home)
    TitleBar titleHome;

    @Override
    protected int layout() {
        return R.layout.main_start_fragment;
    }

    @Override
    protected void initData() {
        titleHome.setLeftIcon(0);
    }

    @Override
    protected void dataBind() {

    }


}
