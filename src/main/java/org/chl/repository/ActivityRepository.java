package org.chl.repository;

import org.chl.model.Activity;
import org.chl.util.Constant;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 06/28/2018.
 */
public interface ActivityRepository extends MongoRepository<Activity, String> {
    List<Activity> findActivityByToMemberId(String toMemberId, Sort sort);

    @Query(" { 'challengeId' : ?0, 'toMemberId' : ?1, 'fromMemberId' : ?2, 'type' : ?3 }")
    Activity findExistActivity(String challengeId, String toMemberId, String fromMemberId, Constant.ACTIVITY type);
}
