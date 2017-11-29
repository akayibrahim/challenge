package org.chl.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Created by ibrahim on 11/28/2017.
 */
@Document
public class Like {
    @Id
    private String id;

    private String challengeId;

    private String memberId;

    private Long countOfLike;

    public Long getCountOfLike() {
        return countOfLike;
    }

    public void setCountOfLike(Long countOfLike) {
        this.countOfLike = countOfLike;
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
