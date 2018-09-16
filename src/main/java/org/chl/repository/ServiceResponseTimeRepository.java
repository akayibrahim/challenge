package org.chl.repository;

import org.chl.model.ServiceResponseTime;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 16/09/2018.
 */
public interface ServiceResponseTimeRepository extends MongoRepository<ServiceResponseTime, String> {
}
