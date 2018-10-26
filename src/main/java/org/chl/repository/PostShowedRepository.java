package org.chl.repository;

import org.chl.model.PostShowed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by ibrahim on 18/07/2018
 */
public interface PostShowedRepository extends MongoRepository<PostShowed, String> {
    List<PostShowed> findByMemberIdAndChallengeIdAndChallengerId(String memberId, String challengeId, String challengerId);

    List<PostShowed> findByMemberIdAndChallengeId(String memberId, String challengeId);

    List<PostShowed> findByMemberId(String memberId);
}
