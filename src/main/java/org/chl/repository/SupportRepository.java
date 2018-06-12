package org.chl.repository;

import org.chl.model.Support;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface SupportRepository extends MongoRepository<Support, String> {

    //    @Query("SELECT s FROM SeqTb s where s.analysisId = :analysisId")
    List<Support> findByChallengeIdAndSupportFirstTeam(String challengeId, Boolean supportFirstTeam);

    List<Support> findByChallengeIdAndSupportSecondTeam(String challengeId, Boolean supportSecondTeam);

    Support findByMemberId(String memberId);

    Support findByMemberIdAndChallengeId(String memberId, String challengeId);
}
