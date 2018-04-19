package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;



/**
 * Created by shibenli on 2016/7/15.
 * 信用信息
 */

public class CreditInfo implements Serializable {
    /**
     * {
     "limit": 611,
     "score": 20000,
     "evaluation": "信用良好",
     "status": "1"
     }
     */

    @Expose
    protected int limit;

    @Expose
    protected int score;

    @Expose
    protected String evaluation;

    @Expose
    protected String status;

    public int getLimit() {
        return limit;
    }

    public CreditInfo setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getScore() {
        return score;
    }

    public CreditInfo setScore(int score) {
        this.score = score;
        return this;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public CreditInfo setEvaluation(String evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public CreditInfo setStatus(String status) {
        this.status = status;
        return this;
    }
}
