package com.jgxq.core.enums;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
public enum CommonErrorCode {
    UNKNOWN_ERROR("0000000000"),
    MISSING_REQUIRED_HTTP_BODY("0000000001"),
    NUMBER_FORMAT_EXCEPTION("0000000002"),
    VALIDATION_ERROR("0000000003"),
    HTTP_METHOD_NOT_SUPPORTED("0000000004"),
    ARGUMENT_TYPE_MISMATCH("0000000005"),
    MISSING_PATH_VARIABLE("0000000006"),
    ILLEGAL_REQUEST("00000000007"),
    BAD_REQUEST("0000000008"),
    BAD_PARAMETERS("0000000009"),
    INTERNAL_SERVER_ERROR("0000000010"),
    FEIGN_EXCEPTION("0000000011"),
    MAPPING_EXCEPTION("0000000012"),
    DATA_INTEGRITY_VIOLATION_EXCEPTION("0000000013");

    private String errorCode;

    private CommonErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
