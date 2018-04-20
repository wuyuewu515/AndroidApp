package com.vcredit.utils.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.vcredit.global.InterfaceConfig;
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

    public PostStringRequest(String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        this.mParams = params;
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
}
