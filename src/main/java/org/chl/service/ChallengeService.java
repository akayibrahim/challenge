package org.chl.service;

import org.chl.intf.IChallengeService;
import org.chl.model.*;
import org.chl.repository.JoinAndProofAttendanceRepository;
import org.chl.repository.VersusAttendanceRepository;
import org.chl.repository.ChallengeRepository;
import org.chl.repository.LikeRepository;
import org.chl.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class ChallengeService implements IChallengeService {
    private ChallengeRepository chlRepo;
    private LikeRepository likeRepo;
    private VersusAttendanceRepository versusRepo;
    private JoinAndProofAttendanceRepository joinAndProofRepo;
    private NotificationService notificationService;

    @Autowired
    public ChallengeService(ChallengeRepository chlRepo, LikeRepository likeRepo, VersusAttendanceRepository versusRepo, JoinAndProofAttendanceRepository joinAndProofRepo, NotificationService notificationService) {
        this.chlRepo = chlRepo;
        this.likeRepo = likeRepo;
        this.versusRepo = versusRepo;
        this.joinAndProofRepo = joinAndProofRepo;
        this.notificationService = notificationService;
    }

    @Override
    public void likeChallange(Like like) {
        likeRepo.save(like);
    }

    @Override
    public VersusChallenge addVersusChallenge(VersusChallenge versusChl) {
        chlRepo.save(versusChl);
        for (VersusAttendance versusAtt:versusChl.getVersusAttendanceList()) {
            versusAtt.setChallengeId(versusChl.getId());
            versusRepo.save(versusAtt);
            sendNotification(versusChl.getChallengerId(), versusAtt.getMemberId(), Constant.PUSH_NOTIFICATION.DONE, versusChl.getUntilDate());
        }
        return versusChl;
    }

    @Override
    public JoinAndProofChallenge addJoinChallenge(JoinAndProofChallenge joinChl) {
        chlRepo.save(joinChl);
        for (JoinAttendance joinAtt:joinChl.getJoinAttendanceList()) {
            joinAtt.setChallengeId(joinChl.getId());
            joinAndProofRepo.save(joinAtt);
        }
        return joinChl;
    }

    @Override
    public SelfChallenge addSelfChallenge(SelfChallenge selfChl) {
        chlRepo.save(selfChl);
        return selfChl;
    }

    @Override
    public void updateProgressOrDoneForSelf(String challengeId, String score, Boolean done) {
        Challenge chl = chlRepo.findOne(challengeId);
        SelfChallenge selfChl = (SelfChallenge) chl;
        if(selfChl != null) {
            selfChl.setScore(score);
            selfChl.setDone(done);
            chlRepo.save(selfChl);
        }
    }

    @Override
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore) {
        Challenge chl = chlRepo.findOne(challengeId);
        VersusChallenge versusChl = (VersusChallenge) chl;
        if(versusChl != null) {
            versusChl.setFirstTeamScore(firstTeamScore);
            versusChl.setSecondTeamScore(secondTeamScore);
            versusChl.setDone(true);
            chlRepo.save(versusChl);
        }
    }

    @Override
    public void deleteChallenge(String challengeId) {
        Challenge findChl = chlRepo.findOne(challengeId);
        if(findChl != null)
            chlRepo.delete(findChl);
    }

    @Override
    public Iterable<Challenge> getChallenges() {
        return chlRepo.findAll();
    }

    @Override
    public Iterable<Challenge> getChallengesOfMember(String memberId) {
        Iterable<Challenge> challenges = chlRepo.findAll(); // TODO
        for (Challenge chl: challenges) {
            if(chl instanceof  VersusChallenge) {
                VersusChallenge versusChl = (VersusChallenge) chl;
                versusChl.setVersusAttendanceList(versusRepo.findByChallengeId(chl.getId()));
            } else if(chl instanceof JoinAndProofChallenge) {
                JoinAndProofChallenge joinChl = (JoinAndProofChallenge) chl;
                joinChl.setJoinAttendanceList(joinAndProofRepo.findByChallengeId(chl.getId()));
            }
            List<Like> likes =  likeRepo.findByChallengeId(chl.getId());
            chl.setLikes(likes);
            chl.setCountOfLike(likes.size());
        }
        return challenges;
    }

    @Override
    public JoinAttendance joinToChallenge(JoinAttendance join) {
        joinAndProofRepo.save(join);
        return join;
    }

    @Override
    public void acceptOrRejectChl(VersusAttendance versusAtt) {
        VersusAttendance result = versusRepo.findByMemberIdAndChallengeId(versusAtt.getMemberId(), versusAtt.getChallengeId());
        if(result != null && versusAtt.getAccept()) {
            result.setAccept(versusAtt.getAccept());
            versusRepo.save(result);
        }
    }

    private void sendNotification(String challengeId, String memberId, Constant.PUSH_NOTIFICATION notificationType, Date untilDate) {
        PushNotification notification = new PushNotification();
        notification.setChallengeId(challengeId);
        notification.setMemberId(memberId);
        notification.setNotification(notificationType);
        notification.setUntilDate(untilDate);
        notificationService.send(notification);
    }
}
