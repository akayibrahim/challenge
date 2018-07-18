package org.chl.repository;

import org.chl.model.JoinAttendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface JoinAndProofAttendanceRepository extends MongoRepository<JoinAttendance, String> {
    JoinAttendance findByMemberIdAndChallengeId(String memberId, String challengeId);

    JoinAttendance findByChallengeIdAndMemberId(String challengeId, String memberId);

    List<JoinAttendance> findByChallengeId(String challengeId);

    @Query(" {'memberId' : ?0, 'join': false, 'proof': false, 'reject': false }" )
    List<JoinAttendance> findChallengeRequests(String memberId);

    List<JoinAttendance> findByChallengeIdAndProof(String challengeId, Boolean proof);

    @Query(" {'memberId' : ?0 }")
    List<JoinAttendance> findByMemberIdInAttendace(String memberId);

    @Query(" {'memberId' :  {$in : ?0}}")
    List<JoinAttendance> findByFriendListInAttendace(List<String> friendList);

    @Query(" {'memberId' :  {$in : ?0}}")
    Page<JoinAttendance> findByFriendListInAttendaceAsPageble(List<String> friendList, Pageable pageable);
}
