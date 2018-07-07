package org.chl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.chl.util.Constant;
import org.chl.util.Subject;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message="You need to pass the challengerFBId parameter")
    private String challengerFBId;
    @NotEmpty(message="You need to pass the name parameter")
    private String name;
    @NotEmpty(message="You need to pass the firstTeamCount parameter")
    private String firstTeamCount;
    @NotEmpty(message="You need to pass the secondTeamCount parameter")
    private String secondTeamCount;

    private String thinksAboutChallenge;
    @NotNull(message="You need to pass the subject parameter")
    @JsonProperty
    private String subject;

    private Date chlDate; // use bg
    @NotNull(message="You need to pass the untilDate parameter")
    private String untilDate; // use bg

    private Date dateOfUntil;

    private String untilDateStr;
    @NotNull(message="You need to pass the done parameter")
    @JsonProperty
    private Boolean done;

    private Date updateDate; // use bg

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

    private Constant.TYPE type;

    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public int getCountOfComments() {
        return countOfComments;
    }

    public void setCountOfComments(int countOfComments) {
        this.countOfComments = countOfComments;
    }

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

    public String getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(String untilDate) {
        this.untilDate = untilDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
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

    public Date getDateOfUntil() {
        return dateOfUntil;
    }

    public void setDateOfUntil(Date dateOfUntil) {
        this.dateOfUntil = dateOfUntil;
    }
}
