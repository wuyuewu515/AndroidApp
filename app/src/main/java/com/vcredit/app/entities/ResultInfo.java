package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;



/**
 * Created by zhuofeng on 2015/8/18.
 */

public class ResultInfo implements Serializable{

    /**
     * displayInfo : 最强王者
     * operationResult : true
     * displayLevel :"2" 	提示级别(1、强提示 2、弱提示)
     */
    @Expose
    protected String displayInfo;
    @Expose
    protected String displayLevel;
    @Expose
    protected boolean operationResult;

    public String getDisplayInfo() {
        return displayInfo;
    }

    public ResultInfo setDisplayInfo(String displayInfo) {
        this.displayInfo = displayInfo;
        return this;
    }

    public String getDisplayLevel() {
        return displayLevel;
    }

    public ResultInfo setDisplayLevel(String displayLevel) {
        this.displayLevel = displayLevel;
        return this;
    }

    public boolean isOperationResult() {
        return operationResult;
    }

    public ResultInfo setOperationResult(boolean operationResult) {
        this.operationResult = operationResult;
        return this;
    }
}
