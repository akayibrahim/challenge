package org.chl.util;

import org.springframework.util.StringUtils;

/**
 * Created by ibrahim on 12/17/2017.
 */
public class Validation {
    public static void doneValidationForVersus(Boolean done, String firstTeamScore, String secondTeamScore) {
        if(done & (StringUtils.isEmpty(firstTeamScore) || StringUtils.isEmpty(secondTeamScore)))
            Exception.throwDoneExceptionForVersus();
    }
}
