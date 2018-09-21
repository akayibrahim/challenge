package org.chl.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Created by ibrahim on 12/17/2017.
 */
public enum Subject {
    RUNNING(true),
    READING(true),
    WALKING(true),
    SWIMMING(true),
    PINBALL(true),
    PING_PONG(true),
    HOLIDAY(true),
    LANGUAGE(true),
    FOOTBALL(false),
    BASKETBALL(false),
    GOLF(true),
    DRIVING(false),
    LOSING_WEIGHT(true);

    Boolean isSelf;

    Subject(Boolean isSelf) {
        this.isSelf= isSelf;
    }
    /*
    @JsonCreator
    public static Subject fromName(String name) {
        for (Subject subject : values()) {
            if(subject.name.equalsIgnoreCase(name))
                return subject;
        }
        throw new IllegalArgumentException("Unknown subject type " + name + ", Allowed subjects are " +
                Arrays.toString(values()));
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(name);
    }
    */
}
