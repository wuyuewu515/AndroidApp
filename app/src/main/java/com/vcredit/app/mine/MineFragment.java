package com.vcredit.app.mine;

import com.vcredit.app.R;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.view.TitleBar;

import butterknife.BindView;

/**
 * 作者: 伍跃武
 * 时间： 2018/4/20
 * 描述：我的首页
 */

public class MineFragment extends AbsBaseFragment {
    @BindView(R.id.title_mine)
    TitleBar titleMine;

    @Override
    protected int layout() {
        return R.layout.main_mine_fragment;
    }

    @Override
    protected void initData() {
        titleMine.setLeftIcon(0);
    }

    @Override
    protected void dataBind() {

    }


}
