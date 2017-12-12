package org.chl.repository;

import org.chl.model.VersusAttendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface VersusAttendanceRepository extends MongoRepository<VersusAttendance, String> {
    VersusAttendance findByMemberIdAndChallengeId(String  memberId, String challengeId);

    List<VersusAttendance> findByChallengeId(String challengeId);
}
