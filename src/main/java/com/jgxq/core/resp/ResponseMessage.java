package com.jgxq.core.resp;

import java.net.HttpURLConnection;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
public class ResponseMessage {
    private String code;
    private String msg;
    private Object data;

    /**
     * 默认构造函数是成功
     *
     * @param data
     */
    public ResponseMessage(Object data) {
        this(String.valueOf(HttpURLConnection.HTTP_OK), null, data);
    }

    public ResponseMessage (Object data, String msg) {
        this.msg = msg;
        this.data = data;
    }

    /**
     * 两个参数是失败
     *
     * @param code
     * @param msg
     */
    public ResponseMessage(String code, String msg) {
        this(code, msg, null);
    }

    public ResponseMessage(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;


    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String rtnCode) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
