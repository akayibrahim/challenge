package org.chl.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.util.StringUtils;

/**
 * Created by ibrahim on 12/17/2017.
 */
public class Validation {
    public static void doneValidationForVersus(Boolean done, Boolean homeWin, Boolean awayWin) {
        if(done != null && done & (homeWin == null || awayWin == null))
            Exception.throwDoneExceptionForVersus();
    }

    public static void checkTeamCountEqual(String firstTeamCount, String secondTeamCount) {
        if(!StringUtils.isEmpty(firstTeamCount) && !StringUtils.isEmpty(secondTeamCount) && !firstTeamCount.equals(secondTeamCount))
            Exception.throwCheckEqualOfTeamCountExceptionForVersus();
    }

    public static void challergerNotJoin() {
            Exception.throwChallengerHasToJoin();
    }
}
