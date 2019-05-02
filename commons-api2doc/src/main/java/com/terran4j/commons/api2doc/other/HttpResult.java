package com.terran4j.commons.api2doc.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.terran4j.commons.api2doc.other.utils.Strings;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResult {

    public static final String SUCCESS_CODE = "success";

    private String requestId;

    /**
     * 服务端开始处理的时间。
     */
    private long serverTime;

    private long spendTime;

    private String resultCode;

    private Object data;

    private String message;

    private Map<String, Object> props = null;

    private List<String> logs = null;

    public static HttpResult success() {
        HttpResult response = new HttpResult();
        response.setResultCode(SUCCESS_CODE);
        return response;
    }

    public static HttpResult success(Object data) {
        HttpResult response = new HttpResult();
        response.setResultCode(SUCCESS_CODE);
        response.setData(data);
        return response;
    }

    private static final long defaultServerTime = System.currentTimeMillis();


    public static HttpResult successFully(Object data) {
        HttpResult response = new HttpResult();
        response.setRequestId("");
        response.setSpendTime(5);
        response.setServerTime(defaultServerTime);
        response.setResultCode(SUCCESS_CODE);
        response.setData(data);
        return response;
    }


    public void clearPropsIfEmpty() {
        if (props != null && props.size() == 0) {
            props = null;
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;
    }

    public final String toString() {
        return Strings.toString(this);
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}