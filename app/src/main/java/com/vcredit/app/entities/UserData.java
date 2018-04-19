package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;



/**
 * Created by shibenli on 2016/5/9.
 * 用户数据
 */

public class UserData extends ResultInfo implements Serializable {

    @Expose
    protected UserInfo userInfo;

    @Expose
    protected CreditInfo creditInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserData setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public CreditInfo getCreditInfo() {
        return creditInfo;
    }

    public UserData setCreditInfo(CreditInfo creditInfo) {
        this.creditInfo = creditInfo;
        return this;
    }

    private UserData() {}
    private static UserData userData;

    public static UserData getInstance() {
        if (userData == null) {
            userData = new UserData();
        }
        return userData;
    }

    public static void setUserData(UserData user) {
        if (user == null){
            userData = null;
            return;
        }
        userData = getInstance();
    }

}
