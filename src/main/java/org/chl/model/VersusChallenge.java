package org.chl.model;

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

    public List<VersusAttendance> getVersusAttendanceList() {
        return versusAttendanceList;
    }

    public void setVersusAttendanceList(List<VersusAttendance> versusAttendanceList) {
        this.versusAttendanceList = versusAttendanceList;
    }
}
