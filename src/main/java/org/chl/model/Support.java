package org.chl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by ibrahim on 11/28/2017.
 */
@Validated
@Document
public class Support {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the challengeId parameter")
    private String challengeId;
    @NotEmpty(message="You need to pass the memberId parameter")
    private String memberId;

    private String supportedMemberId;
    private Date date;
    @JsonProperty
    private Boolean supportFirstTeam;
    @JsonProperty
    private Boolean supportSecondTeam;

    public String getSupportedMemberId() {
        return supportedMemberId;
    }

    public void setSupportedMemberId(String supportedMemberId) {
        this.supportedMemberId = supportedMemberId;
    }

    public Boolean getSupportFirstTeam() {
        return supportFirstTeam;
    }

    public void setSupportFirstTeam(Boolean supportFirstTeam) {
        this.supportFirstTeam = supportFirstTeam;
    }

    public Boolean getSupportSecondTeam() {
        return supportSecondTeam;
    }

    public void setSupportSecondTeam(Boolean supportSecondTeam) {
        this.supportSecondTeam = supportSecondTeam;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
