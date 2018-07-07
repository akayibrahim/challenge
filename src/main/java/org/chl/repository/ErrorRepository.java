package org.chl.repository;

import org.chl.model.Error;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 07/06/2018.
 */
public interface ErrorRepository extends MongoRepository<Error, String> {

}
