package org.chl.model;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 11/28/2017.
 */
@Validated
@Document
public class TextComment extends Comment {
    @NotEmpty(message = "You need to pass the text parameter")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
