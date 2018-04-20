package com.vcredit.app.discover;

import com.vcredit.app.R;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.view.TitleBar;

import butterknife.BindView;

/**
 * 作者: 伍跃武
 * 时间： 2018/4/20
 * 描述：账单
 */

public class BillFragment extends AbsBaseFragment {
    @BindView(R.id.title_bill)
    TitleBar titleBill;

    @Override
    protected int layout() {
        return R.layout.main_bill_fragment;
    }

    @Override
    protected void initData() {
        titleBill.setLeftIcon(0);
    }

    @Override
    protected void dataBind() {

    }


}
