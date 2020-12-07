package com.jgxq.front.define;

public enum ForumErrorCode {

    /**
     * 没有权限
     */
    NO_Permission("0068000001"),

    /**
     * userKey不能为空
     */
    UserKey_Cant_Empty("0068000002"),

    /**
     * 用户不存在
     */
    User_Not_Exists("0068000003"),

    /**
     * 手机号或密码错误
     */
    TelOrPassword_Error("0068000004"),

    /**
     * 短信发送失败
     */
    Message_Send_Error("0068000005"),

    /**
     * 用户已存在
     */
    User_Exists("0068000006");

    private String errorCode;

    ForumErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
