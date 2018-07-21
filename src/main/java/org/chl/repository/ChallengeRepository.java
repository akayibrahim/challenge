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
    @Query(" { '$or': [ { 'challengerId' : ?0 }, { 'versusAttendanceList.memberId': ?0 }, { 'joinAttendanceList.memberId': ?0 } ]," +
            " 'deleted': {$in: [null, false]}, 'visibility': {$in: ?1}, 'active': {$in: ?2} }")
    Page<Challenge> findChallengesByMemberId(String memberId, List<Integer> visibilities, List<Boolean> active, Pageable pageable);

    @Query(" { 'challengerId' : ?0, 'deleted': {$in: [null, false]}, 'visibility': {$in: ?1}, 'active': {$in: ?2} }")
    List<Challenge> findChallengesByMemberId(String memberId, List<Integer> visibilities, List<Boolean> active, Sort sort);

    @Query("{ 'challengerId' : {$nin : ?0}, 'versusAttendanceList.memberId': {$nin : ?0}, 'joinAttendanceList.memberId': {$nin : ?0} , 'type' : 'PUBLIC', 'deleted': {$in: [null, false]}, 'active': true" +
            ", 'visibility': 1, '$or' : [ { 'dateOfUntil': {'$gte': ?2}, 'done': false  }, { 'done': true } ] }")
    Page<Challenge> findPublicChallenges(List<String> memberIdList, Constant.TYPE type, Date sysdate, Pageable pageable);

    @Query("{ '$or' : [ { '$or' : [ {'challengerId' : {$in : ?0}}, { 'versusAttendanceList.memberId': {$in : ?0} }, { 'joinAttendanceList.memberId': {$in : ?0} } ], 'deleted': {$in: [null, false]}, 'dateOfUntil': {'$gte': ?2}, 'done': false , 'active': true}, " +
                       "{ '$or' : [ {'challengerId' : {$in : ?0}}, { 'versusAttendanceList.memberId': {$in : ?0} }, { 'joinAttendanceList.memberId': {$in : ?0} } ], 'deleted': {$in: [null, false]}, 'done': true , 'active': true} ] }")
    Page<Challenge> findChallenges(List<String> memberIdList, Constant.TYPE type, Date sysdate, Pageable pageable);

    @Query(" { '$or': [ { 'versusAttendanceList.memberId': ?0 }, { 'joinAttendanceList.memberId': ?0 } ], 'join': {$in: [null, false]}, 'proof': {$in: [null, false]}, 'reject': {$in: [null, false]} }" )
    List<Challenge> findChallengeRequests(String memberId);

    @Query("{ '$or' : [ { 'subject' : ?0, 'type': ?1, 'deleted': {$in: [null, false]}, 'visibility': {$in: [1, 2]}, 'done': false, 'active': true, 'dateOfUntil': {'$gte': ?2} }, " +
            "           { 'subject' : ?0, 'type': ?1, 'deleted': {$in: [null, false]}, 'visibility': {$in: [1, 2]}, 'done': true, 'active': true } ] }")
    List<Challenge> findChallengesBySubjectAndType(String subject, Constant.TYPE type, Date sysdate, Sort sort);
}
