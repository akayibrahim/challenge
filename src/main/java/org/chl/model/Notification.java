package org.chl.model;

import org.chl.util.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by ibrahim on 12/9/2017.
 */
@Document
public class Notification {
    @Id
    private String id;
    private String memberId;
    private String challengeId;
    private String messageTitle;
    private String message;
    private Constant.PUSH_NOTIFICATION notification;

    public Constant.PUSH_NOTIFICATION getNotification() {
        return notification;
    }

    public void setNotification(Constant.PUSH_NOTIFICATION notification) {
        this.notification = notification;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
