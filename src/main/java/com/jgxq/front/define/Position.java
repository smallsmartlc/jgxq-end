package com.jgxq.front.define;

/**
 * @author LuCong
 * @since 2020-12-10
 **/
public enum Position {

    GK("门将", 0),
    DB("后卫",1),
    AM("中场",2),
    AF("前锋",3);

    public static String getPositionByVal(int value){
        for (Position position : values()) {
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

    Position(String foot, int value) {
        this.position = foot;
        this.value = value;
    }

    private String position;
    private int value;
}
