package org.chl.controller;

import org.chl.model.*;
import org.chl.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @RequestMapping(value = "/likeChallenge")
    public void likeChallenge(@Valid @RequestBody Support support) {
        chlService.likeChallange(support);
    }

    @RequestMapping(value = "/updateProgressOrDoneForSelf")
    public void updateProgressOrDoneForSelf(String challengeId, String score, Boolean done) {
        chlService.updateProgressOrDoneForSelf(challengeId, score, done);
    }

    @RequestMapping(value = "/updateResultsOfVersus")
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore) {
        chlService.updateResultsOfVersus(challengeId, firstTeamScore, secondTeamScore);
    }

    @RequestMapping(value = "/deleteChallenge")
    public void deleteChallenge(String challengeId) {
        chlService.deleteChallenge(challengeId);
    }

    @RequestMapping(value = "/joinToChallenge")
    public void joinToChallenge(@Valid @RequestBody JoinAttendance join) {
        chlService.joinToChallenge(join);
    }

    @RequestMapping(value = "/acceptOrRejectChl")
    public void acceptOrRejectChl(@Valid @RequestBody VersusAttendance chlAtt) {
        chlService.acceptOrRejectChl(chlAtt);
    }

    @RequestMapping(value = "/commentToChallange")
    public void commentToChallange(@Valid @RequestBody TextComment textComment) {
        chlService.commentAsTextToChallange(textComment);
    }

    public ChallengeService getChlService() {
        return chlService;
    }

    public void setChlService(ChallengeService chlService) {
        this.chlService = chlService;
    }
}
