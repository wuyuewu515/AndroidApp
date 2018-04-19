package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;



/**
 * 系统枚举
 * Created by zhuofeng on 2015/8/24.
 */

public class SysEnumInfo implements Serializable {
    /*
        versionCodeEnum	枚举最新版本号（初始版本 1）
        "eSchool（学校枚举）"
        "eSchoolRoll （学籍层次）"
        "eEducationCargo（学历类别）"
        "eAgeLimit（年限）"
        "eMarriage（银行）"
        "eBank（婚姻状况字典）"
        "eEducation（学历字典）"
        eDegree	object	学历贷学历字典
*/
    @Expose
    private int versionCodeEnum;
    @Expose
    private List<KeyValueEntity> eSchool;
    @Expose
    private List<KeyValueEntity> eSchoolRoll;
    @Expose
    private List<KeyValueEntity> eEducationCargo;
    @Expose
    private List<KeyValueEntity> eAgeLimit;
    @Expose
    private List<KeyValueEntity> eBank;
    @Expose
    private List<KeyValueEntity> eMarriage;
    @Expose
    private List<KeyValueEntity> eDegree;

    public int getVersionCodeEnum() {
        return versionCodeEnum;
    }

    public SysEnumInfo setVersionCodeEnum(int versionCodeEnum) {
        this.versionCodeEnum = versionCodeEnum;
        return this;
    }

    public List<KeyValueEntity> geteSchool() {
        return eSchool;
    }

    public SysEnumInfo seteSchool(List<KeyValueEntity> eSchool) {
        this.eSchool = eSchool;
        return this;
    }

    public List<KeyValueEntity> geteSchoolRoll() {
        return eSchoolRoll;
    }

    public SysEnumInfo seteSchoolRoll(List<KeyValueEntity> eSchoolRoll) {
        this.eSchoolRoll = eSchoolRoll;
        return this;
    }

    public List<KeyValueEntity> geteEducationCargo() {
        return eEducationCargo;
    }

    public SysEnumInfo seteEducationCargo(List<KeyValueEntity> eEducationCargo) {
        this.eEducationCargo = eEducationCargo;
        return this;
    }

    public List<KeyValueEntity> geteAgeLimit() {
        return eAgeLimit;
    }

    public SysEnumInfo seteAgeLimit(List<KeyValueEntity> eAgeLimit) {
        this.eAgeLimit = eAgeLimit;
        return this;
    }

    public List<KeyValueEntity> geteBank() {
        return eBank;
    }

    public SysEnumInfo seteBank(List<KeyValueEntity> eBank) {
        this.eBank = eBank;
        return this;
    }

    public List<KeyValueEntity> geteMarriage() {
        return eMarriage;
    }

    public SysEnumInfo seteMarriage(List<KeyValueEntity> eMarriage) {
        this.eMarriage = eMarriage;
        return this;
    }

    public List<KeyValueEntity> geteDegree() {
        return eDegree;
    }

    public SysEnumInfo seteDegree(List<KeyValueEntity> eDegree) {
        this.eDegree = eDegree;
        return this;
    }
}
