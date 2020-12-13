package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-12
 **/
public enum InteractionType {

    NEWS(0),
    TALK(1),
    COMMENT(2);


    InteractionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;
}
