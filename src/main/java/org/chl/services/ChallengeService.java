package org.chl.services;

import org.chl.intf.IChallengeService;
import org.chl.models.Challenge;
import org.chl.models.ChallengeAttendance;
import org.chl.models.Like;
import org.chl.repos.ChallengeAttendaceRepository;
import org.chl.repos.ChallengeRepository;
import org.chl.repos.LikeRepository;
import org.chl.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class ChallengeService implements IChallengeService {
    private ChallengeRepository chlRepo;
    private LikeRepository likeRepo;
    private ChallengeAttendaceRepository chlAttRepo;

    @Autowired
    public ChallengeService(ChallengeRepository chlRepo, LikeRepository likeRepo, ChallengeAttendaceRepository chlAttRepo) {
        this.chlRepo = chlRepo;
        this.likeRepo = likeRepo;
        this.chlAttRepo = chlAttRepo;
    }

    @Override
    public void likeChallange(Like like) {
        likeRepo.save(like);
    }

    @Override
    public Iterable<Like> getChallangeLikes(String challengeId) {
        return likeRepo.findByChallengeId(challengeId);
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

    @Override
    public ChallengeAttendance joinOrInviteForAttendaceToChallenge(ChallengeAttendance chlAtt) {
        chlAttRepo.save(chlAtt);
        return chlAtt;
    }

    @Override
    public List<ChallengeAttendance> getAttendanceOfChls() {
        return chlAttRepo.findAll();
    }

    @Override
    public ChallengeAttendance getAcceptAttendanceMemberId(String memberId, String challengeId) {
        return chlAttRepo.findByMemberIdAndChallengeId(memberId, challengeId);
    }

    @Override
    public List<ChallengeAttendance> getAttendancesOfChallenge(String challengeId) {
        return chlAttRepo.findByChallengeId(challengeId);
    }

    @Override
    public List<ChallengeAttendance> getJoinsToChallenge(String challengeId) {
        return chlAttRepo.findByChallengeIdAndJoin(challengeId, "true");
    }

    @Override
    public void acceptOrRejectChl(ChallengeAttendance chlAtt) {
        ChallengeAttendance result = chlAttRepo.findByMemberIdAndChallengeId(chlAtt.getMemberId(), chlAtt.getChallengeId());
        if(result != null && "true".equals(result.getAskAcceptOrReject())) {
            result.setAcceptOrReject(chlAtt.getAcceptOrReject());
            chlAttRepo.save(result);
        }
    }
}
