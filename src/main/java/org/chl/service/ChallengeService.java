package org.chl.service;

import com.google.common.collect.Lists;
import org.chl.intf.IChallengeService;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.util.Calculations;
import org.chl.util.Constant;
import org.chl.util.DateUtil;
import org.chl.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class ChallengeService implements IChallengeService {
    private ChallengeRepository chlRepo;
    private SupportRepository supportRepository;
    private VersusAttendanceRepository versusRepo;
    private JoinAndProofAttendanceRepository joinAndProofRepo;
    private NotificationService notificationService;
    private MemberService memberService;
    private CommentRepository commentRepository;
    private TrendChallengeRepository trendChallengeRepository;

    @Autowired
    public ChallengeService(ChallengeRepository chlRepo, SupportRepository supportRepository, VersusAttendanceRepository versusRepo, JoinAndProofAttendanceRepository joinAndProofRepo, NotificationService notificationService, MemberService memberService, CommentRepository commentRepository,
                            TrendChallengeRepository trendChallengeRepository) {
        this.chlRepo = chlRepo;
        this.supportRepository = supportRepository;
        this.versusRepo = versusRepo;
        this.joinAndProofRepo = joinAndProofRepo;
        this.notificationService = notificationService;
        this.memberService = memberService;
        this.commentRepository = commentRepository;
        this.trendChallengeRepository = trendChallengeRepository;
    }

    @Override
    public Iterable<Challenge> getChallenges(String memberId) {
        List<String> friendLists = memberService.getFriendList(memberId);
        Iterable<Challenge> challenges = chlRepo.findChallenges(friendLists, Constant.TYPE.PUBLIC, new Sort(Sort.Direction.DESC,"id"));
        prepareChallengesData(memberId, challenges, false);
        return challenges;
    }

    @Override
    public Iterable<Challenge> getChallengesOfMember(String memberId) {
        Iterable<Challenge> challengesByMemberId = chlRepo.findChallengesByMemberId(memberId, new Sort(Sort.Direction.DESC,"id"));
        List<Challenge> challenges = Lists.newArrayList(challengesByMemberId);
        List<VersusAttendance> versusAttendanceList = versusRepo.findByMemberIdInAttendace(memberId);
        List<JoinAttendance> joinAttendanceList = joinAndProofRepo.findByMemberIdInAttendace(memberId);
        List<String> challangeIdList = new ArrayList<>();
        for (VersusAttendance versus:  versusAttendanceList) {
            challangeIdList.add(versus.getChallengeId());
        }
        for (JoinAttendance join:  joinAttendanceList) {
            challangeIdList.add(join.getChallengeId());
        }
        for (Challenge chl : challenges) {
             if (challangeIdList.contains(chl.getId()))
                 challangeIdList.remove(chl.getId());
        }
        Iterable<Challenge> restChallenges = chlRepo.findChallengesByChallengeIdList(challangeIdList, new Sort(Sort.Direction.DESC,"id"));
        List<Challenge> restChallengeList = Lists.newArrayList(restChallenges);
        challenges.addAll(restChallengeList);
        prepareChallengesData(memberId, challenges, true);
        return challenges;
    }

    @Override
    public Iterable<Challenge> getExplorerChallenges(String memberId, String challengeId, Boolean addSimilarChallanges) {
        List<Challenge> challenges = new ArrayList<>();
        Challenge explorerChallenge = chlRepo.findOne(challengeId);
        challenges.add(explorerChallenge);
        if (addSimilarChallanges) {
            Iterable<Challenge> subjectChallenges = chlRepo.findChallengesBySubjectAndType(explorerChallenge.getSubject().toString(), Constant.TYPE.PUBLIC
                    , new Sort(Sort.Direction.DESC,"id"));
            List<Challenge> subjectChallengeList = Lists.newArrayList(subjectChallenges);
            challenges.addAll(subjectChallengeList);
        }
        prepareChallengesData(memberId, challenges, false);
        Iterable<Challenge> challengeIterable = challenges;
        return challengeIterable;
    }

    @Override
    public Iterable<Trends> getTrendChallenges(String memberId) {
        List<Trends> trendList = new ArrayList<>();
        Iterable<TrendChallenge> trendChallenges = trendChallengeRepository.findTrendChallengesByType(Constant.TYPE.PUBLIC, new Sort(Sort.Direction.DESC,"popularity"));
        for (TrendChallenge trendChallenge : trendChallenges) {
            Challenge challenge = chlRepo.findOne(trendChallenge.getChallengeId());
            Trends trend = new Trends();
            trend.setChallengeId(challenge.getId());
            trend.setName(challenge.getName());
            trend.setProoferFbID(challenge.getChallengerFBId());
            trend.setSubject(challenge.getSubject().toString());
            trend.setProof("steve"); // TODO
            trendList.add(trend);
        }
        return trendList;
    }

    private void prepareChallengesData(String memberId, Iterable<Challenge> challenges, Boolean comeFromSelf) {
        for (Challenge chl: challenges) {
            if(chl instanceof VersusChallenge) {
                VersusChallenge versusChl = (VersusChallenge) chl;
                versusChl.setVersusAttendanceList(versusRepo.findByChallengeId(chl.getId()));
                Integer teamSize = versusChl.getVersusAttendanceList().size() / 2;
                versusChl.setFirstTeamCount((teamSize).toString());
                versusChl.setSecondTeamCount(teamSize.toString());
            } else if(chl instanceof JoinAndProofChallenge) {
                JoinAndProofChallenge joinChl = (JoinAndProofChallenge) chl;
                joinChl.setJoinAttendanceList(joinAndProofRepo.findByChallengeId(chl.getId()));
                Integer teamSize = joinChl.getJoinAttendanceList().size() - 1;
                joinChl.setFirstTeamCount(BigDecimal.ONE.toString());
                joinChl.setSecondTeamCount(teamSize.toString());
            } else {
                chl.setFirstTeamCount(BigDecimal.ONE.toString());
                chl.setSecondTeamCount(BigDecimal.ZERO.toString());
            }
            List<TextComment> commentAndProofs = commentRepository.findByChallengeId(chl.getId());
            chl.setCountOfComments(commentAndProofs.size());
            Member memberInfo = memberService.getMemberInfo(chl.getChallengerId());
            chl.setName(memberInfo.getName() + " " + memberInfo.getSurname());
            chl.setChallengerFBId(memberInfo.getFacebookID());
            chl.setComeFromSelf(comeFromSelf);
            Support memberSupport =  supportRepository.findByMemberId(memberId);
            chl.setSupportFirstTeam(memberSupport != null ? memberSupport.getSupportFirstTeam() : false);
            chl.setSupportSecondTeam(memberSupport != null ? memberSupport.getSupportSecondTeam() : false);
            List<Support> supports =  supportRepository.findByChallengeIdAndSupportFirstTeam(chl.getId(), true);
            chl.setFirstTeamSupportCount(supports != null ? supports.size() : 0);
            List<Support> supportSecondTeam =  supportRepository.findByChallengeIdAndSupportSecondTeam(chl.getId(), true);
            chl.setSecondTeamSupportCount(supportSecondTeam != null ? supportSecondTeam.size() : 0);
            List<JoinAttendance> proofs = joinAndProofRepo.findByChallengeId(chl.getId());
            chl.setCountOfProofs(proofs != null ? proofs.size() : 0);
            JoinAttendance proofOfMember = joinAndProofRepo.findByChallengeIdAndMemberId(chl.getId(), memberId);
            chl.setProofed(proofOfMember != null ? proofOfMember.getProof() : false);
            chl.setFirstTeamScore(chl.getFirstTeamScore() != null ? chl.getFirstTeamScore() : "-");
            chl.setSecondTeamScore(chl.getSecondTeamScore() != null ? chl.getSecondTeamScore() : "-");
            chl.setUntilDateStr(Calculations.calculateUntilDate(DateUtil.covertToDate(chl.getUntilDate())));
            chl.setInsertTime(Calculations.calculateInsertTime(chl.getChlDate()));
            if (proofOfMember != null && proofOfMember.getProof() && proofOfMember.getChallenger()) {
                chl.setStatus(Constant.STATUS.NEW_PROOF.getStatus());
            } else if (proofOfMember != null && proofOfMember.getProof()) {
                chl.setStatus(Constant.STATUS.PROOF.getStatus());
            } else if (proofOfMember != null && proofOfMember.getJoin()) {
                chl.setStatus(Constant.STATUS.JOIN.getStatus());
            } else if (!chl.getDone()) {
                chl.setStatus(Constant.STATUS.NEW.getStatus());
            } else if (chl.getDone()) {
                chl.setStatus(Constant.STATUS.FINISH.getStatus());
            }
        }
    }

    @Override
    public VersusChallenge addVersusChallenge(VersusChallenge versusChl) {
        Validation.doneValidationForVersus(versusChl.getDone(), versusChl.getFirstTeamScore(), versusChl.getSecondTeamScore());
        Validation.checkTeamCountEqual(versusChl.getFirstTeamCount(), versusChl.getSecondTeamCount());
        Validation.checkMemberAvaliable(memberService.checkMemberAvailable(versusChl.getChallengerId()));
        versusChl.setType(Constant.TYPE.PRIVATE);
        versusChl.setChlDate(new Date());
        for (VersusAttendance versusAtt:versusChl.getVersusAttendanceList()) {
            if (!Validation.checkMemberAvaliable(memberService.checkMemberAvailable(versusAtt.getMemberId())))
                return null;
        }
        chlRepo.save(versusChl);
        for (VersusAttendance versusAtt:versusChl.getVersusAttendanceList()) {
            if (versusAtt.getMemberId().equals(versusChl.getChallengerId()))
                    versusAtt.setAccept(true);
            versusAtt.setChallengeId(versusChl.getId());
            versusAtt.setFacebookID(memberService.getMemberInfo(versusAtt.getMemberId()).getFacebookID());
            versusRepo.save(versusAtt);
            sendNotification(versusChl.getChallengerId(), versusAtt.getMemberId(), Constant.PUSH_NOTIFICATION.DONE, DateUtil.covertToDate(versusChl.getUntilDate()));
        }
        return versusChl;
    }

    @Override
    public JoinAndProofChallenge addJoinChallenge(JoinAndProofChallenge joinChl) {
        joinChl.setType(Constant.TYPE.PUBLIC);
        Validation.checkMemberAvaliable(memberService.checkMemberAvailable(joinChl.getChallengerId()));
        joinChl.setChlDate(new Date());
        if (joinChl.getJoinAttendanceList() == null)
            joinChl.setJoinAttendanceList(new ArrayList<JoinAttendance>());
        JoinAttendance attendance = new JoinAttendance();
        attendance.setChallenger(true);
        attendance.setProof(true);
        attendance.setJoin(false);
        attendance.setMemberId(joinChl.getChallengerId());
        joinChl.getJoinAttendanceList().add(attendance);
        for (JoinAttendance joinAtt : joinChl.getJoinAttendanceList()) {
            if (Validation.checkMemberAvaliable(memberService.checkMemberAvailable(joinAtt.getMemberId())))
                return null;
        }
        chlRepo.insert(joinChl);
        for (JoinAttendance joinAtt:joinChl.getJoinAttendanceList()) {
            if (!joinAtt.getChallenger())
                joinAtt.setChallenger(false);
            joinAtt.setChallengeId(joinChl.getId());
            joinAtt.setFacebookID(memberService.getMemberInfo(joinAtt.getMemberId()).getFacebookID());
            joinAndProofRepo.save(joinAtt);
        }
        return joinChl;
    }

    @Override
    public SelfChallenge addSelfChallenge(SelfChallenge selfChl) {
        selfChl.setType(Constant.TYPE.SELF);
        Validation.checkMemberAvaliable(memberService.checkMemberAvailable(selfChl.getChallengerId()));
        selfChl.setChlDate(new Date());
        chlRepo.save(selfChl);
        return selfChl;
    }

    @Override
    public void likeChallange(Support support) {
        TrendChallenge trendChallenge = trendChallengeRepository.findTrendByChallengeId(support.getChallengeId());
        if (trendChallenge != null) {
            trendChallenge.setPopularity(trendChallenge.getPopularity() + 1);
            trendChallengeRepository.save(trendChallenge);
        } else {
            TrendChallenge newTrendChallenge = new TrendChallenge();
            newTrendChallenge.setPopularity(1);
            newTrendChallenge.setChallengeId(support.getChallengeId());
            newTrendChallenge.setType(chlRepo.findOne(support.getChallengeId()).getType());
            newTrendChallenge.setPopularityType(Constant.POPULARITY.SUPPORT);
            trendChallengeRepository.save(newTrendChallenge);
        }
        supportRepository.save(support);
    }

    @Override
    public void updateProgressOrDoneForSelf(String challengeId, String score, Boolean done) {
        Challenge chl = chlRepo.findOne(challengeId);
        SelfChallenge selfChl = (SelfChallenge) chl;
        if(selfChl != null) {
            selfChl.setScore(score);
            selfChl.setDone(done);
            selfChl.setUpdateDate(new Date());
            chlRepo.save(selfChl);
        }
    }

    @Override
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore) {
        Validation.doneValidationForVersus(true, firstTeamScore, secondTeamScore);
        Challenge chl = chlRepo.findOne(challengeId);
        VersusChallenge versusChl = (VersusChallenge) chl;
        if(versusChl != null) {
            versusChl.setFirstTeamScore(firstTeamScore);
            versusChl.setSecondTeamScore(secondTeamScore);
            versusChl.setDone(true);
            versusChl.setUpdateDate(new Date());
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
    public Iterable<Challenge> getAllChallenges() {
        return chlRepo.findAll();
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

    @Override
    public void commentAsTextToChallange(TextComment textComment) {
        commentRepository.save(textComment);
    }
}
