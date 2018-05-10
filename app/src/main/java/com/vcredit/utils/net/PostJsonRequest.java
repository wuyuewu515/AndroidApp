package com.vcredit.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.vcredit.app.entities.UrlCacheBean;
import com.vcredit.app.entities.UrlCacheBeanDao;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.global.SampleApplicationLike;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.EncryptUtils;
import com.vcredit.utils.HttpUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuofeng on 2015/4/8.
 * <p>
 * Volley自定义POST请求
 */
public class PostJsonRequest extends JsonObjectRequest {

    private boolean needCache = false;
    private JSONObject params;
    private String useragent;

    /**
     * 构造函数，初始化请求对象
     */
    public PostJsonRequest(String url, JSONObject params, Listener<JSONObject> listener,
                           ErrorListener errorListener, boolean needCache) {
        super(Method.POST, url, params, listener, errorListener);
        this.needCache = needCache;
        this.params = params;
        setRetryPolicy(new DefaultRetryPolicy(InterfaceConfig.SOCKET_TIMEOUT, InterfaceConfig.MAX_RETRIES, InterfaceConfig.BACK_OFF));
    }

    /**
     * 重写参数编码方法
     */
    @Override
    protected String getParamsEncoding() {
        return InterfaceConfig.ENCODEING;
    }

    /**
     * 重写请求头获取方法
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Charsert", getParamsEncoding());
        headers.put("Content-Type", "application/json;charset=utf-8");
        headers.put("Accept-Encoding", "gzip,deflate");
        headers.put("User-Agent", useragent);
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        if (!CommonUtils.isNullOrEmpty(HttpUtil.sessionId))
            headers.put("x-auth-token", HttpUtil.sessionId);
        return headers;
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        super.deliverResponse(response);

        if (needCache) {
            UrlCacheBeanDao urlCacheBeanDao = SampleApplicationLike.getInstance().getDaoSession().getUrlCacheBeanDao();

            String paramsMd5 = EncryptUtils.md5_16(params.toString());
            String urlMd5 = EncryptUtils.md5_16(getUrl());
            String keyMd5 = paramsMd5 + urlMd5;
            //如果数据库中有这个数据，就更新
            UrlCacheBean unique = urlCacheBeanDao.queryBuilder().where(UrlCacheBeanDao.Properties.UrlMd5.eq(keyMd5)).build().unique();
            UrlCacheBean bean;
            if (null != unique) {
                long id = unique.getId();
                bean = new UrlCacheBean(id, keyMd5, response.toString());
            } else {
                bean = new UrlCacheBean(null, keyMd5, response.toString());
            }
            urlCacheBeanDao.save(bean);
        }

    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Response<JSONObject> superResponse = super.parseNetworkResponse(response);
        String authToken = response.headers.get("x-auth-token");
        if (!CommonUtils.isNullOrEmpty(authToken))
            HttpUtil.sessionId = authToken;
        return superResponse;
    }

    public void setUserAgent(String useragent) {
        this.useragent = useragent;

    }
}
