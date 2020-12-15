package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-12
 **/
public enum ReadType {

    READ((byte)0),
    UNREAD((byte)1);


    ReadType(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }

    private Byte value;
}
