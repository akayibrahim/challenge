package org.chl.model;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 11/28/2017.
 */
@Validated
@Document
public class TextComment extends Comment {
    @NotEmpty(message = "You need to pass the text parameter")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
