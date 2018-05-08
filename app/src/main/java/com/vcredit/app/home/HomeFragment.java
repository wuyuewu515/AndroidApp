package com.vcredit.app.home;

import android.view.View;

import com.vcredit.app.R;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.net.AbsRequestListener;
import com.vcredit.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

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


    @OnClick({R.id.btn_getMesage, R.id.btn_showMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_getMesage: {
                Map<String, String> map = new HashMap<>();
                map.put("password", "456");

                httpUtil.setNeedCache(true).doPostByString(HttpUtil.getServiceUrl(InterfaceConfig.HOME), map, new AbsRequestListener(activity) {
                    @Override
                    public void onSuccess(String result) {
                        TooltipUtils.showToastL(activity, result);


                    }
                });
            }
            break;
            case R.id.btn_showMessage: {
//
//                UrlCacheBean unique = urlCacheBeanDao.queryBuilder().where(UrlCacheBeanDao.Properties.UrlMd5.eq(url)).build().unique();
//                if (null != unique)
//                    TooltipUtils.showToastL(activity, unique.getUrlResult());
            }
            break;
        }
    }
}
