package org.chl.repos;

import org.chl.models.ChallengeAttendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface ChallengeAttendaceRepository extends MongoRepository<ChallengeAttendance, BigInteger> {
    ChallengeAttendance findByMemberIdAndChallengeId(String  memberId, String challengeId);

    List<ChallengeAttendance> findByChallengeId(String challengeId);

    List<ChallengeAttendance> findByChallengeIdAndJoin(String challengeId, String join);

}
