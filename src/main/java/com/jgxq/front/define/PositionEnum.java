package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-10
 **/
public enum PositionEnum {

    GK("门将", 0),
    B("后卫",1),
    M("中场",1),
    F("前锋",3);

    public static String getPositionByVal(int value){
        for (PositionEnum position : values()) {
            if(position.getValue() == value){
                return position.getPosition();
            }
        }
        return null;
    }

    public String getPosition() {
        return position;
    }

    public int getValue() {
        return value;
    }

    PositionEnum(String foot, int value) {
        this.position = foot;
        this.value = value;
    }

    private String position;
    private int value;
}
