package com.vcredit.app.credit;

import com.vcredit.app.R;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.view.TitleBar;

import butterknife.BindView;

/**
 * 作者: 伍跃武
 * 时间： 2018/4/20
 * 描述：信用首页
 */

public class CreditFragment extends AbsBaseFragment {
    @BindView(R.id.title_credit)
    TitleBar titleCredit;

    @Override
    protected int layout() {
        return R.layout.main_credit_fragment;
    }

    @Override
    protected void initData() {
        titleCredit.setLeftIcon(0);
    }

    @Override
    protected void dataBind() {

    }


}
