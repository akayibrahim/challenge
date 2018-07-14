package org.chl.service;

import com.google.common.collect.Lists;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.chl.intf.IChallengeService;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.util.*;
import org.chl.util.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ActivityService activityService;

    @Autowired
    public ChallengeService(ChallengeRepository chlRepo, SupportRepository supportRepository, VersusAttendanceRepository versusRepo, JoinAndProofAttendanceRepository joinAndProofRepo, NotificationService notificationService, MemberService memberService, CommentRepository commentRepository,
                            TrendChallengeRepository trendChallengeRepository, ActivityService activityService) {
        this.chlRepo = chlRepo;
        this.supportRepository = supportRepository;
        this.versusRepo = versusRepo;
        this.joinAndProofRepo = joinAndProofRepo;
        this.notificationService = notificationService;
        this.memberService = memberService;
        this.commentRepository = commentRepository;
        this.trendChallengeRepository = trendChallengeRepository;
        this.activityService = activityService;
    }

    @Override
    public Challenge getChallengeById(String challangeId) {
        return chlRepo.findById(challangeId).get();
    }

    @Override
    public Iterable<Challenge> getChallenges(String memberId) {
        List<String> friendLists = memberService.getFollowingIdList(memberId);
        Iterable<Challenge> challenges = chlRepo.findChallenges(friendLists, Constant.TYPE.PUBLIC, new Date(), new Sort(Sort.Direction.DESC,"updateDate"));
        prepareChallengesData(memberId, challenges, false);
        return challenges;
    }

    @Override
    public Iterable<Challenge> getChallengesOfMember(String memberId) {
        setToDoneForPastChallenge(memberId);
        List<String> visibilities = fullVisibility();
        List<Challenge> challenges = findMemberChallenges(memberId, visibilities, true);
        return challenges;
    }

    private List<String> fullVisibility() {
        List<String> visibilities = new ArrayList<>();
        visibilities.add(Constant.VISIBILITY.EVERYONE.getCode());
        visibilities.add(Constant.VISIBILITY.FRIENDS.getCode());
        visibilities.add(Constant.VISIBILITY.JUST.getCode());
        return visibilities;
    }

    @Override
    public Iterable<Challenge> getChallengesOfFriend(String memberId, String friendMemberId) {
        List<String> visibilities = new ArrayList<>();
        visibilities.add(Constant.VISIBILITY.EVERYONE.getCode());
        if (memberService.isMyFriend(memberId, friendMemberId))
            visibilities.add(Constant.VISIBILITY.FRIENDS.getCode());
        List<Challenge> challenges = findMemberChallenges(friendMemberId, visibilities, false);
        return challenges;
    }

    private List<Challenge> findMemberChallenges(String memberId, List<String> visibilities, Boolean myProfile) {
        Boolean activesForMyProfile[] = {true, false};
        Boolean activesForOthers[] = {true};
        List<Boolean> actives = myProfile ? Arrays.asList(activesForMyProfile) : Arrays.asList(activesForOthers);
        Iterable<Challenge> challengesByMemberId = chlRepo.findChallengesByMemberId(memberId, visibilities, actives, new Sort(Sort.Direction.DESC,"updateDate"));
        List<Challenge> challenges = Lists.newArrayList(challengesByMemberId);
        List<JoinAttendance> joinAttendanceList = joinAndProofRepo.findByMemberIdInAttendace(memberId);
        List<String> challangeIdList = new ArrayList<>();
        if (myProfile) {
            List<VersusAttendance> versusAttendanceList = versusRepo.findByMemberIdInAttendace(memberId);
            for (VersusAttendance versus:  versusAttendanceList) {
                challangeIdList.add(versus.getChallengeId());
            }
        }
        for (JoinAttendance join:  joinAttendanceList) {
            challangeIdList.add(join.getChallengeId());
        }
        for (Challenge chl : challenges) {
             if (challangeIdList.contains(chl.getId()))
                 challangeIdList.remove(chl.getId());
        }
        Iterable<Challenge> restChallenges = chlRepo.findChallengesByChallengeIdList(challangeIdList, actives, new Sort(Sort.Direction.DESC,"updateDate"));
        List<Challenge> restChallengeList = Lists.newArrayList(restChallenges);
        challenges.addAll(restChallengeList);
        prepareChallengesData(memberId, challenges, true);
        return challenges;
    }

    private void setToDoneForPastChallenge(String memberId) {
        List<String> visibilities = fullVisibility();
        Boolean actives[] = {true, false};
        Iterable<Challenge> challengesByMemberId = chlRepo.findChallengesByMemberId(memberId, visibilities, Arrays.asList(actives), new Sort(Sort.Direction.DESC,"updateDate"));
        challengesByMemberId.forEach(challenge -> {
            if (DateUtil.covertToDate(challenge.getUntilDate()).compareTo(new Date()) <= 0) {
                Challenge exist = chlRepo.findById(challenge.getId()).get();
                exist.setDone(true);
                exist.setUpdateDate(new Date());
                chlRepo.save(exist);
            }
        });
    }

    @Override
    public Iterable<Challenge> getExplorerChallenges(String memberId, String challengeId, Boolean addSimilarChallanges) {
        List<Challenge> challenges = new ArrayList<>();
        Challenge explorerChallenge = chlRepo.findById(challengeId).get();
        boolean isExist = false;
        if (addSimilarChallanges) {
            Iterable<Challenge> subjectChallenges = chlRepo.findChallengesBySubjectAndType(explorerChallenge.getSubject().toString(), Constant.TYPE.PUBLIC
                    , new Date(), new Sort(Sort.Direction.DESC,"id"));
            List<Challenge> subjectChallengeList = Lists.newArrayList(subjectChallenges);
            challenges.addAll(subjectChallengeList);
            for (Challenge chl : subjectChallengeList) {
                if (explorerChallenge.getId().equals(chl.getId())) {
                    isExist = true;
                    break;
                }
            }
        }
        if (!isExist)
            challenges.add(explorerChallenge);
        prepareChallengesData(memberId, challenges, false);
        Iterable<Challenge> challengeIterable = challenges;
        return challengeIterable;
    }

    @Override
    public Iterable<Trends> getTrendChallenges(String memberId, String subjectSearchKey) {
        List<Trends> trendList = new ArrayList<>();
        if (StringUtils.hasText(subjectSearchKey)) {
            Iterable<TrendChallenge> trendChallenges = trendChallengeRepository.findTrendChallengesBySearch(subjectSearchKey, Constant.TYPE.PUBLIC, new Sort(Sort.Direction.DESC,"popularity"));
            for (TrendChallenge trendChallenge : trendChallenges) {
                Challenge challenge = chlRepo.findById(trendChallenge.getChallengeId()).get();
                trendList.add(prepareTrend(challenge));
            }
        } else {
            Iterable<TrendChallenge> trendChallenges = trendChallengeRepository.findTrendChallengesByType(Constant.TYPE.PUBLIC, new Sort(Sort.Direction.DESC,"popularity"));
            for (TrendChallenge trendChallenge : trendChallenges) {
                Challenge challenge = chlRepo.findById(trendChallenge.getChallengeId()).get();
                trendList.add(prepareTrend(challenge));
            }
        }
        return trendList;
    }

    private Trends prepareTrend(Challenge challenge) {
        Trends trend = new Trends();
        trend.setChallengeId(challenge.getId());
        trend.setName(challenge.getName());
        trend.setProoferFbID(challenge.getChallengerFBId());
        trend.setSubject(challenge.getSubject().toString());
        trend.setProof("steve"); // TODO
        trend.setChallengerId(challenge.getChallengerId());
        return trend;
    }

    private void prepareChallengesData(String memberId, Iterable<Challenge> challenges, Boolean comeFromSelf) {
        for (Challenge chl: challenges) {
            if(chl instanceof VersusChallenge) {
                VersusChallenge versusChl = (VersusChallenge) chl;
                versusChl.setVersusAttendanceList(versusRepo.findByChallengeId(chl.getId()));
                Integer teamSize = versusChl.getVersusAttendanceList().size() / 2;
                versusChl.setFirstTeamCount((teamSize).toString());
                versusChl.setSecondTeamCount(teamSize.toString());
                versusChl.setFirstTeamScore(versusChl.getFirstTeamScore() != null ? versusChl.getFirstTeamScore() : "-");
                versusChl.setSecondTeamScore(versusChl.getSecondTeamScore() != null ? versusChl.getSecondTeamScore() : "-");
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
            Support memberSupport = supportRepository.findByMemberIdAndChallengeId(memberId, chl.getId());
            chl.setSupportFirstTeam(memberSupport != null ? memberSupport.getSupportFirstTeam() : false);
            chl.setSupportSecondTeam(memberSupport != null ? memberSupport.getSupportSecondTeam() : false);
            List<Support> supports =  supportRepository.findByChallengeIdAndSupportFirstTeam(chl.getId(), true);
            chl.setFirstTeamSupportCount(supports != null ? supports.size() : 0);
            List<Support> supportSecondTeam =  supportRepository.findByChallengeIdAndSupportSecondTeam(chl.getId(), true);
            chl.setSecondTeamSupportCount(supportSecondTeam != null ? supportSecondTeam.size() : 0);
            List<JoinAttendance> proofs = joinAndProofRepo.findByChallengeIdAndProof(chl.getId(), true);
            chl.setCountOfProofs(proofs != null ? proofs.size() : 0);
            JoinAttendance proofOfMember = joinAndProofRepo.findByChallengeIdAndMemberId(chl.getId(), chl.getChallengerId());
            chl.setProofed(proofOfMember != null ? proofOfMember.getProof() : false);
            if (chl.getActive())
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
        if (!memberService.checkMemberAvailable(versusChl.getChallengerId()))
            Exception.throwMemberNotAvailable();
        versusChl.setActive(false);
        versusChl.setType(Constant.TYPE.PRIVATE);
        versusChl.setChlDate(new Date());
        if (versusChl.getActive())
            versusChl.setDateOfUntil(DateUtil.covertToDate(versusChl.getUntilDate()));
        for (VersusAttendance versusAtt:versusChl.getVersusAttendanceList()) {
            if (!memberService.checkMemberAvailable(versusAtt.getMemberId()))
                Exception.throwMemberNotAvailable();
        }
        chlRepo.save(versusChl);
        for (VersusAttendance versusAtt:versusChl.getVersusAttendanceList()) {
            if (versusAtt.getMemberId().equals(versusChl.getChallengerId()))
                    versusAtt.setAccept(true);
            else
                versusAtt.setAccept(null);
            versusAtt.setChallengeId(versusChl.getId());
            versusAtt.setFacebookID(memberService.getMemberInfo(versusAtt.getMemberId()).getFacebookID());
            versusAtt.setReject(false);
            versusRepo.save(versusAtt);
            if (!versusAtt.getMemberId().equals(versusChl.getChallengerId()))
                activityService.createActivity(Mappers.prepareActivity(versusAtt.getId(), versusChl.getId(), versusChl.getChallengerId(), versusAtt.getMemberId(), Constant.ACTIVITY.ACCEPT));
            // sendNotification(versusChl.getChallengerId(), versusAtt.getMemberId(), Constant.PUSH_NOTIFICATION.DONE, DateUtil.covertToDate(versusChl.getUntilDate()));
        }
        return versusChl;
    }

    @Override
    public JoinAndProofChallenge addJoinChallenge(JoinAndProofChallenge joinChl) {
        joinChl.setType(Constant.TYPE.PUBLIC);
        if (!memberService.checkMemberAvailable(joinChl.getChallengerId()))
            Exception.throwMemberNotAvailable();
        joinChl.setChlDate(new Date());
        if (joinChl.getJoinAttendanceList() == null || joinChl.getJoinAttendanceList().size() == 0) {
            joinChl.setActive(true);
            joinChl.setJoinAttendanceList(new ArrayList<JoinAttendance>());
        } else
            joinChl.setActive(false);
        if (joinChl.getActive())
            joinChl.setDateOfUntil(DateUtil.covertToDate(joinChl.getUntilDate()));
        JoinAttendance attendance = new JoinAttendance();
        attendance.setChallenger(true);
        attendance.setProof(true);
        attendance.setJoin(false);
        attendance.setMemberId(joinChl.getChallengerId());
        joinChl.getJoinAttendanceList().add(attendance);
        for (JoinAttendance joinAtt : joinChl.getJoinAttendanceList()) {
            if (joinAtt.getMemberId().equals("TO_WORLD"))
                continue;
            if (!memberService.checkMemberAvailable(joinAtt.getMemberId()))
                Exception.throwMemberNotAvailable();
        }
        chlRepo.insert(joinChl);
        for (JoinAttendance joinAtt:joinChl.getJoinAttendanceList()) {
            if (joinAtt.getMemberId().equals("TO_WORLD"))
                continue;
            if (!joinAtt.getChallenger())
                joinAtt.setChallenger(false);
            joinAtt.setProof(false);
            joinAtt.setChallengeId(joinChl.getId());
            joinAtt.setFacebookID(memberService.getMemberInfo(joinAtt.getMemberId()).getFacebookID());
            joinAtt.setReject(false);
            joinAndProofRepo.save(joinAtt);
            if (!joinAtt.getChallenger())
                activityService.createActivity(Mappers.prepareActivity(joinAtt.getId(), joinChl.getId(), joinChl.getChallengerId(), joinAtt.getMemberId(), Constant.ACTIVITY.JOIN));
        }
        return joinChl;
    }

    @Override
    public SelfChallenge addSelfChallenge(SelfChallenge selfChl) {
        selfChl.setType(Constant.TYPE.SELF);
        if (!memberService.checkMemberAvailable(selfChl.getChallengerId()))
            Exception.throwMemberNotAvailable();
        selfChl.setActive(true);
        selfChl.setChlDate(new Date());
        selfChl.setDateOfUntil(DateUtil.covertToDate(selfChl.getUntilDate()));
        chlRepo.save(selfChl);
        return selfChl;
    }

    @Override
    public Iterable<Subjects> getSubjects(boolean isSelf) {
        Iterable<Subjects> subjects = Constant.toList(Subject.values(), isSelf);
        return subjects;
    }

    @Override
    public void supportChallange(Support support) {
        TrendChallenge trendChallenge = trendChallengeRepository.findTrendByChallengeId(support.getChallengeId());
        Challenge challenge = chlRepo.findById(support.getChallengeId()).get();
        if (trendChallenge != null) {
            trendChallenge.setPopularity(trendChallenge.getPopularity() + 1);
            trendChallengeRepository.save(trendChallenge);
        } else {
            TrendChallenge newTrendChallenge = new TrendChallenge();
            newTrendChallenge.setPopularity(1);
            newTrendChallenge.setChallengeId(support.getChallengeId());
            newTrendChallenge.setType(chlRepo.findById(support.getChallengeId()).get().getType());
            newTrendChallenge.setPopularityType(Constant.POPULARITY.SUPPORT);
            newTrendChallenge.setSubject(challenge.getSubject().toString());
            trendChallengeRepository.save(newTrendChallenge);
        }
        String activityTableId;
        Support exist = supportRepository.findByMemberIdAndChallengeId(support.getMemberId(), support.getChallengeId());
        if (exist != null) {
            exist.setSupportFirstTeam(support.getSupportFirstTeam());
            exist.setSupportSecondTeam(support.getSupportSecondTeam());
            supportRepository.save(exist);
            activityTableId = exist.getId();
        } else {
            supportRepository.save(support);
            activityTableId = support.getId();
        }
        if(challenge instanceof VersusChallenge) {
            VersusChallenge versusChl = (VersusChallenge) challenge;
            versusChl.setVersusAttendanceList(versusRepo.findByChallengeId(challenge.getId()));
            versusChl.getVersusAttendanceList().forEach(versusAttendance -> {
                if (support.getSupportFirstTeam()) {
                    if (versusAttendance.getFirstTeamMember())
                        activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(), versusAttendance.getMemberId(), Constant.ACTIVITY.SUPPORT));
                } else if (support.getSupportSecondTeam()) {
                    if (versusAttendance.getSecondTeamMember())
                        activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(), versusAttendance.getMemberId(), Constant.ACTIVITY.SUPPORT));
                }

            });
        } else if(challenge instanceof JoinAndProofChallenge) {
            JoinAndProofChallenge joinChl = (JoinAndProofChallenge) challenge;
            joinChl.setJoinAttendanceList(joinAndProofRepo.findByChallengeId(challenge.getId()));
            joinChl.getJoinAttendanceList().forEach(joinAttendance -> {
                if (!joinAttendance.getChallenger())
                    activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(), joinAttendance.getMemberId(), Constant.ACTIVITY.SUPPORT));
            });
        } else {
            activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(), challenge.getChallengerId(), Constant.ACTIVITY.SUPPORT));
        }
    }

    @Override
    public void updateProgressOrDoneForSelf(String challengeId, String result, Boolean done) {
        Challenge chl = chlRepo.findById(challengeId).get();
        SelfChallenge selfChl = (SelfChallenge) chl;
        if(selfChl != null && !selfChl.getDone()) {
            selfChl.setDone(done);
            selfChl.setResult(result);
            selfChl.setUpdateDate(new Date());
            chlRepo.save(selfChl);
        } else
            Exception.throwUpdateCannotForDone();
    }

    @Override
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore) {
        Validation.doneValidationForVersus(true, firstTeamScore, secondTeamScore);
        Challenge chl = chlRepo.findById(challengeId).get();
        VersusChallenge versusChl = (VersusChallenge) chl;
        if(versusChl != null && !versusChl.getDone()) {
            versusChl.setFirstTeamScore(firstTeamScore);
            versusChl.setSecondTeamScore(secondTeamScore);
            versusChl.setDone(true);
            versusChl.setUpdateDate(new Date());
            chlRepo.save(versusChl);
        } else
            Exception.throwUpdateCannotForDone();
    }

    @Override
    public void deleteChallenge(String challengeId) {
        Challenge findChl = chlRepo.findById(challengeId).get();
        if(findChl != null) {
            findChl.setDeleted(true);
            chlRepo.save(findChl);
        }
    }

    @Override
    public Iterable<Challenge> getAllChallenges() {
        return chlRepo.findAll();
    }

    @Override
    public void joinToChallenge(JoinToChallenge joinToChallenge) {
        JoinAttendance join = joinAndProofRepo.findByChallengeIdAndMemberId(joinToChallenge.getChallengeId(), joinToChallenge.getMemberId());
        Challenge challenge = chlRepo.findById(joinToChallenge.getChallengeId()).get();
        String activityTableId;
        if (join != null) {
            if (joinToChallenge.getJoin()) {
                join.setJoin(true);
                join.setReject(false);
                activateChallenge(challenge);
            } else {
                join.setJoin(false);
                join.setReject(true);
            }
            joinAndProofRepo.save(join);
            activityTableId = join.getId();
        } else {
            JoinAttendance joinAttendance = new JoinAttendance();
            joinAttendance.setProof(false);
            joinAttendance.setJoin(joinToChallenge.getJoin());
            joinAttendance.setChallengeId(joinToChallenge.getChallengeId());
            joinAttendance.setMemberId(joinToChallenge.getMemberId());
            joinAttendance.setChallenger(false);
            joinAttendance.setReject(false);
            joinAndProofRepo.save(joinAttendance);
            activityTableId = joinAttendance.getId();
        }
        activityService.createActivity(Mappers.prepareActivity(activityTableId, joinToChallenge.getChallengeId(), joinToChallenge.getMemberId(), challenge.getChallengerId(), Constant.ACTIVITY.JOIN));
    }

    private void activateChallenge(Challenge challenge) {
        if (!challenge.getActive()) {
            challenge.setUntilDate(setUntilDateFromChallengeTime(challenge.getChallengeTime()));
            challenge.setDateOfUntil(addDayToToday(challenge.getChallengeTime()));
            challenge.setActive(true);
            chlRepo.save(challenge);
        }
    }

    private String setUntilDateFromChallengeTime(String challengeTime) {
        Date untilDate = addDayToToday(challengeTime);
        return DateUtil.toString(untilDate);
    }

    private Date addDayToToday(String challengeTime) {
        Date current = new Date();
        LocalDateTime localDateTime = current.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(Integer.valueOf(challengeTime));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void acceptOrRejectChl(VersusAttendance versusAtt) {
        VersusAttendance result = versusRepo.findByMemberIdAndChallengeId(versusAtt.getMemberId(), versusAtt.getChallengeId());
        if(result != null && versusAtt.getAccept()) {
            result.setAccept(versusAtt.getAccept());
            result.setReject(false);
            versusRepo.save(result);
            activateChallengeForVersus(versusAtt);
        } else if (result != null && !versusAtt.getAccept()) {
            result.setAccept(false);
            result.setReject(true);
            versusRepo.save(result);
        } else
            Exception.throwNotFoundRecord();
    }

    private void activateChallengeForVersus(VersusAttendance versusAtt) {
        Challenge challenge = chlRepo.findById(versusAtt.getChallengeId()).get();
        if (!challenge.getActive()) {
            Boolean isAllParticipantAccept = true;
            List<VersusAttendance> versusAttendances = versusRepo.findByChallengeId(versusAtt.getChallengeId());
            for (VersusAttendance versus: versusAttendances) {
                if (!versus.getAccept())
                    isAllParticipantAccept = false;
            }
            if (isAllParticipantAccept)
                activateChallenge(challenge);
        }
    }

    @Override
    public List<ChallengeRequest> getChallengeRequests(String memberId) {
        List<ChallengeRequest> challengeRequests = new ArrayList<>();
        List<JoinAttendance> joinAttendances = joinAndProofRepo.findChallengeRequests(memberId);
        List<VersusAttendance> versusAttendances = versusRepo.findChallengeRequests(memberId);
        joinAttendances.forEach(joinAttendance -> {
            Challenge challenge = chlRepo.findById(joinAttendance.getChallengeId()).get();
            Member member = memberService.getMemberInfo(challenge.getChallengerId());
            ChallengeRequest request = new ChallengeRequest();
            request.setChallengeId(joinAttendance.getChallengeId());
            request.setFacebookID(member.getFacebookID());
            request.setMemberId(member.getId());
            request.setName(member.getName());
            request.setSurname(member.getSurname());
            request.setSubject(challenge.getSubject().toString());
            request.setType(Constant.REQUEST_TYPE.JOIN);
            challengeRequests.add(request);
        });
        versusAttendances.forEach(versusAttendance -> {
            Challenge challenge = chlRepo.findById(versusAttendance.getChallengeId()).get();
            Member member = memberService.getMemberInfo(challenge.getChallengerId());
            ChallengeRequest request = new ChallengeRequest();
            request.setChallengeId(versusAttendance.getChallengeId());
            request.setFacebookID(member.getFacebookID());
            request.setMemberId(member.getId());
            request.setName(member.getName());
            request.setSurname(member.getSurname());
            request.setSubject(challenge.getSubject().toString());
            request.setType(Constant.REQUEST_TYPE.ACCEPT);
            challengeRequests.add(request);
        });
        return challengeRequests;
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
        textComment.setDate(new Date());
        commentRepository.save(textComment);
        Challenge challenge = chlRepo.findById(textComment.getChallengeId()).get();
        activityService.createActivity(Mappers.prepareActivity(textComment.getId(), textComment.getChallengeId(), textComment.getMemberId(), challenge.getChallengerId(), Constant.ACTIVITY.COMMENT));
    }

    @Override
    public Iterable<TextComment> getComments(String challengeId) {
        Iterable<TextComment> textComments = commentRepository.findByChallengeId(challengeId);
        for (TextComment comment : textComments) {
            Member member = memberService.getMemberInfo(comment.getMemberId());
            comment.setName(member.getName());
            comment.setFbID(member.getFacebookID());
        }
        return textComments;
    }
}
