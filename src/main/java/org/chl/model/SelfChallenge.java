package org.chl.model;

import javax.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
@Document
public class SelfChallenge extends Challenge {

}
