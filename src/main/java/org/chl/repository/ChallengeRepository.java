package org.chl.repository;

import org.chl.model.Challenge;
import org.chl.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface ChallengeRepository extends MongoRepository<Challenge, String> {
    // cal with new Sort(Direction.ASC/DESC,"order")
    @Query(" { 'challengerId' : ?0, 'deleted': {$in: [null, false]}, 'visibility': {$in: ?1}, 'active': {$in: ?2} }")
    List<Challenge> findChallengesByMemberId(String memberId, List<Integer> visibilities, List<Boolean> active, Sort sort);

    @Query("{ '$or' : [ { '$or' : [{'challengerId' : {$in : ?0}, 'type' : 'SELF' }, {'challengerId' : {$nin : ?0}, 'type' : ?1} ], 'deleted': {$in: [null, false]}, 'dateOfUntil': {'$gte': ?2}, 'done': false , 'active': true}, " +
                       "{ '$or' : [{'challengerId' : {$in : ?0}, 'type' : 'SELF' }, {'challengerId' : {$nin : ?0}, 'type' : ?1} ], 'deleted': {$in: [null, false]}, 'done': true , 'active': true} ] }")
    List<Challenge> findChallenges(List<String> memberIdList, Constant.TYPE type, Date sysdate, Sort sort);

    @Query("{ '$or' : [ { '$or' : [{'challengerId' : {$in : ?0}, 'type' : 'SELF' }, {'challengerId' : {$nin : ?0}, 'type' : ?1} ], 'deleted': {$in: [null, false]}, 'dateOfUntil': {'$gte': ?2}, 'done': false , 'active': true}, " +
            "{ '$or' : [{'challengerId' : {$in : ?0}, 'type' : 'SELF' }, {'challengerId' : {$nin : ?0}, 'type' : ?1} ], 'deleted': {$in: [null, false]}, 'done': true , 'active': true} ] }")
    Page<Challenge> findChallenges(List<String> memberIdList, Constant.TYPE type, Date sysdate, Pageable pageable);

    @Query(" { 'id' : {$in : ?0} , 'deleted': {$in: [null, false]}, 'visibility': {$in: [1, 2]}, 'active': {$in: ?1} }")
    List<Challenge> findChallengesByChallengeIdList(List<String> challengeIdList, List<Boolean> active, Sort sort);

    @Query("{ '$or' : [ { 'subject' : ?0, 'type': ?1, 'deleted': {$in: [null, false]}, 'visibility': {$in: [1, 2]}, 'done': false, 'active': true, 'dateOfUntil': {'$gte': ?2} }, " +
            "           { 'subject' : ?0, 'type': ?1, 'deleted': {$in: [null, false]}, 'visibility': {$in: [1, 2]}, 'done': true, 'active': true } ] }")
    List<Challenge> findChallengesBySubjectAndType(String subject, Constant.TYPE type, Date sysdate, Sort sort);
}
