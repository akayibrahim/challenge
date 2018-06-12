package org.chl.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class VersusChallenge extends Challenge {
    private List<VersusAttendance> versusAttendanceList;

    private int visibility;

    @NotEmpty(message="You need to pass the score parameter")
    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public List<VersusAttendance> getVersusAttendanceList() {
        return versusAttendanceList;
    }

    public void setVersusAttendanceList(List<VersusAttendance> versusAttendanceList) {
        this.versusAttendanceList = versusAttendanceList;
    }
}
