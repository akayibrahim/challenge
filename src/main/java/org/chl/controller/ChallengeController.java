package org.chl.controller;

import org.chl.model.*;
import org.chl.service.ChallengeService;
import org.chl.util.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeService chlService;

    public ChallengeService getChlService() {
        return chlService;
    }

    public void setChlService(ChallengeService chlService) {
        this.chlService = chlService;
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
    public void likeChallenge(@Valid @RequestBody Like like) {
        chlService.likeChallange(like);
    }

    @RequestMapping(value = "/getChallenges")
    public Iterable<Challenge> getChallenges(String memberId) {
        Iterable<Challenge> challenges = chlService.getChallengesOfMember(memberId);
        return challenges;
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
}
