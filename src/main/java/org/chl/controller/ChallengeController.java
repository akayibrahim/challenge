package org.chl.controller;

import org.chl.model.*;
import org.chl.model.Error;
import org.chl.repository.ErrorRepository;
import org.chl.service.ActivityService;
import org.chl.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeService chlService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ErrorRepository errorRepository;

    @RequestMapping(value = "/getChallenges")
    public Iterable<Challenge> getChallenges(String memberId) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getChallenges(memberId);
            return challenges;
        } catch (Exception e) {
            logError(null, memberId, "getChallenges", e, "memberId=" + memberId);
        }
        return null;
    }

    @RequestMapping(value = "/getChallengesOfMember")
    public Iterable<Challenge> getChallengesOfMember(String memberId) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getChallengesOfMember(memberId);
            return challenges;
        } catch (Exception e) {
            logError(null, memberId, "getChallengesOfMember", e, "memberId=" + memberId);
        }
        return null;
    }

    @RequestMapping(value = "/getChallengesOfFriend")
    public Iterable<Challenge> getChallengesOfFriend(String memberId, String friendMemberId) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getChallengesOfFriend(memberId, friendMemberId);
            return challenges;
        } catch (Exception e) {
            logError(null, memberId, "getChallengesOfFriend", e, "memberId=" + memberId + "&friendMemberId=" + friendMemberId);
        }
        return null;
    }

    @RequestMapping(value = "/getExplorerChallenges")
    public Iterable<Challenge> getExplorerChallenges(String memberId, String challengeId, Boolean addSimilarChallanges) throws Exception {
        try {
            Iterable<Challenge> challenges = chlService.getExplorerChallenges(memberId, challengeId, addSimilarChallanges);
            return challenges;
        } catch (Exception e) {
            logError(challengeId, memberId, "getExplorerChallenges", e, "memberId=" + memberId + "&challengeId=" + challengeId + "&addSimilarChallanges=" + addSimilarChallanges);
        }
        return null;
    }

    @RequestMapping(value = "/getTrendChallenges")
    public Iterable<Trends> getTrendsChallenges(String memberId, String subjectSearchKey) throws Exception {
        try {
            Iterable<Trends> trends = chlService.getTrendChallenges(memberId, subjectSearchKey);
            return trends;
        } catch (Exception e) {
            logError(null, memberId, "getTrendChallenges", e, "memberId=" + memberId + "&subjectSearchKey=" + subjectSearchKey);
        }
        return null;
    }

    @RequestMapping(value = "/addJoinChallenge")
    public String addJoinChallenge(@Valid @RequestBody JoinAndProofChallenge joinChl) throws Exception {
        try {
            Challenge challenge = chlService.addJoinChallenge(joinChl);
            return challenge.getId();
        } catch (Exception e) {
            logError(null, joinChl.getChallengerId(), "addJoinChallenge", e, "memberId=" + joinChl.getChallengerId());
            // TODO inputs
        }
        return null;
    }

    @RequestMapping(value = "/addVersusChallenge")
    public String addVersusChallenge(@Valid @RequestBody VersusChallenge versusChl) throws Exception {
        try {
            Challenge challenge = chlService.addVersusChallenge(versusChl);
            return challenge.getId();
        } catch (Exception e) {
            logError(null, versusChl.getChallengerId(), "addVersusChallenge", e, "memberId=" + versusChl.getChallengerId());
            // TODO inputs
        }
        return null;
    }

    @RequestMapping(value = "/addSelfChallenge")
    public String addSelfChallenge(@Valid @RequestBody SelfChallenge selfChl) throws Exception {
        try {
            Challenge challenge = chlService.addSelfChallenge(selfChl);
            return challenge.getId();
        } catch (Exception e) {
            logError(null, selfChl.getChallengerId(), "addSelfChallenge", e, "memberId=" + selfChl.getChallengerId());
            // TODO inputs
        }
        return null;
    }

    @RequestMapping(value = "/getSubjects")
    public Iterable<Subjects> getSubjects() throws Exception {
        try {
            return chlService.getSubjects(false);
        } catch (Exception e) {
            logError(null, null, "getSubjects", e, null);
        }
        return null;
    }

    @RequestMapping(value = "/getSelfSubjects")
    public Iterable<Subjects> getSelfSubjects() throws Exception {
        try {
            return chlService.getSubjects(true);
        } catch (Exception e) {
            logError(null, null, "getSelfSubjects", e, null);
        }
        return null;
    }

    @RequestMapping(value = "/commentToChallange")
    public void commentToChallange(@Valid @RequestBody TextComment textComment) throws Exception {
        try {
            chlService.commentAsTextToChallange(textComment);
        } catch (Exception e) {
            logError(textComment.getChallengeId(), textComment.getMemberId(), "commentToChallange", e, "memberId=" + textComment.getMemberId()
            + "&challengeId=" + textComment.getChallengeId() + "&comment=" + textComment.getComment());
        }
    }

    @RequestMapping(value = "/getComments")
    public Iterable<TextComment> getComments(String challengeId) throws Exception {
        try {
            return chlService.getComments(challengeId);
        } catch (Exception e) {
            logError(challengeId, null, "getComments", e, "challengeId=" + challengeId);
        }
        return null;
    }

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

    @RequestMapping(value = "/joinToChallenge")
    public void joinToChallenge(@Valid @RequestBody JoinToChallenge joinToChallenge) throws Exception {
        try {
            chlService.joinToChallenge(joinToChallenge);
        } catch (Exception e) {
            logError(joinToChallenge.getChallengeId(), joinToChallenge.getMemberId(), "joinToChallenge", e, "memberId=" + joinToChallenge.getMemberId() + "&challengeId=" + joinToChallenge.getChallengeId()
                    + "&join=" + joinToChallenge.getJoin());
        }
    }

    @RequestMapping(value = "/updateProgressOrDoneForSelf")
    public void updateProgressOrDoneForSelf(String challengeId, String result, Boolean done) throws Exception {
        try {
            chlService.updateProgressOrDoneForSelf(challengeId, result, done);
        } catch (Exception e) {
            logError(challengeId, null, "updateProgressOrDoneForSelf", e, "challengeId=" + challengeId + "&result=" + result
                    + "&done=" + done);
        }
    }

    @RequestMapping(value = "/updateResultsOfVersus")
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore) throws Exception {
        try {
            chlService.updateResultsOfVersus(challengeId, firstTeamScore, secondTeamScore);
        } catch (Exception e) {
            logError(challengeId, null, "updateResultsOfVersus", e, "challengeId=" + challengeId + "&firstTeamScore=" + firstTeamScore
                    + "&secondTeamScore=" + secondTeamScore);
        }
    }

    @RequestMapping(value = "/deleteChallenge")
    public void deleteChallenge(String challengeId) throws Exception {
        try {
            chlService.deleteChallenge(challengeId);
        } catch (Exception e) {
            logError(challengeId, null, "deleteChallenge", e, "challengeId=" + challengeId);
        }
    }

    @RequestMapping(value = "/acceptOrRejectChl")
    public void acceptOrRejectChl(@Valid @RequestBody VersusAttendance chlAtt) throws Exception {
        try {
            chlService.acceptOrRejectChl(chlAtt);
        } catch (Exception e) {
            logError(chlAtt.getChallengeId(), chlAtt.getMemberId(), "acceptOrRejectChl", e, "challengeId=" + chlAtt.getChallengeId());
            // TODO inputs
        }

    }

    @RequestMapping(value = "/getActivities")
    public List<Activity> getActivities(String toMemberId) throws Exception {
        try {
            return activityService.getActivities(toMemberId);
        } catch (Exception e) {
            logError(null, toMemberId, "getActivities", e, "toMemberId=" + toMemberId);
        }
        return null;
    }

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
        Error error = new Error();
        error.setFe(false);
        error.setChallengeId(challengeId);
        error.setMemberId(memberId);
        error.setServiceURL(serviceURL);
        error.setErrorMessage(e.toString());
        error.setInputs(inputs);
        error.setInsertTime(new Date());
        errorRepository.save(error);
        throw new Exception(e.toString());
    }
}
