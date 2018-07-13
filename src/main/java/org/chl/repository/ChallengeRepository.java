package org.chl.repository;

import org.chl.model.Challenge;
import org.chl.util.Constant;
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
    @Query(" { 'challengerId' : ?0, 'deleted': {$in: [null, false]}, 'visibility': {$in: ?1} }")
    Iterable<Challenge> findChallengesByMemberId(String memberId, List<String> visibilities, Sort sort);

    @Query("{ '$or' : [ { '$or' : [{'challengerId' : {$in : ?0} }, {'type' : ?1} ], 'deleted': {$in: [null, false]}, 'dateOfUntil': {'$gte': ?2}, 'done': false , 'active': true}, " +
                       "{ '$or' : [{'challengerId' : {$in : ?0} }, {'type' : ?1} ], 'deleted': {$in: [null, false]}, 'done': true , 'active': true} ] }")
    Iterable<Challenge> findChallenges(List<String> memberIdList, Constant.TYPE type, Date sysdate, Sort sort);

    @Query(" { 'id' : {$in : ?0} , 'deleted': {$in: [null, false]}, 'visibility': {$in: [null, 1, 2]} }")
    Iterable<Challenge> findChallengesByChallengeIdList(List<String> challengeIdList, Sort sort);

    @Query("{ '$or' : [ { 'subject' : ?0 , type: ?1,'deleted': {$in: [null, false]}, 'visibility': {$in: [null, 1, 2]}, 'dateOfUntil': {'$gte': ?2}, 'done': false, 'active': true }, " +
            "           { 'subject' : ?0 , type: ?1,'deleted': {$in: [null, false]}, 'visibility': {$in: [null, 1, 2]}, 'done': true, 'active': true } ] }")
    Iterable<Challenge> findChallengesBySubjectAndType(String subject, Constant.TYPE type, Date sysdate, Sort sort);
}
