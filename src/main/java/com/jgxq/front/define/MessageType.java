package com.jgxq.front.define;

public enum MessageType {

    thumb((byte) 0," 赞了我"),
    comment((byte) 1," 回复了我：");


    MessageType(byte value,String message) {
        this.value = value;
        this.message = message;
    }

    public byte getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    private byte value;
    private String message;
}
