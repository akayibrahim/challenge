package org.chl.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Document
public class Challenge {
    @Id
    private String id;

    private String challengerId;

    private String thinksAboutChallenge;

    private String challengeSubject;

    private String chlDate;

    private String untilDate;

    private String type;

    private List<String> attendanceRequestList;

    private List<String> attendanceAcceptList;


    private List<String> joinToChallengeList;

    public List<String> getJoinToChallengeList() {
        return joinToChallengeList;
    }

    public void setJoinToChallengeList(List<String> joinToChallengeList) {
        this.joinToChallengeList = joinToChallengeList;
    }

    public List<String> getAttendanceAcceptList() {
        return attendanceAcceptList;
    }

    public void setAttendanceAcceptList(List<String> attendanceAcceptList) {
        this.attendanceAcceptList = attendanceAcceptList;
    }

    public List<String> getAttendanceRequestList() {
        return attendanceRequestList;
    }

    public void setAttendanceRequestList(List<String> attendanceRequestList) {
        this.attendanceRequestList = attendanceRequestList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChallengerId() {
        return challengerId;
    }

    public void setChallengerId(String challengerId) {
        this.challengerId = challengerId;
    }

    public String getThinksAboutChallenge() {
        return thinksAboutChallenge;
    }

    public void setThinksAboutChallenge(String thinksAboutChallenge) {
        this.thinksAboutChallenge = thinksAboutChallenge;
    }

    public String getChallengeSubject() {
        return challengeSubject;
    }

    public void setChallengeSubject(String challengeSubject) {
        this.challengeSubject = challengeSubject;
    }

    public String getChlDate() {
        return chlDate;
    }

    public void setChlDate(String chlDate) {
        this.chlDate = chlDate;
    }

    public String getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
