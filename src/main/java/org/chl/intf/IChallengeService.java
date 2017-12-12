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
    VersusChallenge addVersusChallenge(VersusChallenge versusChl);

    JoinAndProofChallenge addJoinChallenge(JoinAndProofChallenge joinChl);

    SelfChallenge addSelfChallenge(SelfChallenge selfChl);

    void updateProgressOrDoneForSelf(String challengeId, String score, Boolean done);

    void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore);

    Iterable<Challenge> getChallenges();

    Iterable<Challenge> getChallengesOfMember(@Valid @NotEmpty String memberId);

    void likeChallange(Like like);

    JoinAttendance joinToChallenge(JoinAttendance join);

    void acceptOrRejectChl(VersusAttendance chlAtt);

    void deleteChallenge(@Valid @NotEmpty String challengeId);
}
