package org.chl.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class JoinAndProofChallenge extends Challenge {
    private List<JoinAttendance> joinAttendanceList;
    private Boolean toWorld;

    public Boolean getToWorld() {
        return toWorld;
    }

    public void setToWorld(Boolean toWorld) {
        this.toWorld = toWorld;
    }

    public List<JoinAttendance> getJoinAttendanceList() {
        return joinAttendanceList;
    }

    public void setJoinAttendanceList(List<JoinAttendance> joinAttendanceList) {
        this.joinAttendanceList = joinAttendanceList;
    }
}
