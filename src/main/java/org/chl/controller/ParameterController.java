package org.chl.controller;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.chl.model.Parameter;
import org.chl.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ParameterController {
    @Autowired
    ParameterService parameterService;

    @RequestMapping("/getParameterValue")
    public String getParameterValue(String key) {
        Parameter parameter = parameterService.findByKey(key);
        return parameter != null ? parameter.getValue() : "0";
    }

    @RequestMapping("/addParameter")
    public void addParameter(String key, String value) {
        parameterService.addParemeter(key, value);
    }

    @RequestMapping("/removeParameter")
    public void removeParameter(String key) {
        parameterService.removeParameter(key);
    }

    @RequestMapping("/updateParameter")
    public void updateParameter(String key, String value) {
        removeParameter(key);
        addParameter(key, value);
    }
}
