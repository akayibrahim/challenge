package org.chl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.chl.util.Constant;
import org.chl.util.Subject;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class Challenge {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the challengerId parameter")
    private String challengerId;
    @NotEmpty(message="You need to pass the challengerId parameter")
    private String challengerFBId;
    @NotEmpty(message="You need to pass the challengerId parameter")
    private String name;
    @NotEmpty(message="You need to pass the challengerId parameter")
    private String firstTeamCount;
    @NotEmpty(message="You need to pass the challengerId parameter")
    private String secondTeamCount;

    private String thinksAboutChallenge;
    @NotNull(message="You need to pass the subject parameter")
    @JsonProperty
    private Subject subject;
    @NotNull(message="You need to pass the chlDate parameter")
    private Date chlDate; // use bg
    @NotNull(message="You need to pass the untilDate parameter")
    private Date untilDate; // use bg

    private String untilDateStr;
    @NotNull(message="You need to pass the done parameter")
    @JsonProperty
    private Boolean done;
    @NotNull(message="You need to pass the updateDate parameter")
    private Date updateDate; // use bg
    @NotNull(message="You need to pass the isComeFromSelf parameter")
    @JsonProperty
    private Boolean comeFromSelf;
    @JsonProperty
    private Boolean supportFirstTeam;
    @JsonProperty
    private Boolean supportSecondTeam;

    private int firstTeamSupportCount;

    private int secondTeamSupportCount;

    private int countOfProofs;

    private String insertTime;

    private String status;

    @JsonProperty
    private Boolean proofed;

    private int countOfComments;

    private String firstTeamScore;

    private String secondTeamScore;

    public String getFirstTeamScore() {
        return firstTeamScore;
    }

    public void setFirstTeamScore(String firstTeamScore) {
        this.firstTeamScore = firstTeamScore;
    }

    public String getSecondTeamScore() {
        return secondTeamScore;
    }

    public void setSecondTeamScore(String secondTeamScore) {
        this.secondTeamScore = secondTeamScore;
    }

    public int getCountOfComments() {
        return countOfComments;
    }

    public void setCountOfComments(int countOfComments) {
        this.countOfComments = countOfComments;
    }

    private Constant.TYPE type;

    public Constant.TYPE getType() {
        return type;
    }

    public void setType(Constant.TYPE type) {
        this.type = type;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getChlDate() {
        return chlDate;
    }

    public void setChlDate(Date chlDate) {
        this.chlDate = chlDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstTeamCount() {
        return firstTeamCount;
    }

    public void setFirstTeamCount(String firstTeamCount) {
        this.firstTeamCount = firstTeamCount;
    }

    public String getSecondTeamCount() {
        return secondTeamCount;
    }

    public void setSecondTeamCount(String secondTeamCount) {
        this.secondTeamCount = secondTeamCount;
    }

    public String getChallengerFBId() {
        return challengerFBId;
    }

    public void setChallengerFBId(String challengerFBId) {
        this.challengerFBId = challengerFBId;
    }

    public String getUntilDateStr() {
        return untilDateStr;
    }

    public void setUntilDateStr(String untilDateStr) {
        this.untilDateStr = untilDateStr;
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

    public int getFirstTeamSupportCount() {
        return firstTeamSupportCount;
    }

    public void setFirstTeamSupportCount(int firstTeamSupportCount) {
        this.firstTeamSupportCount = firstTeamSupportCount;
    }

    public int getSecondTeamSupportCount() {
        return secondTeamSupportCount;
    }

    public void setSecondTeamSupportCount(int secondTeamSupportCount) {
        this.secondTeamSupportCount = secondTeamSupportCount;
    }

    public int getCountOfProofs() {
        return countOfProofs;
    }

    public void setCountOfProofs(int countOfProofs) {
        this.countOfProofs = countOfProofs;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getProofed() {
        return proofed;
    }

    public void setProofed(Boolean proofed) {
        this.proofed = proofed;
    }

    public Boolean getComeFromSelf() {
        return comeFromSelf;
    }

    public void setComeFromSelf(Boolean comeFromSelf) {
        this.comeFromSelf = comeFromSelf;
    }
}
