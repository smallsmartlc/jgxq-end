package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
public enum KeyLength {

    USER_KEY_LEN(8);

    private int length;

    KeyLength(int length) {
        this.length = length;
    }

    public int getLength(){
        return length;
    }
}
