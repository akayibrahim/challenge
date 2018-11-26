package org.chl.intf;

import org.chl.model.Parameter;

/**
 * Created by ibrahim on 25/11/2018.
 */
public interface IParameterService {
    Parameter findByKey(String key);

    void addParemeter(String key, String value);

    void removeParameter(String key);
}
