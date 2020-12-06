package com.jgxq.core.resp;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
public class ErrorMessage {
    private String code;
    private String message;
    private String fields;

    public ErrorMessage() {
    }

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorMessage(String code, String message, String fields) {
        this.code = code;
        this.message = message;
        this.fields = fields;
    }

    public String getFields() {
        return this.fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

