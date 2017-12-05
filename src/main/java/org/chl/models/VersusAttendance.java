package org.chl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class VersusAttendance extends Attendance {
    @NotNull(message="You need to pass the isAccept parameter")
    @JsonProperty
    private Boolean accept;
    @NotNull(message="You need to pass the isFirstTeamMember parameter")
    @JsonProperty
    private Boolean firstTeamMember;
    @NotNull(message="You need to pass the isSecondTeamMember parameter")
    @JsonProperty
    private Boolean secondTeamMember;

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public Boolean getFirstTeamMember() {
        return firstTeamMember;
    }

    public void setFirstTeamMember(Boolean firstTeamMember) {
        this.firstTeamMember = firstTeamMember;
    }

    public Boolean getSecondTeamMember() {
        return secondTeamMember;
    }

    public void setSecondTeamMember(Boolean secondTeamMember) {
        this.secondTeamMember = secondTeamMember;
    }
}
