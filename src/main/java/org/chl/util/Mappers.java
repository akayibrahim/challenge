package org.chl.util;

import org.chl.model.Activity;

public class Mappers {

    public static Activity prepareActivity(String activityTableId, String challengeId, String fromMemberId, String toMemberId, Constant.ACTIVITY type) {
        Activity activity = new Activity();
        activity.setActivityTableId(activityTableId);
        activity.setChallengeId(challengeId);
        activity.setFromMemberId(fromMemberId);
        activity.setToMemberId(toMemberId);
        activity.setType(type);
        return activity;
    }
}
