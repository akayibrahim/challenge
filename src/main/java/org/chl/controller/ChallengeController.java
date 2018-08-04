package org.chl.controller;

import org.chl.model.*;
import org.chl.model.Error;
import org.chl.service.ActivityService;
import org.chl.service.ChallengeService;
import org.chl.service.ErrorService;
import org.chl.service.ProofService;
import org.chl.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeService chlService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ErrorService errorService;
    @Autowired
    ProofService proofService;

    @Transactional
    @RequestMapping(value = "/getChallenges")
    public Iterable<Challenge> getChallenges(String memberId, int page) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getChallenges(memberId, page);
            return challenges;
        } catch (Exception e) {
            logError(null, memberId, "getChallenges", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getChallengesOfMember")
    public Iterable<Challenge> getChallengesOfMember(String memberId, int page) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getChallengesOfMember(memberId, page);
            return challenges;
        } catch (Exception e) {
            logError(null, memberId, "getChallengesOfMember", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getChallengesOfFriend")
    public Iterable<Challenge> getChallengesOfFriend(String memberId, String friendMemberId, int page) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getChallengesOfFriend(memberId, friendMemberId, page);
            return challenges;
        } catch (Exception e) {
            logError(null, memberId, "getChallengesOfFriend", e, "memberId=" + memberId + "&friendMemberId=" + friendMemberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getExplorerChallenges")
    public Iterable<Challenge> getExplorerChallenges(String memberId, String challengeId, Boolean addSimilarChallenges, int page) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getExplorerChallenges(memberId, challengeId, addSimilarChallenges, page);
            return challenges;
        } catch (Exception e) {
            logError(challengeId, memberId, "getExplorerChallenges", e, "memberId=" + memberId + "&challengeId=" + challengeId + "&addSimilarChallanges=" + addSimilarChallenges);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getTrendChallenges")
    public Iterable<Trends> getTrendsChallenges(String memberId, String subjectSearchKey, int page) throws Exception {
        try {
            Iterable<Trends> trends = chlService.getTrendChallenges(memberId, subjectSearchKey, page);
            return trends;
        } catch (Exception e) {
            logError(null, memberId, "getTrendChallenges", e, "memberId=" + memberId + "&subjectSearchKey=" + subjectSearchKey);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/addJoinChallenge")
    public String addJoinChallenge(@Valid @RequestBody Challenge joinChl) throws Exception {
        try {
            Challenge challenge = chlService.addJoinChallenge(joinChl);
            return challenge.getId();
        } catch (Exception e) {
            logError(null, joinChl.getChallengerId(), "addJoinChallenge", e, "memberId=" + joinChl.getChallengerId());
            // TODO inputs
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/addJoinChallengeOld")
    public String addJoinChallengeOld(@Valid @RequestBody MultipartFile file, String challengerId, ArrayList<JoinAttendance> joinAttendanceList
    , String challengerFBId, String name, String thinksAboutChallenge, String subject, Boolean done, String firstTeamCount
    , String secondTeamCount, Constant.TYPE type, Integer visibility, String challengeTime, String untilDate, Boolean proofed) throws Exception {
        try {
            JoinAndProofChallenge joinChl = new JoinAndProofChallenge();
            joinChl.setChallengerId(challengerId);
            joinChl.setName(name);
            joinChl.setChallengerFBId(challengerFBId);
            if (StringUtils.hasText(thinksAboutChallenge))
                joinChl.setThinksAboutChallenge(thinksAboutChallenge);
            joinChl.setSubject(subject);
            joinChl.setDone(done);
            joinChl.setFirstTeamCount(firstTeamCount);
            joinChl.setSecondTeamCount(secondTeamCount);
            joinChl.setType(type);
            joinChl.setVisibility(visibility);
            joinChl.setChallengeTime(challengeTime);
            joinChl.setUntilDate(untilDate);
            joinChl.setProofed(proofed);
            joinChl.setJoinAttendanceList(joinAttendanceList);
            Challenge challenge = chlService.addJoinChallenge(joinChl);
            proofService.uploadImage(file, challenge.getId(), challengerId);
            return challenge.getId();
        } catch (Exception e) {
            logError(null, challengerId, "addJoinChallenge", e, "memberId=" + challengerId);
            // TODO inputs
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/addVersusChallenge")
    public String addVersusChallenge(@Valid @RequestBody Challenge challenge) throws Exception {
        try {
            Challenge chl = chlService.addVersusChallenge(challenge);
            return chl.getId();
        } catch (Exception e) {
            logError(null, challenge.getChallengerId(), "addVersusChallenge", e, "memberId=" + challenge.getChallengerId());
            // TODO inputs
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/addSelfChallenge")
    public String addSelfChallenge(@Valid @RequestBody Challenge challenge) throws Exception {
        try {
            Challenge chl = chlService.addSelfChallenge(challenge);
            return chl.getId();
        } catch (Exception e) {
            logError(null, challenge.getChallengerId(), "addSelfChallenge", e, "memberId=" + challenge.getChallengerId());
            // TODO inputs
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getSubjects")
    public Iterable<Subjects> getSubjects() throws Exception {
        try {
            return chlService.getSubjects(false);
        } catch (Exception e) {
            logError(null, null, "getSubjects", e, null);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getSelfSubjects")
    public Iterable<Subjects> getSelfSubjects() throws Exception {
        try {
            return chlService.getSubjects(true);
        } catch (Exception e) {
            logError(null, null, "getSelfSubjects", e, null);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/commentToChallange")
    public void commentToChallange(@Valid @RequestBody TextComment textComment) throws Exception {
        try {
            chlService.commentAsTextToChallange(textComment);
        } catch (Exception e) {
            logError(textComment.getChallengeId(), textComment.getMemberId(), "commentToChallange", e, "memberId=" + textComment.getMemberId()
            + "&challengeId=" + textComment.getChallengeId() + "&comment=" + textComment.getComment());
        }
    }

    @Transactional
    @RequestMapping(value = "/getComments")
    public Iterable<TextComment> getComments(String challengeId, int page) throws Exception {
        try {
            return chlService.getComments(challengeId, page);
        } catch (Exception e) {
            logError(challengeId, null, "getComments", e, "challengeId=" + challengeId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/supportChallenge")
    public void likeChallenge(@Valid @RequestBody Support support) throws Exception {
        try {
            support.setDate(new Date());
            chlService.supportChallange(support);
        } catch (Exception e) {
            logError(support.getChallengeId(), support.getMemberId(), "supportChallenge", e, "memberId=" + support.getMemberId() + "&challengeId=" + support.getChallengeId()
            + "&firstTeamSupport=" + support.getSupportFirstTeam() + "&secondTeamSupport=" + support.getSupportSecondTeam());
        }
    }

    @Transactional
    @RequestMapping(value = "/joinToChallenge")
    public void joinToChallenge(@Valid @RequestBody JoinToChallenge joinToChallenge) throws Exception {
        try {
            chlService.joinToChallenge(joinToChallenge);
        } catch (Exception e) {
            logError(joinToChallenge.getChallengeId(), joinToChallenge.getMemberId(), "joinToChallenge", e, "memberId=" + joinToChallenge.getMemberId() + "&challengeId=" + joinToChallenge.getChallengeId()
                    + "&join=" + joinToChallenge.getJoin());
        }
    }

    @Transactional
    @RequestMapping(value = "/updateProgressOrDoneForSelf")
    public void updateProgressOrDoneForSelf(String challengeId, Boolean homeWin, String result, String goal, Boolean done) throws Exception {
        try {
            chlService.updateProgressOrDoneForSelf(challengeId, homeWin, result, goal, done);
        } catch (Exception e) {
            logError(challengeId, null, "updateProgressOrDoneForSelf", e, "challengeId=" + challengeId + "&result=" + result
                    + "&done=" + done);
        }
    }

    @Transactional
    @RequestMapping(value = "/updateResultsOfVersus")
    public void updateResultsOfVersus(String challengeId, Boolean homeWin, Boolean awayWin, String firstTeamScore, String secondTeamScore, Boolean done) throws Exception {
        try {
            chlService.updateResultsOfVersus(challengeId, homeWin, awayWin, firstTeamScore, secondTeamScore, done);
        } catch (Exception e) {
            logError(challengeId, null, "updateResultsOfVersus", e, "challengeId=" + challengeId + "&firstTeamScore=" + firstTeamScore
                    + "&secondTeamScore=" + secondTeamScore);
        }
    }

    @Transactional
    @RequestMapping(value = "/deleteChallenge")
    public void deleteChallenge(String challengeId) throws Exception {
        try {
            chlService.deleteChallenge(challengeId);
        } catch (Exception e) {
            logError(challengeId, null, "deleteChallenge", e, "challengeId=" + challengeId);
        }
    }

    @Transactional
    @RequestMapping(value = "/acceptOrRejectChl")
    public void acceptOrRejectChl(@Valid @RequestBody VersusAttendance chlAtt) throws Exception {
        try {
            chlService.acceptOrRejectChl(chlAtt);
        } catch (Exception e) {
            logError(chlAtt.getChallengeId(), chlAtt.getMemberId(), "acceptOrRejectChl", e, "challengeId=" + chlAtt.getChallengeId());
            // TODO inputs
        }

    }

    @Transactional
    @RequestMapping(value = "/getActivities")
    public List<Activity> getActivities(String toMemberId, int page) throws Exception {
        try {
            return activityService.getActivities(toMemberId, page);
        } catch (Exception e) {
            logError(null, toMemberId, "getActivities", e, "toMemberId=" + toMemberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getChallengeRequest")
    public List<ChallengeRequest> getChallengeRequest(String memberId) throws Exception {
        try {
            return chlService.getChallengeRequests(memberId);
        } catch (Exception e) {
            logError(null, memberId, "getChallengeRequest", e, "memberId=" + memberId);
        }
        return null;
    }

    private void logError(String challengeId, String memberId, String serviceURL, java.lang.Exception e, String inputs) throws Exception {
        errorService.logError(challengeId,memberId,serviceURL,e,inputs);
    }

    @Transactional
    @RequestMapping(value = "/getChallengeSizeOfMember")
    public String getChallengeSizeOfMember(String memberId) throws Exception {
        try {
            return chlService.getChallengeSizeOfMember(memberId);
        } catch (Exception e) {
            logError(null, memberId, "getChallengeSizeOfMember", e, "memberId=" + memberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getSupportList")
    public List<Support> getSupportList(String challengeId, String memberId, String supportedMemberId, Boolean firstTeam) throws Exception {
        try {
            return chlService.getSupportList(challengeId, memberId, supportedMemberId, firstTeam);
        } catch (Exception e) {
            logError(challengeId, supportedMemberId, "getSupportList", e, "memberId=" + supportedMemberId);
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/getChallengerList")
    public List<Attendance> getChallengerList(String challengeId, String memberId, Boolean firstTeam) throws Exception {
        try {
            return chlService.getChallengerList(challengeId, memberId, firstTeam);
        } catch (Exception e) {
            logError(challengeId, null, "getChallengerList", e, "challengeId=" + challengeId);
        }
        return null;
    }
}
