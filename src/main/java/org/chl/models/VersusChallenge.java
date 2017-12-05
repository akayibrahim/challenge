package org.chl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class VersusChallenge extends Challenge {
    private List<VersusAttendance> versusAttendanceList;
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

    public List<VersusAttendance> getVersusAttendanceList() {
        return versusAttendanceList;
    }

    public void setVersusAttendanceList(List<VersusAttendance> versusAttendanceList) {
        this.versusAttendanceList = versusAttendanceList;
    }
}
