package org.chl.intf;

import org.chl.model.Error;
import org.springframework.validation.annotation.Validated;

/**
 * Created by ibrahim on 07/17/2018.
 */
@Validated
public interface IErrorService {
    void logError(String challengeId, String memberId, String serviceURL, Exception e, String inputs) throws Exception;

    void save(Error error);
}

