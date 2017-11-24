package org.chl.services;

import org.chl.intf.IChallengeService;
import org.chl.models.Challenge;
import org.chl.repos.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class ChallengeService implements IChallengeService {
    private ChallengeRepository chlRepo;

    @Autowired
    public ChallengeService(ChallengeRepository chlRepo) {
        this.chlRepo = chlRepo;
    }

    @Override
    public Challenge addChallenge(Challenge chl) {
        chlRepo.save(chl);
        return chl;
    }

    @Override
    public Iterable<Challenge> getChallenges() {
        return chlRepo.findAll();
    }
}
