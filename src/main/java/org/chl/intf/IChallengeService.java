package org.chl.intf;

import org.chl.models.Challenge;
import org.chl.models.ChallengeAttendance;
import org.chl.models.Like;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface IChallengeService {
    Challenge addChallenge(Challenge chl);

    Iterable<Challenge> getChallenges();

    void likeChallange(Like like);

    Iterable<Like> getChallangeLikes(String challengeId);

    ChallengeAttendance joinOrInviteForAttendaceToChallenge(ChallengeAttendance chlAtt);

    List<ChallengeAttendance> getAttendanceOfChls();

    ChallengeAttendance getAcceptAttendanceMemberId(String memberId, String challengeId);

    List<ChallengeAttendance> getAttendancesOfChallenge(String challengeId);

    List<ChallengeAttendance> getJoinsToChallenge(String challengeId);

    void acceptOrRejectChl(ChallengeAttendance chlAtt);
}
