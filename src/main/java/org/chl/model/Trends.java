package org.chl.model;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 06/8/2018.
 */
@Validated
@Document
public class Trends {
    @NotEmpty(message="You need to pass the challengeId parameter")
    private String challengeId;
    @NotEmpty(message="You need to pass the name parameter")
    private String name;
    @NotEmpty(message="You need to pass the proofedMedia parameter")
    private String proof;
    @NotEmpty(message="You need to pass the prooferFbID parameter")
    private String prooferFbID;
    @NotEmpty(message = "You need to pass the subject parameter")
    private String subject;

    private String challengerId;

    private Boolean provedWithImage;

    public Boolean getProvedWithImage() {
        return provedWithImage;
    }

    public void setProvedWithImage(Boolean provedWithImage) {
        this.provedWithImage = provedWithImage;
    }

    public String getChallengerId() {
        return challengerId;
    }

    public void setChallengerId(String challengerId) {
        this.challengerId = challengerId;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getProoferFbID() {
        return prooferFbID;
    }

    public void setProoferFbID(String prooferFbID) {
        this.prooferFbID = prooferFbID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
