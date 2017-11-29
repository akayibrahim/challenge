package org.chl.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Document
public class ChallengeAttendance {
    @Id
    private String id;

    private String challengeId;

    private String memberId;

    private String askAcceptOrReject;

    private String acceptOrReject;

    private String join;

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

    public String getAskAcceptOrReject() {
        return askAcceptOrReject;
    }

    public void setAskAcceptOrReject(String askAcceptOrReject) {
        this.askAcceptOrReject = askAcceptOrReject;
    }

    public String getAcceptOrReject() {
        return acceptOrReject;
    }

    public void setAcceptOrReject(String acceptOrReject) {
        this.acceptOrReject = acceptOrReject;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }
}
