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
    void save(Challenge challenge);

    Challenge getChallengeById(String challangeId);

    Iterable<Challenge> getChallenges(@Valid @NotEmpty String memberId, int page);

    Iterable<Challenge> getChallengesOfMember(@Valid @NotEmpty String memberId, int page);

    Iterable<Challenge> getChallengesOfFriend(@Valid @NotEmpty String memberId, String friendMemberId, int page);

    Iterable<Challenge> getExplorerChallenges(@Valid @NotEmpty String memberId, String challengeId, Boolean addSimilarChallenges, int page);

    Iterable<Trends> getTrendChallenges(String memberId, String subjectSearchKey, int page);

    Challenge addVersusChallenge(Challenge challenge);

    Challenge addJoinChallenge(Challenge challenge);

    Challenge addSelfChallenge(Challenge challenge);

    Iterable<Subjects> getSubjects(boolean isSelf);

    Iterable<TextComment> getComments(String challengeId, int page);

    void updateProgressOrDoneForSelf(String challengeId, String score, Boolean done);

    void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore, Boolean done);

    Iterable<Challenge> getAllChallenges();

    void supportChallange(Support support);

    void joinToChallenge(JoinToChallenge joinToChallenge);

    void acceptOrRejectChl(VersusAttendance chlAtt);

    List<ChallengeRequest> getChallengeRequests(String memberId);

    void deleteChallenge(@Valid @NotEmpty String challengeId);

    void commentAsTextToChallange(TextComment textComment);

    String getChallengeSizeOfMember(String memberId);

    List<Support> getSupportList(String challengeId, String memberId, String supportedMemberId, Boolean firstTeam);

    List<Attendance> getChallengerList(String challengeId, String memberId, Boolean firstTeam);
}
