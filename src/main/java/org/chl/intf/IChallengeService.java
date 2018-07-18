package org.chl.intf;

import org.chl.model.*;
import javax.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Validated
public interface IChallengeService {
    Challenge getChallengeById(String challangeId);

    Iterable<Challenge> getChallenges(@Valid @NotEmpty String memberId, int page);

    Iterable<Challenge> getChallengesOfMember(@Valid @NotEmpty String memberId);

    Iterable<Challenge> getChallengesOfFriend(@Valid @NotEmpty String memberId, String friendMemberId);

    Iterable<Challenge> getExplorerChallenges(@Valid @NotEmpty String memberId, String challengeId, Boolean addSimilarChallanges);

    Iterable<Trends> getTrendChallenges(String memberId, String subjectSearchKey);

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

    List<ChallengeRequest> getChallengeRequests(String memberId);

    void deleteChallenge(@Valid @NotEmpty String challengeId);

    void commentAsTextToChallange(TextComment textComment);
}
