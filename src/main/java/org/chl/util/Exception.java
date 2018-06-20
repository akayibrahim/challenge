package org.chl.util;

/**
 * Created by ibrahim on 12/17/2017.
 */
public class Exception {
    public static void throwDoneExceptionForVersus() {
        throw new IllegalArgumentException("if challange is done, teams Score are mandatory");
    }

    public static void throwCheckEqualOfTeamCountExceptionForVersus() {
        throw new IllegalArgumentException("Team count has to be equal.");
    }

    public static void throwMemberNotAvailable() {
        throw new IllegalArgumentException("Member is not avaliable.");
    }

    public static void throwNotFoundRecord() {
        throw new IllegalArgumentException("Record not found.");
    }

    public static void throwChallengerHasToJoin() {
        throw new IllegalArgumentException("Challenger has to join.");
    }

    public static void throwUpdateCannotForDone() {
        throw new IllegalArgumentException("Done challenge cannot update.");
    }
}
