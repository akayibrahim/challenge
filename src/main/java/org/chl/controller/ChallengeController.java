package org.chl.controller;

import org.chl.model.*;
import org.chl.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.MultipartConfig;
import javax.validation.Valid;
import java.util.Date;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeService chlService;

    @RequestMapping(value = "/getChallenges")
    public Iterable<Challenge> getChallenges(String memberId) {
        Iterable<Challenge> challenges = chlService.getChallenges(memberId);
        return challenges;
    }

    @RequestMapping(value = "/getChallengesOfMember")
    public Iterable<Challenge> getChallengesOfMember(String memberId) {
        Iterable<Challenge> challenges = chlService.getChallengesOfMember(memberId);
        return challenges;
    }

    @RequestMapping(value = "/getExplorerChallenges")
    public Iterable<Challenge> getExplorerChallenges(String memberId, String challengeId, Boolean addSimilarChallanges) {
        Iterable<Challenge> challenges = chlService.getExplorerChallenges(memberId, challengeId, addSimilarChallanges);
        return challenges;
    }

    @RequestMapping(value = "/getTrendChallenges")
    public Iterable<Trends> getTrendsChallenges(String memberId) {
        Iterable<Trends> trends = chlService.getTrendChallenges(memberId);
        return trends;
    }

    @RequestMapping(value = "/addJoinChallenge")
    public String addJoinChallenge(@Valid @RequestBody JoinAndProofChallenge joinChl) {
        Challenge challenge = chlService.addJoinChallenge(joinChl);
        return challenge.getId();
    }

    @RequestMapping(value = "/addVersusChallenge")
    public String addVersusChallenge(@Valid @RequestBody VersusChallenge versusChl) {
        Challenge challenge = chlService.addVersusChallenge(versusChl);
        return challenge.getId();
    }

    @RequestMapping(value = "/addSelfChallenge")
    public String addSelfChallenge(@Valid @RequestBody SelfChallenge selfChl) {
        Challenge challenge = chlService.addSelfChallenge(selfChl);
        return challenge.getId();
    }

    @RequestMapping(value = "/getSubjects")
    public Iterable<Subjects> getSubjects() {
        return chlService.getSubjects(false);
    }

    @RequestMapping(value = "/getSelfSubjects")
    public Iterable<Subjects> getSelfSubjects() {
        return chlService.getSubjects(true);
    }

    @RequestMapping(value = "/commentToChallange")
    public void commentToChallange(@Valid @RequestBody TextComment textComment) {
        chlService.commentAsTextToChallange(textComment);
    }

    @RequestMapping(value = "/getComments")
    public Iterable<TextComment> getComments(String challengeId) {
        return chlService.getComments(challengeId);
    }

    @RequestMapping(value = "/supportChallenge")
    public void likeChallenge(@Valid @RequestBody Support support) {
        support.setDate(new Date());
        chlService.supportChallange(support);
    }

    @RequestMapping(value = "/joinToChallenge")
    public void joinToChallenge(@Valid @RequestBody JoinToChallenge joinToChallenge) {
        chlService.joinToChallenge(joinToChallenge);
    }

    @RequestMapping(value = "/updateProgressOrDoneForSelf")
    public void updateProgressOrDoneForSelf(String challengeId, String result, Boolean done) {
        chlService.updateProgressOrDoneForSelf(challengeId, result, done);
    }

    @RequestMapping(value = "/updateResultsOfVersus")
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore) {
        chlService.updateResultsOfVersus(challengeId, firstTeamScore, secondTeamScore);
    }

    @RequestMapping(value = "/deleteChallenge")
    public void deleteChallenge(String challengeId) {
        chlService.deleteChallenge(challengeId);
    }

    @RequestMapping(value = "/acceptOrRejectChl")
    public void acceptOrRejectChl(@Valid @RequestBody VersusAttendance chlAtt) {
        chlService.acceptOrRejectChl(chlAtt);
    }

    public ChallengeService getChlService() {
        return chlService;
    }

    public void setChlService(ChallengeService chlService) {
        this.chlService = chlService;
    }
}
