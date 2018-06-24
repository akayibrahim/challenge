package org.chl.model;

import org.chl.util.Subject;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 06/08/2018.
 */
@Validated
@Document
public class TrendSubject {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the subject parameter")
    private Subject subject;
    @NotEmpty(message="You need to pass the popularity parameter")
    private Integer popularity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }
}
