package org.chl.repository;

import org.chl.model.Challenge;
import org.chl.model.Member;
import org.chl.util.Constant;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface ChallengeRepository extends MongoRepository<Challenge, String> {
    // cal with new Sort(Direction.ASC/DESC,"order")
    @Query(" { 'challengerId' : ?0 }")
    Iterable<Challenge> findChallengesByMemberId(String memberId, Sort sort);

    @Query(" {'$or' : [{'challengerId' : {$in : ?0} }, {'type' : ?1} ]}")
    Iterable<Challenge> findChallenges(List<String> memberIdList, Constant.TYPE type, Sort sort);

    @Query(" { 'id' : {$in : ?0} }")
    Iterable<Challenge> findChallengesByChallengeIdList(List<String> challengeIdList, Sort sort);
}
