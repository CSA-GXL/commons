package com.terran4j.demo.api2doc;

import com.terran4j.commons.api2doc.annotations.ApiComment;

public class ApiResultBean3<T> {
    @ApiComment(value = "提示信息")
    private String resMsg3;
    @ApiComment(value = "返回值集合")
    private T result3;

    public String getResMsg3() {
        return resMsg3;
    }

    public void setResMsg3(String resMsg3) {
        this.resMsg3 = resMsg3;
    }

    public T getResult3() {
        return result3;
    }

    public void setResult3(T result3) {
        this.result3 = result3;
    }
}
