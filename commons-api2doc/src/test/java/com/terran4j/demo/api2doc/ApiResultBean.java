package com.terran4j.demo.api2doc;

public class ApiResultBean<T> {
    private String resMsg;
    private T result;

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
