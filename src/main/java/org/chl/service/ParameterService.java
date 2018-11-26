package org.chl.service;

import org.chl.intf.IParameterService;
import org.chl.model.Parameter;
import org.chl.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ibrahim on 06/28/2018.
 */
@Service
public class ParameterService implements IParameterService {

    @Autowired
    ParameterRepository parameterRepository;

    @Override
    public Parameter findByKey(String key) {
        return parameterRepository.findByKey(key);
    }

    @Override
    public void addParemeter(String key, String value) {
        Parameter parameter = new Parameter();
        parameter.setKey(key);
        parameter.setValue(value);
        parameterRepository.save(parameter);
    }

    @Override
    public void removeParameter(String key) {
        Parameter parameter = findByKey(key);
        parameterRepository.delete(parameter);
    }
}
