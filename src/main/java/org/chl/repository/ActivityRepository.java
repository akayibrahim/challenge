package org.chl.repository;

import org.chl.model.Activity;
import org.chl.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 06/28/2018.
 */
public interface ActivityRepository extends MongoRepository<Activity, String> {
    @Query(" { 'toMemberId' : ?0, 'type' : {$nin: ['FOLLOWING']} }")
    Page<Activity> findActivityByToMemberId(String toMemberId, Pageable pageable);

    @Query(" { 'challengeId' : ?0, 'toMemberId' : ?1, 'fromMemberId' : ?2, 'type' : ?3 }")
    Activity findExistActivity(String challengeId, String toMemberId, String fromMemberId, Constant.ACTIVITY type);
}
