package org.chl.intf;

import org.chl.model.Activity;
import org.chl.model.Notification;

import java.util.List;

/**
 * Created by ibrahim on 06/28/2018.
 */
public interface IActivityService {
    void createActivity(Activity activity);

    List<Activity> getActivities(String memberId);
}
