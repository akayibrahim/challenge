package org.chl.model;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class SelfChallenge extends Challenge {
    @NotEmpty(message="You need to pass the goal parameter")
    private String goal;

    private int visibility;

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
