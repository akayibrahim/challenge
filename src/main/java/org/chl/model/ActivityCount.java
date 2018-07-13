package org.chl.model;

import org.chl.util.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by ibrahim on 07/13/2018.
 */
@Validated
@Document
public class ActivityCount {
    @Id
    private String id;
    @NotEmpty(message = "You need to pass the memberId parameter")
    private String memberId;
    @NotEmpty(message = "You need to pass the count parameter")
    private String count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
