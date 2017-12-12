package org.chl.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by ibrahim on 12/9/2017.
 */
@Document
public class PushNotification extends Notification {
    private Date untilDate;

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }
}
