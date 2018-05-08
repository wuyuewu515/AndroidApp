package com.vcredit.app.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * 作者: 伍跃武
 * 时间： 2018/5/4
 * 描述：url缓存数
 */
@Entity
public class UrlCacheBean {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "URL")
    private String urlMd5;
    @Property(nameInDb = "URLRESULT")
    private String urlResult;

    @Generated(hash = 2028091366)
    public UrlCacheBean(Long id, String urlMd5, String urlResult) {
        this.id = id;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UrlCacheBean{" +
                "id=" + id +
                ", urlMd5='" + urlMd5 + '\'' +
                ", urlResult='" + urlResult + '\'' +
                '}';
    }
}
