package org.chl.model;

import org.chl.util.Constant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by ibrahim on 15/11/2018.
 */
@Validated
@Document
public class Parameter {
    @Id
    private String id;
    @NotEmpty(message="You need to pass the key parameter")
    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
