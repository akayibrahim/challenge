package org.chl.model;

import org.chl.util.Constant;
import org.chl.util.Subject;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 06/08/2018.
 */
@Validated
@Document
public class TrendChallenge {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the challengeId parameter")
    private String challengeId;
    @NotEmpty(message="You need to pass the popularity parameter")
    private Integer popularity;

    private Constant.TYPE type;

    private Constant.POPULARITY popularityType;

    public Constant.POPULARITY getPopularityType() {
        return popularityType;
    }

    public void setPopularityType(Constant.POPULARITY popularityType) {
        this.popularityType = popularityType;
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

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Constant.TYPE getType() {
        return type;
    }

    public void setType(Constant.TYPE type) {
        this.type = type;
    }
}
