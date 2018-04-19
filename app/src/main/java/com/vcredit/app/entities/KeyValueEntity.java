package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;



/**
 * Created by chenlei on 2016/3/25.
 */

public class KeyValueEntity implements Serializable {

    @Expose
    private String value;
    @Expose
    private String key;

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyValueEntity that = (KeyValueEntity) o;
        return key.equals(that.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }


    public String getValue() {
        return value;
    }

    public KeyValueEntity setValue(String value) {
        this.value = value;
        return this;
    }

    public String getKey() {
        return key;
    }

    public KeyValueEntity setKey(String key) {
        this.key = key;
        return this;
    }
}

