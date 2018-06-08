package org.chl.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * Created by ibrahim on 12/17/2017.
 */
public enum Subject {
    RUNNING,
    READ_BOOK, // TODO change to READING
    SWIMMING,
    PINBALL,
    PING_PONG,
    HOLIDAY,
    LEARNING_LANGUAGE,
    LOSING_WEIGHT;

    /*
    Subject() {
        this.name = name;
    }

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
