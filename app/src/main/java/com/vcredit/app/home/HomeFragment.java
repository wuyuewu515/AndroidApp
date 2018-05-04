package com.vcredit.app.home;

import android.view.View;

import com.vcredit.app.R;
import com.vcredit.app.entities.UrlCacheBean;
import com.vcredit.app.entities.UrlCacheBeanDao;
import com.vcredit.base.AbsBaseFragment;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.EncryptUtils;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.net.AbsRequestListener;
import com.vcredit.view.TitleBar;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;

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


    private UrlCacheBeanDao urlCacheBeanDao;
    private String url;

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
        urlCacheBeanDao = app.getDaoSession().getUrlCacheBeanDao();
    }


    @OnClick({R.id.btn_getMesage, R.id.btn_showMessage})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_getMesage: {
                Map<String, String> map = new HashMap<>();
                map.put("password", "should");

                httpUtil.doPostByString(HttpUtil.getServiceUrl(InterfaceConfig.HOME), map, new AbsRequestListener(activity) {
                    @Override
                    public void onSuccess(String result) {
                        TooltipUtils.showToastL(activity, result);
                        url = EncryptUtils.md5(HttpUtil.getServiceUrl(InterfaceConfig.HOME));
                        UrlCacheBean bean = new UrlCacheBean(url, result);
                        urlCacheBeanDao.save(bean);
                    }
                });
            }
            break;
            case R.id.btn_showMessage: {
                Property property = urlCacheBeanDao.getPkProperty();
                WhereCondition condition = property.gt(url);
                UrlCacheBean unique = urlCacheBeanDao.queryBuilder().where(condition).build().unique();

                TooltipUtils.showToastL(activity,unique.getUrlResult());
                List<UrlCacheBean> urlCacheBeans = urlCacheBeanDao.loadAll();
                for (int i = 0; i < urlCacheBeans.size(); i++) {
                    CommonUtils.LOG_D(getClass(), urlCacheBeans.get(i).getUrlResult());
                }
            }
            break;
        }
    }
}
