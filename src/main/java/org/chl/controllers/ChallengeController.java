package org.chl.controllers;

import org.chl.models.Challenge;
import org.chl.services.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/addChl")
    public String addChallenge(@RequestBody Challenge chl) {
        Challenge challenge = chlService.addChallenge(chl);
        return "1";
    }

    @RequestMapping(value = "/getChls")
    public Iterable<Challenge> getChallenges() {
        Iterable<Challenge> challenges = chlService.getChallenges();
        return challenges;
    }
    
}
