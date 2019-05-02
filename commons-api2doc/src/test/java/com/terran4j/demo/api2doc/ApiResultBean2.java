package com.terran4j.demo.api2doc;

import com.terran4j.commons.api2doc.annotations.ApiComment;

public class ApiResultBean2<T> {
    @ApiComment(value = "提示信息")
    private String resMsg2;
    @ApiComment(value = "返回值集合")
    private T result2;

    public String getResMsg2() {
        return resMsg2;
    }

    public void setResMsg2(String resMsg2) {
        this.resMsg2 = resMsg2;
    }

    public T getResult2() {
        return result2;
    }

    public void setResult2(T result2) {
        this.result2 = result2;
    }
}
