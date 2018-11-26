package org.chl.repository;

import org.chl.model.Parameter;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 25/11/2018.
 */
public interface ParameterRepository extends MongoRepository<Parameter, String> {
    Parameter findByKey(String key);
}
