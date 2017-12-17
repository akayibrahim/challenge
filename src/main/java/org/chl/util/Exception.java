package org.chl.util;

/**
 * Created by ibrahim on 12/17/2017.
 */
public class Exception {
    public static void throwDoneExceptionForVersus() {
        throw new IllegalArgumentException("if challange is done, teams Score are mandatory");
    }
}
