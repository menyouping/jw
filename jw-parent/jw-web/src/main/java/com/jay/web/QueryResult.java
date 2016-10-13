package com.jay.web;

import com.alibaba.fastjson.JSON;

public class QueryResult {

    public static final Integer OK = 200;
    public static final Integer FAILED = 0;
    
    private int status = OK;
    private String message;
    private Object result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
