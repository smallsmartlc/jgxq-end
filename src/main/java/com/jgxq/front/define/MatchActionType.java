package com.jgxq.front.define;

public enum MatchActionType {

    GOAL(0),
    PENALTY(1),
    NO_GOAL(2),
    OWN_GOAL(3),
    ASSIST(4),
    YELLOW(5),
    RED(6),
    UP(7),
    DOWN(8);

    MatchActionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;
}
