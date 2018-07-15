package org.chl.repository;

import org.chl.model.VersusAttendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface VersusAttendanceRepository extends MongoRepository<VersusAttendance, String> {
    VersusAttendance findByMemberIdAndChallengeId(String  memberId, String challengeId);

    List<VersusAttendance> findByChallengeId(String challengeId);

    @Query(" {'memberId' : ?0 }" )
    List<VersusAttendance> findByMemberIdInAttendace(String memberId);

    @Query(" {'memberId' : ?0, 'accept': {$in: [null, false]}, 'reject': false }" )
    List<VersusAttendance> findChallengeRequests(String memberId);

    @Query(" {'memberId' :  {$in : ?0}}")
    List<VersusAttendance> findByFriendListInAttendace(List<String> friendList);
}
