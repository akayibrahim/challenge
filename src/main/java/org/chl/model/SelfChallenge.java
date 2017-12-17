package org.chl.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class SelfChallenge extends Challenge {
    private String score;
    @NotEmpty(message="You need to pass the goal parameter")
    private String goal;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
