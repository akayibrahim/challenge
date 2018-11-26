package org.chl.service;

import org.chl.intf.IErrorService;
import org.chl.model.Error;
import org.chl.repository.ErrorRepository;
import org.chl.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by ibrahim on 07/17/2018.
 */
@Service
public class ErrorService implements IErrorService {
    @Autowired
    private ErrorRepository errorRepository;

    @Transactional
    @Override
    public void logError(String challengeId, String memberId, String serviceURL, java.lang.Exception e, String inputs) throws java.lang.Exception {
        Error error = new Error();
        error.setFe(false);
        error.setChallengeId(challengeId);
        error.setMemberId(memberId);
        error.setServiceURL(serviceURL);
        error.setErrorMessage(e.toString());
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        error.setStack(sw.toString());
        error.setInputs(inputs);
        error.setInsertTime(DateUtil.getCurrentDatePlusThreeHour());
        errorRepository.save(error);
        System.out.println(sw.toString());
        throw new java.lang.Exception(e.toString());
    }

    @Override
    public void save(Error error) {
        error.setFe(true);
        error.setInsertTime(DateUtil.getCurrentDatePlusThreeHour());
        errorRepository.save(error);
    }
}
