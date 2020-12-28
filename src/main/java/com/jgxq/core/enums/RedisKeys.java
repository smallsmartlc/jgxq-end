package com.jgxq.core.enums;

public enum RedisKeys {
    top_news("JGXQ_20000915_TOP");


    private String key;

    RedisKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
