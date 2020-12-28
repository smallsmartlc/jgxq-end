package com.jgxq.front.define;

public enum BooleanEnum {
    True((byte) 1), False((byte) 0);

    private Byte value;

    BooleanEnum(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return this.value;
    }

}

