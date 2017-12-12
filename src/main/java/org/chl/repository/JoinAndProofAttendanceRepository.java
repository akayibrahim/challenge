package org.chl.repository;

import org.chl.model.JoinAttendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface JoinAndProofAttendanceRepository extends MongoRepository<JoinAttendance, String> {
    JoinAttendance findByMemberIdAndChallengeId(String memberId, String challengeId);

    JoinAttendance findByChallengeIdAndMemberId(String challengeId, String memberId);

    List<JoinAttendance> findByChallengeId(String challengeId);
}
