package com.jgxq.core.exception;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
public class SmartException extends RuntimeException {
    private String code;
    private String fields;

    public SmartException(Throwable cause) {
        super(cause);
    }

    public SmartException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public SmartException(String code, String message) {
        super(message);
        this.code = code;
    }

    public SmartException(String code, String message, String fields) {
        super(message);
        this.code = code;
        this.fields = fields;
    }

    public SmartException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public SmartException(String code, String message, String fields, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.fields = fields;
    }

    public String getCode() {
        return code;
    }

    public String getFields() {
        return fields;
    }
}
