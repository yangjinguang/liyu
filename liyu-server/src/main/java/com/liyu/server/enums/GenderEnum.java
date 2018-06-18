package com.liyu.server.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderEnum {
    MALE("男性"),
    FEMALE("女性"),
    OTHER("其他");

    final String desc;

    GenderEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
