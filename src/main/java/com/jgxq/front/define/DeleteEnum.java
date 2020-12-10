package com.jgxq.front.define;

public enum DeleteEnum {
    NORMAL((byte) 1), DELETE((byte) -1);

    private Byte value;

    DeleteEnum(Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return this.value;
    }

    public DeleteEnum valueOf(Byte value) {
        DeleteEnum s = null;
        switch (value) {
            case (byte) 1:
                s = NORMAL;
                break;
            case (byte) -1:
                s = DELETE;
                break;
        }
        return s;
    }

    /**
     * 判断数值是否属于枚举类的值
     *
     * @param key
     * @return
     */
    public static boolean isInclude(Byte key) {
        boolean include = false;
        for (DeleteEnum e : DeleteEnum.values()) {
            if (e.value == key) {
                include = true;
                break;
            }
        }
        return include;
    }
}

