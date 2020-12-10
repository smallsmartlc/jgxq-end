package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-10
 **/
public enum StrongFootEnum {

    RIGHT("右脚", 0),
    LEFT("左脚",1),
    DOUBLE("双脚",2);

    public static String getFootByVal(int value){
        for (StrongFootEnum foot : values()) {
            if(foot.getValue() == value){
                return foot.getFoot();
            }
        }
        return null;
    }

    public String getFoot() {
        return foot;
    }

    public int getValue() {
        return value;
    }

    StrongFootEnum(String foot, int value) {
        this.foot = foot;
        this.value = value;
    }

    private String foot;
    private int value;
}
