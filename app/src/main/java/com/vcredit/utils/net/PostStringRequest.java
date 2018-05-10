package com.vcredit.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.vcredit.app.entities.UrlCacheBean;
import com.vcredit.app.entities.UrlCacheBeanDao;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.global.SampleApplicationLike;
import com.vcredit.utils.EncryptUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: 伍跃武
 * 时间： 2018/4/20
 * 描述： postString 方法
 */

public class PostStringRequest extends StringRequest {
    private Map<String, String> mParams = new HashMap<>();
    private boolean needCache = false;
    private String useragent;


    /**
     * @param url           请求服务器地址
     * @param params        参数
     * @param listener      成功监听
     * @param errorListener 错误监听
     * @param needCache     是否需要本地缓存
     */
    public PostStringRequest(String url, Map<String, String> params, Response.Listener<String> listener,
                             Response.ErrorListener errorListener, boolean needCache) {
        super(Method.POST, url, listener, errorListener);
        this.mParams = params;
        this.needCache = needCache;
        setRetryPolicy(new DefaultRetryPolicy(InterfaceConfig.SOCKET_TIMEOUT, InterfaceConfig.MAX_RETRIES, InterfaceConfig.BACK_OFF));
    }


    /**
     * 重写请求编码
     *
     * @return
     */
    @Override
    protected String getParamsEncoding() {
        return InterfaceConfig.ENCODEING;
    }

    /**
     * 重写请求头
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("access-key", "VaReBQn0BVxBoCWqsFmBVvJz3mukQs1U");
        long currentTime = System.currentTimeMillis();
        headers.put("client-time", currentTime + "");
        headers.put("platform", "android");
        headers.put("User-Agent", useragent);
        String result = "access-key=VaReBQn0BVxBoCWqsFmBVvJz3mukQs1U&client-time=" + currentTime + "&platform=android";
        String md5Sign = "";
        try {
            String encode = URLEncoder.encode(result, "UTF-8");
            md5Sign = EncryptUtils.md5(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.put("sign", md5Sign);
        return headers;
    }

    /**
     * 重写获取参数的方法
     *
     * @return
     */
    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected void deliverResponse(String response) {

        if (needCache) {
            UrlCacheBeanDao urlCacheBeanDao = SampleApplicationLike.getInstance().getDaoSession().getUrlCacheBeanDao();
            String paramsMd5 = EncryptUtils.md5_16(getParams().toString());
            String urlMd5 = EncryptUtils.md5_16(getUrl());
            String keyMd5 = paramsMd5 + urlMd5;
            //如果数据库中有这个数据，就更新
            UrlCacheBean unique = urlCacheBeanDao.queryBuilder().where(UrlCacheBeanDao.Properties.UrlMd5.eq(keyMd5)).build().unique();
            UrlCacheBean bean;
            if (null != unique) {
                long id = unique.getId();
                bean = new UrlCacheBean(id, keyMd5, response);
            } else {
                bean = new UrlCacheBean(null, keyMd5, response);
            }
            urlCacheBeanDao.save(bean);
        }
        super.deliverResponse(response);
    }

    /**
     * 设置ua
     * @param useragent ua
     */
    public void setUserAgent(String useragent) {
        this.useragent = useragent;

    }

}
