package org.chl.intf;

import org.chl.model.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
public interface IChallengeService {
    Iterable<Challenge> getChallenges(@Valid @NotEmpty String memberId);

    Iterable<Challenge> getChallengesOfMember(@Valid @NotEmpty String memberId);

    Iterable<Challenge> getExplorerChallenges(@Valid @NotEmpty String memberId, String challengeId, Boolean addSimilarChallanges);

    Iterable<Trends> getTrendChallenges(String memberId);

    VersusChallenge addVersusChallenge(VersusChallenge versusChl);

    JoinAndProofChallenge addJoinChallenge(JoinAndProofChallenge joinChl);

    SelfChallenge addSelfChallenge(SelfChallenge selfChl);

    Iterable<Subjects> getSubjects(boolean isSelf);

    Iterable<TextComment> getComments(String challengeId);

    void updateProgressOrDoneForSelf(String challengeId, String score, Boolean done);

    void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore);

    Iterable<Challenge> getAllChallenges();

    void supportChallange(Support support);

    void joinToChallenge(JoinToChallenge joinToChallenge);

    void acceptOrRejectChl(VersusAttendance chlAtt);

    void deleteChallenge(@Valid @NotEmpty String challengeId);

    void commentAsTextToChallange(TextComment textComment);
}
