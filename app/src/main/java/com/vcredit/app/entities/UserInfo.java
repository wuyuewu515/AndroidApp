package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;



/**
 * Created by wangzhengji on 2015/4/10.
 */

public class UserInfo implements Serializable {
    /**
     * {
     "realName": "赖长平",
     "mobileNo": "13761197891",
     "isAuthName": false,
     "unreadMsgQty": 3,
     "cardQty": 5,
     "serviceTel": "400-820-5181"
     }

     */
    //用户姓名
    @Expose
    private String realName;

    //手机号码
    @Expose
    private String mobileNo;

    //是否认证实名（true 已认证 false 未认证）
    @Expose
    private boolean isAuthName;

    //消息数
    @Expose
    private int unreadMsgQty;

    //奖品数
    @Expose
    private int cardQty;

    @Expose
    private String serviceTel;

    @Expose
    private String accessToken;

    public String getRealName() {
        return realName;
    }

    public UserInfo setRealName(String realName) {
        this.realName = realName;
        return this;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public UserInfo setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
        return this;
    }

    public boolean isAuthName() {
        return isAuthName;
    }

    public UserInfo setAuthName(boolean authName) {
        isAuthName = authName;
        return this;
    }

    public int getUnreadMsgQty() {
        return unreadMsgQty;
    }

    public UserInfo setUnreadMsgQty(int unreadMsgQty) {
        this.unreadMsgQty = unreadMsgQty;
        return this;
    }

    public int getCardQty() {
        return cardQty;
    }

    public UserInfo setCardQty(int cardQty) {
        this.cardQty = cardQty;
        return this;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public UserInfo setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public UserInfo setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
