package com.terran4j.demo.api2doc;

import com.terran4j.commons.api2doc.annotations.ApiComment;

public class ApiResultBean<T> {
    @ApiComment(value = "提示信息")
    private String resMsg;
    @ApiComment(value = "返回值集合")
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
