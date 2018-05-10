package com.vcredit.app.entities;

import java.util.Date;

/**
 * 作者: 伍跃武
 * 时间： 2018/5/10
 * 描述：日历实体对象
 */

public class ClendarInfo {
    protected Date date;
    private String doThing;


    public Date getDate() {
        return date;
    }

    public ClendarInfo setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getDoThing() {
        return doThing;
    }

    public ClendarInfo setDoThing(String doThing) {
        this.doThing = doThing;
        return this;
    }
}
