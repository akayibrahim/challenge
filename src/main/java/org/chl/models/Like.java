package org.chl.models;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

/**
 * Created by ibrahim on 11/28/2017.
 */
@Validated
@Document
public class Like {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the challengeId parameter")
    private String challengeId;
    @NotEmpty(message="You need to pass the memberId parameter")
    private String memberId;

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
