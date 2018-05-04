package com.vcredit.app.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

/**
 * 作者: 伍跃武
 * 时间： 2018/5/4
 * 描述：url缓存数
 */
@Entity
public class UrlCacheBean {

    @Property
    private String urlMd5;
    @Property
    private String urlResult;
    @Generated(hash = 2014956234)
    public UrlCacheBean(String urlMd5, String urlResult) {
        this.urlMd5 = urlMd5;
        this.urlResult = urlResult;
    }
    @Generated(hash = 1778004388)
    public UrlCacheBean() {
    }
    public String getUrlMd5() {
        return this.urlMd5;
    }
    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }
    public String getUrlResult() {
        return this.urlResult;
    }
    public void setUrlResult(String urlResult) {
        this.urlResult = urlResult;
    }


}
