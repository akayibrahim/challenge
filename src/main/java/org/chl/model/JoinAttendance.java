package org.chl.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class JoinAttendance extends Attendance {
    @NotNull(message="You need to pass the join parameter")
    @JsonProperty
    private Boolean join;
    @NotNull(message="You need to pass the proof parameter")
    @JsonProperty
    private Boolean proof;
    @NotNull(message="You need to pass the challenger parameter")
    @JsonProperty
    private Boolean challenger;

    public Boolean getJoin() {
        return join;
    }

    public void setJoin(Boolean join) {
        this.join = join;
    }

    public Boolean getProof() {
        return proof;
    }

    public void setProof(Boolean proof) {
        this.proof = proof;
    }

    public Boolean getChallenger() {
        return challenger;
    }

    public void setChallenger(Boolean challenger) {
        this.challenger = challenger;
    }
}
