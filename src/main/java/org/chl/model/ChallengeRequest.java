package org.chl.model;

import org.chl.util.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by ibrahim on 07/02/2018.
 */
@Validated
@Document
public class ChallengeRequest {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the challengeId parameter")
    private String challengeId;
    @NotEmpty(message="You need to pass the memberId parameter")
    private String memberId;
    @NotEmpty(message="You need to pass the facebookID parameter")
    private String facebookID;
    @NotEmpty(message="You need to pass the name parameter")
    private String name;
    @NotEmpty(message="You need to pass the surname parameter")
    private String surname;
    @NotEmpty(message="You need to pass the type parameter")
    private Constant.REQUEST_TYPE type;
    @NotEmpty(message="You need to pass the subject parameter")
    private String subject;

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Constant.REQUEST_TYPE getType() {
        return type;
    }

    public void setType(Constant.REQUEST_TYPE type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
