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
    private Date chlDate;
    @NotNull(message="You need to pass the untilDate parameter")
    private Date untilDate;

    private String untilDateStr;
    @NotNull(message="You need to pass the done parameter")
    @JsonProperty
    private Boolean done;
    @NotNull(message="You need to pass the updateDate parameter")
    private Date updateDate;

    private List<Like> likes;

    private int countOfLike;

    private List<Comment> comments;

    private int countOfComments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public int getCountOfLike() {
        return countOfLike;
    }

    public void setCountOfLike(int countOfLike) {
        this.countOfLike = countOfLike;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
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
}
