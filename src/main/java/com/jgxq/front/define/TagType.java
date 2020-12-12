package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-12
 **/
public enum  TagType {

    TEAM( 0),
    PLAYER(1);

    TagType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;
}
