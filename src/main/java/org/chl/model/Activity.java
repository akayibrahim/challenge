package org.chl.model;

import org.chl.util.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by ibrahim on 06/28/2018.
 */
@Validated
@Document
public class Activity {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the challengeId parameter")
    private String challengeId;
    @NotEmpty(message="You need to pass the fromMemberId parameter")
    private String fromMemberId;
    @NotEmpty(message="You need to pass the toMemberId parameter")
    private String toMemberId;
    @NotEmpty(message="You need to pass the facebookID parameter")
    private String facebookID;
    @NotEmpty(message="You need to pass the name parameter")
    private String name;
    @NotEmpty(message="You need to pass the type parameter")
    private Constant.ACTIVITY type;

    private String content;

    private String activityTableId;

    private  String mediaObjectId;

    private Date insertDate;

    private Boolean provedWithImage;

    public Boolean getProvedWithImage() {
        return provedWithImage;
    }

    public void setProvedWithImage(Boolean provedWithImage) {
        this.provedWithImage = provedWithImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(String fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public String getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(String toMemberId) {
        this.toMemberId = toMemberId;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Constant.ACTIVITY getType() {
        return type;
    }

    public void setType(Constant.ACTIVITY type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getActivityTableId() {
        return activityTableId;
    }

    public void setActivityTableId(String activityTableId) {
        this.activityTableId = activityTableId;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getMediaObjectId() {
        return mediaObjectId;
    }

    public void setMediaObjectId(String mediaObjectId) {
        this.mediaObjectId = mediaObjectId;
    }
}
