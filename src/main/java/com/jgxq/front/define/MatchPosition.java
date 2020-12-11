package com.jgxq.front.define;

public enum MatchPosition {

    GK(0),
    DB(1),
    DM(2),
    AM(3),
    AF(4);

    MatchPosition(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;
}
