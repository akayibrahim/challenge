package org.chl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class JoinAndProofChallenge extends Challenge {
    private List<JoinAttendance> joinAttendanceList;

    public List<JoinAttendance> getJoinAttendanceList() {
        return joinAttendanceList;
    }

    public void setJoinAttendanceList(List<JoinAttendance> joinAttendanceList) {
        this.joinAttendanceList = joinAttendanceList;
    }
}
