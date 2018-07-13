package org.chl.repository;

import org.chl.model.ActivityCount;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by ibrahim on 07/13/2018.
 */
public interface ActivityCountRepository extends MongoRepository<ActivityCount, String> {
    ActivityCount findByMemberId(String memberId);
}
