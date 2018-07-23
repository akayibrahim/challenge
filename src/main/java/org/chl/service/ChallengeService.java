package org.chl.service;

import org.chl.intf.IChallengeService;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.util.*;
import org.chl.util.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ibrahim on 11/24/2017.
 */
@Service
public class ChallengeService implements IChallengeService {
    private ChallengeRepository chlRepo;
    private SupportRepository supportRepository;
    private NotificationService notificationService;
    private MemberService memberService;
    private CommentRepository commentRepository;
    private TrendChallengeRepository trendChallengeRepository;
    private ActivityService activityService;
    private PostShowedRepository postShowedRepository;

    @Autowired
    public ChallengeService(ChallengeRepository chlRepo, SupportRepository supportRepository, NotificationService notificationService, MemberService memberService, CommentRepository commentRepository,
                            TrendChallengeRepository trendChallengeRepository, ActivityService activityService, PostShowedRepository postShowedRepository) {
        this.chlRepo = chlRepo;
        this.supportRepository = supportRepository;
        this.notificationService = notificationService;
        this.memberService = memberService;
        this.commentRepository = commentRepository;
        this.trendChallengeRepository = trendChallengeRepository;
        this.activityService = activityService;
        this.postShowedRepository = postShowedRepository;
    }

    @Override
    public void save(Challenge challenge) {
        chlRepo.save(challenge);
    }

    @Override
    public Challenge getChallengeById(String challangeId) {
        return chlRepo.findById(challangeId).get();
    }

    @Override
    public Iterable<Challenge> getChallenges(String memberId, int page) {
        List<String> friendLists = memberService.getFollowingIdList(memberId);
        List<Challenge> challenges = new ArrayList<>();
        List<Challenge> challengesAsPageable = getChallengeAsPageable(friendLists, page);
        challengesAsPageable.stream().distinct().filter(chl -> chl.isSelf()).forEach(chl -> {
            challenges.add(chl);
        });
        challengesAsPageable.stream().distinct().filter(chl -> chl.isJoin()).forEach(chl -> {
            chl.getJoinAttendanceList().stream().filter(join -> friendLists.contains(join.getMemberId()) &&
                    (join.getJoin() || join.getProof())).forEach(join -> {
                changeChallengeWithAttendanceInfo(challenges, chl.getId(), join.getMemberId(), join.getFacebookID());
            });
        });
        challengesAsPageable.stream().distinct().filter(chl -> chl.isVersus()).forEach(chl -> {
            chl.getVersusAttendanceList().stream().filter(versus -> friendLists.contains(versus.getMemberId())).forEach(versus -> {
                changeChallengeWithAttendanceInfo(challenges, chl.getId(), versus.getMemberId(), versus.getFacebookID());
            });
        });
        List<String> friendListsWithMember = friendLists;
        friendListsWithMember.add(memberId);
        List<Challenge> publicChallenges = getPublicChallenges(friendListsWithMember, page);
        challenges.addAll(publicChallenges);
        prepareChallengesData(memberId, challenges, false, true);
        return challenges.stream().sorted(Comparator.comparing(Challenge::getUpdateDate).reversed()).collect(Collectors.toList());
    }

    private boolean postShowedControl(String memberId, Challenge chl) {
        List<PostShowed> postShowed = postShowedRepository.findByMemberIdAndChallengeIdAndChallengerId(memberId, chl.getId(), chl.getChallengerId());
        return postShowed.isEmpty();
    }

    private void changeChallengeWithAttendanceInfo(List<Challenge> challenges, String challengeId, String memberId, String facebookID) {
        Challenge challenge = chlRepo.findById(challengeId).get();
        challenge.setChallengerId(memberId);
        challenge.setChallengerFBId(facebookID);
        if (challenges.stream().noneMatch(chl -> chl.getId().equals(challenge.getId())))
            challenges.add(challenge);
    }

    private List<Challenge> getChallengeAsPageable(List<String> friendLists, int page) {
        Page<Challenge> nextPage;
        List<Challenge> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(Sort.Direction.DESC, "updateDate");
            nextPage = chlRepo.findChallenges(friendLists, Constant.TYPE.PUBLIC, new Date(), Util.getPageable(page, sort, 7));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<Challenge>();
    }

    private List<Challenge> getPublicChallenges(List<String> friendLists, int page) {
        Page<Challenge> nextPage;
        List<Challenge> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(Sort.Direction.DESC, "updateDate");
            nextPage = chlRepo.findPublicChallenges(friendLists, Constant.TYPE.PUBLIC, new Date(), Util.getPageable(page, sort, 3));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<Challenge>();
    }

    @Override
    public Iterable<Challenge> getChallengesOfMember(String memberId, int page) {
        setToDoneForPastChallenge(memberId);
        List<Integer> visibilities = fullVisibility();
        List<Challenge> challenges = findMemberChallenges(memberId, visibilities, true, page);
        return challenges;
    }

    private List<Integer> fullVisibility() {
        List<Integer> visibilities = new ArrayList<>();
        visibilities.add(Constant.VISIBILITY.EVERYONE.getCode());
        visibilities.add(Constant.VISIBILITY.FRIENDS.getCode());
        visibilities.add(Constant.VISIBILITY.JUST.getCode());
        return visibilities;
    }

    private List<Integer> visibilityWithputJust() {
        List<Integer> visibilities = new ArrayList<>();
        visibilities.add(Constant.VISIBILITY.EVERYONE.getCode());
        visibilities.add(Constant.VISIBILITY.FRIENDS.getCode());
        return visibilities;
    }

    @Override
    public Iterable<Challenge> getChallengesOfFriend(String memberId, String friendMemberId, int page) {
        List<Integer> visibilities = prepareVisibilityOfFriends(memberId, friendMemberId);
        List<Challenge> challenges = findMemberChallenges(friendMemberId, visibilities, false, page);
        return challenges;
    }

    private List<Integer> prepareVisibilityOfFriends(String memberId, String friendMemberId) {
        List<Integer> visibilities = new ArrayList<>();
        visibilities.add(Constant.VISIBILITY.EVERYONE.getCode());
        if (memberService.isMyFriend(memberId, friendMemberId))
            visibilities.add(Constant.VISIBILITY.FRIENDS.getCode());
        return visibilities;
    }

    private List<Challenge> getChallengeAsPageableForProfile(String memberId, List<Integer> visibilities, List<Boolean> actives, int page) {
        Page<Challenge> nextPage;
        List<Challenge> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "done"), new Sort.Order(Sort.Direction.DESC, "chlDate"));
            nextPage = chlRepo.findChallengesByMemberId(memberId, visibilities, actives, Util.getPageable(page, sort, Constant.DEFAULT_PAGEABLE_SIZE));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<Challenge>();
    }

    private List<Challenge> findMemberChallenges(String memberId, List<Integer> visibilities, Boolean myProfile, int page) {
        Boolean activesForMyProfile[] = {true, false};
        Boolean activesForOthers[] = {true};
        List<Boolean> actives = myProfile ? Arrays.asList(activesForMyProfile) : Arrays.asList(activesForOthers);
        List<Challenge> challenges = new ArrayList<>();
        List<Challenge> challengesOfMember = getChallengeAsPageableForProfile(memberId, visibilities, actives, page);
        challenges.addAll(challengesOfMember);
        prepareChallengesData(memberId, challenges, true, false);
        return challenges.stream().sorted(Comparator.comparing(Challenge::getChlDate).reversed()).collect(Collectors.toList());
    }

    private void setToDoneForPastChallenge(String memberId) {
        List<Integer> visibilities = fullVisibility();
        Boolean actives[] = {true, false};
        List<Challenge> challengesByMemberId = chlRepo.findChallengesByMemberId(memberId, visibilities, Arrays.asList(actives), new Sort(Sort.Direction.DESC, "updateDate"));
        challengesByMemberId
                .stream()
                .filter(chl -> !chl.getDone() && chl.getActive() && DateUtil.covertToDate(chl.getUntilDate()).compareTo(new Date()) <= 0)
                .forEach(challenge -> {
                    Challenge exist = chlRepo.findById(challenge.getId()).get();
                    exist.setDone(true);
                    exist.setUpdateDate(new Date());
                    chlRepo.save(exist);
                });
    }

    @Override
    public Iterable<Challenge> getExplorerChallenges(String memberId, String challengeId, Boolean addSimilarChallenges, int page) {
        List<Challenge> challenges = new ArrayList<>();
        Challenge explorerChallenge = chlRepo.findById(challengeId).get();
        if (page == 0)
            challenges.add(explorerChallenge);
        addSimilarChallenges(addSimilarChallenges, challenges, explorerChallenge, page);
        prepareChallengesData(memberId, challenges, false, false);
        Iterable<Challenge> challengeIterable = challenges;
        return challengeIterable;
    }

    private void addSimilarChallenges(Boolean addSimilarChallanges, List<Challenge> challenges, Challenge explorerChallenge, int page) {
        if (addSimilarChallanges) {
            List<Challenge> subjectChallenges = getSimilarChallenges(explorerChallenge.getSubject().toString(), page);
            subjectChallenges.stream()
                    .filter(chl -> !chl.getId().equals(explorerChallenge.getId()))
                    .forEach(chl -> {
                        challenges.add(chl);
                    });
        }

    }

    private List<Challenge> getSimilarChallenges(String subject, int page) {
        Page<Challenge> nextPage;
        List<Challenge> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(Sort.Direction.DESC, "updateDate");
            nextPage = chlRepo.findChallengesBySubjectAndType(subject, Constant.TYPE.PUBLIC, new Date(), Util.getPageable(page, sort, Constant.DEFAULT_PAGEABLE_SIZE));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<Challenge>();
    }

    @Override
    public Iterable<Trends> getTrendChallenges(String memberId, String subjectSearchKey, int page) {
        List<Trends> trendList = new ArrayList<>();
        List<TrendChallenge> trendChallenges = getTrendChallengeAsPageable(memberId, subjectSearchKey, page);
        for (TrendChallenge trendChallenge : trendChallenges) {
            Challenge challenge = chlRepo.findById(trendChallenge.getChallengeId()).get();
            trendList.add(prepareTrend(challenge));
        }
        if (!StringUtils.hasText(subjectSearchKey) && trendList.stream().count() < 10) {
            List<String> member = new ArrayList<>();
            member.add(memberId);
            List<Challenge> publicChallenges = getPublicChallenges(member, 0);
            publicChallenges.stream()
                    .filter(challenge -> trendList.stream().noneMatch(trends -> trends.getChallengeId().equals(challenge.getId())))
                    .forEach(challenge -> {
                        trendList.add(prepareTrend(challenge));
                    });
        }
        return trendList;
    }

    private List<TrendChallenge> getTrendChallengeAsPageable(String memberId, String subjectSearchKey, int page) {
        Page<TrendChallenge> nextPage;
        List<TrendChallenge> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(Sort.Direction.DESC, "popularity");
            if (StringUtils.hasText(subjectSearchKey))
                nextPage = trendChallengeRepository.findTrendChallengesBySearch(subjectSearchKey, Constant.TYPE.PUBLIC, Util.getPageable(page, sort, Constant.DEFAULT_PAGEABLE_SIZE));
            else
                nextPage = trendChallengeRepository.findTrendChallengesByType(Constant.TYPE.PUBLIC, Util.getPageable(page, sort, Constant.DEFAULT_PAGEABLE_SIZE));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<TrendChallenge>();
    }

    private Trends prepareTrend(Challenge challenge) {
        Trends trend = new Trends();
        trend.setChallengeId(challenge.getId());
        trend.setName(challenge.getName());
        trend.setProoferFbID(challenge.getChallengerFBId());
        trend.setSubject(challenge.getSubject().toString());
        trend.setChallengerId(challenge.getChallengerId());
        return trend;
    }

    private void prepareChallengesData(String memberId, List<Challenge> challenges, Boolean comeFromSelf, Boolean comeFromFeeds) {
        challenges.stream().forEach(chl -> {
            Boolean isPublic = false;
            if (chl.isVersus()) {
                prepareVersusChallenge(chl);
            } else if (chl.isJoin()) {
                prepareJoinChallenge(chl, memberId);
                isPublic = true;
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
            Support memberSupport = getMemberSupport(memberId, isPublic, chl.getId(), chl.getChallengerId());
            chl.setSupportFirstTeam(memberSupport != null ? memberSupport.getSupportFirstTeam() : false);
            chl.setSupportSecondTeam(memberSupport != null ? memberSupport.getSupportSecondTeam() : false);
            List<Support> supports = getSupports(chl, isPublic);
            chl.setFirstTeamSupportCount(supports != null ? supports.size() : 0);
            List<Support> supportSecondTeam = getSupportsOfAway(chl, isPublic);
            chl.setSecondTeamSupportCount(supportSecondTeam != null ? supportSecondTeam.size() : 0);
            if (chl.getActive() && !chl.getDone())
                chl.setUntilDateStr(Calculations.calculateUntilDate(DateUtil.covertToDate(chl.getUntilDate())));
            chl.setInsertTime(Calculations.calculateInsertTime(chl.getChlDate()));
            JoinAttendance proofOfChallenger = Optional.ofNullable(chl.getJoinAttendanceList())
                    .orElseGet(Collections::emptyList).stream().filter(join -> join.getMemberId().equals(chl.getChallengerId())).findFirst().orElse(null);
            chl.setProofedByChallenger(proofOfChallenger != null ? proofOfChallenger.getProof() : false);
            setStatusOfChallenge(chl, proofOfChallenger);
            JoinAttendance proofOfMember = Optional.ofNullable(chl.getJoinAttendanceList())
                    .orElseGet(Collections::emptyList).stream().filter(join -> join.getMemberId().equals(memberId)).findFirst().orElse(null);
            chl.setProofed(proofOfMember != null ? proofOfMember.getProof() : false);
            if (comeFromFeeds)
                savePostShowed(memberId, chl);
        });
    }

    private void setStatusOfChallenge(Challenge chl, JoinAttendance proofOfChallenger) {
        if (proofOfChallenger != null && proofOfChallenger.getProof() && proofOfChallenger.getChallenger()) {
            chl.setStatus(Constant.STATUS.NEW_PROOF.getStatus());
        } else if (proofOfChallenger != null && proofOfChallenger.getProof()) {
            chl.setStatus(Constant.STATUS.PROOF.getStatus());
        } else if (proofOfChallenger != null && proofOfChallenger.getJoin()) {
            chl.setStatus(Constant.STATUS.JOIN.getStatus());
        } else if (!chl.getDone()) {
            chl.setStatus(String.format(Constant.STATUS.NEW.getStatus(), chl.isJoin() ? Constant.PUBLIC_CHL :
                    (chl.isVersus() ? Constant.TEAM_CHL : Constant.SELF_CHL)));
        } else if (chl.getDone()) {
            chl.setStatus(String.format(Constant.STATUS.FINISH.getStatus(), chl.isJoin() ? Constant.PUBLIC_CHL :
                    (chl.isVersus() ? Constant.TEAM_CHL : Constant.SELF_CHL)));
        }
    }

    private void prepareJoinChallenge(Challenge challenge, String memberId) {
        List<JoinAttendance> joinAttendances = challenge.getJoinAttendanceList();
        if (challenge.getActive()) {
            challenge.setJoinAttendanceList(new ArrayList<>());
            joinAttendances.stream()
                    .filter(join -> (join.getJoin() != null && join.getJoin()) || (join.getProof() != null && join.getProof()))
                    .forEach(join -> {
                        challenge.getJoinAttendanceList().add(join);
                    });
        }
        Integer teamSize = challenge.getJoinAttendanceList().size() - 1;
        challenge.setFirstTeamCount(BigDecimal.ONE.toString());
        if (!challenge.getToWorld())
            challenge.setSecondTeamCount(teamSize.toString());
        else
            challenge.setSecondTeamCount(BigDecimal.ZERO.toString());
        long proofSize = Optional.ofNullable(challenge.getJoinAttendanceList())
                .orElseGet(Collections::emptyList).stream()
                .filter(Objects::nonNull)
                .filter(join -> join.getProof()).collect(Collectors.toList()).size();
        challenge.setCountOfProofs((int) proofSize);
    }

    private void prepareVersusChallenge(Challenge challenge) {
        Integer teamSize = challenge.getVersusAttendanceList().size() / 2;
        challenge.setFirstTeamCount((teamSize).toString());
        challenge.setSecondTeamCount(teamSize.toString());
        challenge.setFirstTeamScore(challenge.getFirstTeamScore() != null ? challenge.getFirstTeamScore() : "-");
        challenge.setSecondTeamScore(challenge.getSecondTeamScore() != null ? challenge.getSecondTeamScore() : "-");
    }

    private List<Support> getSupportsOfAway(Challenge chl, Boolean isPublic) {
        List<Support> supportSecondTeam = null;
        if (isPublic)
            supportSecondTeam = supportRepository.findByChallengeIdAndSupportedMemberIdAndSupportSecondTeam(chl.getId(), chl.getChallengerId(), true);
        else
            supportSecondTeam = supportRepository.findByChallengeIdAndSupportSecondTeam(chl.getId(), true);
        return supportSecondTeam;
    }

    private List<Support> getSupports(Challenge chl, Boolean isPublic) {
        List<Support> supports;
        if (isPublic)
            supports = supportRepository.findByChallengeIdAndSupportedMemberIdAndSupportFirstTeam(chl.getId(), chl.getChallengerId(), true);
        else
            supports = supportRepository.findByChallengeIdAndSupportFirstTeam(chl.getId(), true);
        return supports;
    }

    private Support getMemberSupport(String memberId, Boolean isPublic, String id, String challengerId) {
        Support memberSupport = null;
        if (isPublic)
            memberSupport = supportRepository.findByMemberIdAndChallengeIdAndSupportedMemberId(memberId, id, challengerId);
        else
            memberSupport = supportRepository.findByMemberIdAndChallengeId(memberId, id);
        return memberSupport;
    }

    private void savePostShowed(String memberId, Challenge chl) {
        List<PostShowed> exits = postShowedRepository.findByMemberIdAndChallengeIdAndChallengerId(memberId, chl.getId(), chl.getChallengerId());
        if (exits.isEmpty()) {
            PostShowed postShowed = new PostShowed();
            postShowed.setChallengeId(chl.getId());
            postShowed.setChallengerId(chl.getChallengerId());
            postShowed.setMemberId(memberId);
            postShowed.setInsertDate(new Date());
            // postShowedRepository.save(postShowed);
        }
    }

    @Override
    public Challenge addVersusChallenge(Challenge challenge) {
        Validation.doneValidationForVersus(challenge.getDone(), challenge.getFirstTeamScore(), challenge.getSecondTeamScore());
        Validation.checkTeamCountEqual(challenge.getFirstTeamCount(), challenge.getSecondTeamCount());
        initializeVersusChallenge(challenge);
        challenge.getVersusAttendanceList().stream().forEach(versusAtt -> {
            if (!memberService.checkMemberAvailable(versusAtt.getMemberId()))
                Exception.throwMemberNotAvailable();
            versusAtt.setAccept(versusAtt.getMemberId().equals(challenge.getChallengerId()) ? true : null);
            versusAtt.setChallengeId(challenge.getId());
            versusAtt.setFacebookID(memberService.getMemberInfo(versusAtt.getMemberId()).getFacebookID());
            versusAtt.setReject(false);
        });
        chlRepo.save(challenge);
        challenge.getVersusAttendanceList().stream().forEach(versusAtt -> {
            if (!versusAtt.getMemberId().equals(challenge.getChallengerId()))
                activityService.createActivity(Mappers.prepareActivity(null, challenge.getId(), challenge.getChallengerId(), versusAtt.getMemberId(), Constant.ACTIVITY.ACCEPT));
            // sendNotification(versusChl.getChallengerId(), versusAtt.getMemberId(), Constant.PUSH_NOTIFICATION.DONE, DateUtil.covertToDate(versusChl.getUntilDate()));
        });
        return challenge;
    }

    @Override
    public Challenge addJoinChallenge(Challenge challenge) {
        initializeJoinChallenge(challenge);
        addChallengerToAttendance(challenge);
        saveAttendances(challenge);
        chlRepo.save(challenge);
        challenge.getJoinAttendanceList().stream().filter(join -> join != null && !join.getMemberId().equals("TO_WORLD"))
                .forEach(joinAtt -> {
                    if (!joinAtt.getChallenger())
                        activityService.createActivity(Mappers.prepareActivity(null, challenge.getId(), challenge.getChallengerId(), joinAtt.getMemberId(), Constant.ACTIVITY.JOIN));
                });
        return challenge;
    }

    private void saveAttendances(Challenge challenge) {
        challenge.getJoinAttendanceList().stream().filter(join -> join != null && !join.getMemberId().equals("TO_WORLD"))
                .forEach(joinAtt -> {
                    if (!memberService.checkMemberAvailable(joinAtt.getMemberId()))
                        Exception.throwMemberNotAvailable();
                    if (!joinAtt.getChallenger())
                        joinAtt.setChallenger(false);
                    joinAtt.setProof(false);
                    joinAtt.setChallengeId(challenge.getId());
                    joinAtt.setFacebookID(memberService.getMemberInfo(joinAtt.getMemberId()).getFacebookID());
                    joinAtt.setReject(false);
                });
    }

    private void addChallengerToAttendance(Challenge challenge) {
        JoinAttendance attendance = new JoinAttendance();
        attendance.setChallenger(true);
        attendance.setProof(true);
        attendance.setJoin(false);
        attendance.setMemberId(challenge.getChallengerId());
        challenge.getJoinAttendanceList().add(attendance);
    }

    private void initializeJoinChallenge(Challenge challenge) {
        challenge.setType(Constant.TYPE.PUBLIC);
        challenge.setChlDate(new Date());
        challenge.setUpdateDate(new Date());
        if (challenge.getJoinAttendanceList() == null || challenge.getJoinAttendanceList().size() == 0) {
            challenge.setActive(true);
            challenge.setToWorld(true);
            challenge.setJoinAttendanceList(new ArrayList<JoinAttendance>());
        } else {
            challenge.setToWorld(false);
            challenge.setActive(false);
        }
        if (challenge.getActive())
            challenge.setDateOfUntil(DateUtil.covertToDate(challenge.getUntilDate()));
    }

    private void initializeVersusChallenge(Challenge challenge) {
        challenge.setType(Constant.TYPE.PRIVATE);
        challenge.setUpdateDate(new Date());
        challenge.setChlDate(new Date());
        challenge.setActive(false);
        if (challenge.getActive())
            challenge.setDateOfUntil(DateUtil.covertToDate(challenge.getUntilDate()));
    }

    private void initializeSelfChallenge(Challenge challenge) {
        challenge.setType(Constant.TYPE.SELF);
        challenge.setUpdateDate(new Date());
        challenge.setChlDate(new Date());
        challenge.setActive(true);
        if (!challenge.getDone())
            challenge.setDateOfUntil(DateUtil.covertToDate(challenge.getUntilDate()));
    }

    @Override
    public Challenge addSelfChallenge(Challenge challenge) {
        if (!memberService.checkMemberAvailable(challenge.getChallengerId()))
            Exception.throwMemberNotAvailable();
        initializeSelfChallenge(challenge);
        chlRepo.save(challenge);
        return challenge;
    }

    @Override
    public Iterable<Subjects> getSubjects(boolean isSelf) {
        Iterable<Subjects> subjects = Constant.toList(Subject.values(), isSelf);
        return subjects;
    }

    @Override
    public void supportChallange(Support support) {
        Challenge challenge = chlRepo.findById(support.getChallengeId()).get();
        createTrend(support, challenge);
        String activityTableId;
        Support exist = null;
        if (StringUtils.hasText(support.getSupportedMemberId()))
            exist = supportRepository.findByMemberIdAndChallengeIdAndSupportedMemberId(support.getMemberId(), support.getChallengeId(), support.getSupportedMemberId());
        else
            exist = supportRepository.findByMemberIdAndChallengeId(support.getMemberId(), support.getChallengeId());
        if (exist != null) {
            exist.setSupportFirstTeam(support.getSupportFirstTeam());
            exist.setSupportSecondTeam(support.getSupportSecondTeam());
            supportRepository.save(exist);
            activityTableId = exist.getId();
        } else {
            supportRepository.save(support);
            activityTableId = support.getId();
        }
        addActivityForSupport(support, challenge, activityTableId);
    }

    private void addActivityForSupport(Support support, Challenge challenge, String activityTableId) {
        if (StringUtils.hasText(support.getSupportedMemberId()) ||
                (!StringUtils.hasText(support.getSupportedMemberId()) && !challenge.getChallengerId().equals(support.getMemberId())) &&
                        (support.getSupportFirstTeam() || support.getSupportSecondTeam())) {
            if (challenge instanceof VersusChallenge) {
                VersusChallenge versusChl = (VersusChallenge) challenge;
                versusChl.getVersusAttendanceList().forEach(versusAttendance -> {
                    if (support.getSupportFirstTeam()) {
                        if (versusAttendance.getFirstTeamMember())
                            activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(), versusAttendance.getMemberId(), Constant.ACTIVITY.SUPPORT));
                    } else if (support.getSupportSecondTeam()) {
                        if (versusAttendance.getSecondTeamMember())
                            activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(), versusAttendance.getMemberId(), Constant.ACTIVITY.SUPPORT));
                    }

                });
            } else {
                activityService.createActivity(Mappers.prepareActivity(activityTableId, support.getChallengeId(), support.getMemberId(),
                        StringUtils.hasText(support.getSupportedMemberId()) ? support.getSupportedMemberId() : challenge.getChallengerId(),
                        Constant.ACTIVITY.SUPPORT));
            }
        }
    }

    private void createTrend(Support support, Challenge challenge) {
        TrendChallenge trendChallenge = trendChallengeRepository.findTrendByChallengeId(support.getChallengeId());
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
    }

    @Override
    public void updateProgressOrDoneForSelf(String challengeId, String result, Boolean done) {
        Challenge selfChl = chlRepo.findById(challengeId).get();
        if (selfChl != null && !selfChl.getDone()) {
            selfChl.setDone(done);
            selfChl.setResult(result);
            selfChl.setUpdateDate(new Date());
            chlRepo.save(selfChl);
        } else
            Exception.throwUpdateCannotForDone();
    }

    @Override
    public void updateResultsOfVersus(String challengeId, String firstTeamScore, String secondTeamScore, Boolean done) {
        Validation.doneValidationForVersus(true, firstTeamScore, secondTeamScore);
        Challenge versusChl = chlRepo.findById(challengeId).get();
        if (versusChl != null && !versusChl.getDone()) {
            versusChl.setFirstTeamScore(firstTeamScore);
            versusChl.setSecondTeamScore(secondTeamScore);
            versusChl.setDone(done);
            versusChl.setUpdateDate(new Date());
            chlRepo.save(versusChl);
        } else
            Exception.throwUpdateCannotForDone();
    }

    @Override
    public void deleteChallenge(String challengeId) {
        Challenge findChl = chlRepo.findById(challengeId).get();
        if (findChl != null) {
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
        Challenge challenge = chlRepo.findById(joinToChallenge.getChallengeId()).get();
        Optional<JoinAttendance> joinAttendance = challenge.getJoinAttendanceList().stream()
                .filter(join -> join.getMemberId().equals(joinToChallenge.getMemberId()))
                .findFirst();

        if (challenge.getJoinAttendanceList().stream().noneMatch(join -> join.getMemberId().equals(joinToChallenge.getMemberId())))
            challenge.getJoinAttendanceList().add(newAttendance(joinToChallenge));
        else {
            joinAttendance.ifPresent(join -> {
                if (joinToChallenge.getJoin()) {
                    join.setJoin(true);
                    join.setReject(false);
                    activateChallenge(challenge);
                } else {
                    join.setJoin(false);
                    join.setReject(true);
                }
            });
        }
        challenge.getJoinAttendanceList().stream()
                .filter(join -> !join.getChallenger() && join.getMemberId().equals(joinToChallenge.getMemberId()))
                .findFirst().ifPresent(join -> {
            activityService.createActivity(Mappers.prepareActivity(null, joinToChallenge.getChallengeId(), joinToChallenge.getMemberId(), challenge.getChallengerId(), Constant.ACTIVITY.JOIN));
        });
        chlRepo.save(challenge);
    }

    private JoinAttendance newAttendance(JoinToChallenge joinToChallenge) {
        JoinAttendance newAttendance = new JoinAttendance();
        newAttendance.setProof(false);
        newAttendance.setJoin(joinToChallenge.getJoin());
        newAttendance.setChallengeId(joinToChallenge.getChallengeId());
        newAttendance.setMemberId(joinToChallenge.getMemberId());
        newAttendance.setChallenger(false);
        newAttendance.setReject(false);
        return newAttendance;
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
        localDateTime = localDateTime.plusMinutes(Integer.valueOf(challengeTime));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public void acceptOrRejectChl(VersusAttendance versusAtt) {
        Challenge challenge = chlRepo.findById(versusAtt.getChallengeId()).get();
        challenge.getVersusAttendanceList().stream()
                .filter(versus -> versus.getMemberId().equals(versusAtt.getMemberId()))
                .findFirst().ifPresent(versus -> {
            versus.setAccept(versusAtt.getAccept());
            versus.setReject(!versusAtt.getAccept());
            activityService.createActivity(Mappers.prepareActivity(null, challenge.getId(), versusAtt.getMemberId(), challenge.getChallengerId(), Constant.ACTIVITY.ACCEPT));
        });
        chlRepo.save(challenge);
        if (!challenge.getActive() && challenge.getVersusAttendanceList().stream().
                noneMatch(ver -> ver.getAccept() != null && !ver.getAccept()))
                activateChallenge(challenge);
    }

    @Override
    public List<ChallengeRequest> getChallengeRequests(String memberId) {
        List<ChallengeRequest> challengeRequests = new ArrayList<>();
        List<Challenge> challenges = chlRepo.findChallengeRequests(memberId);
        challenges.stream().filter(join -> join.isJoin())
                .forEach(challenge -> {
                    challenge.getJoinAttendanceList().stream()
                            .filter(join -> !join.getChallenger() && join.getMemberId().equals(memberId)
                                    && !join.getReject() && !join.getJoin() && !join.getProof())
                            .forEach(chl -> {
                                Member member = memberService.getMemberInfo(challenge.getChallengerId());
                                ChallengeRequest request = new ChallengeRequest();
                                request.setChallengeId(challenge.getId());
                                request.setFacebookID(member.getFacebookID());
                                request.setMemberId(member.getId());
                                request.setName(member.getName());
                                request.setSurname(member.getSurname());
                                request.setSubject(challenge.getSubject().toString());
                                request.setType(Constant.REQUEST_TYPE.JOIN);
                                challengeRequests.add(request);
                            });
                });
        challenges.stream().filter(versus -> versus.isVersus() && !versus.getChallengerId().equals(memberId))
                .forEach(challenge -> {
                    challenge.getVersusAttendanceList().stream()
                            .filter(versus -> {
                                return !versus.getMemberId().equals(challenge.getChallengerId())
                                        && versus.getMemberId().equals(memberId) && versus.getAccept() == null;
                            })
                            .forEach(versusAttendance -> {
                                Member member = memberService.getMemberInfo(challenge.getChallengerId());
                                ChallengeRequest request = new ChallengeRequest();
                                request.setChallengeId(challenge.getId());
                                request.setFacebookID(member.getFacebookID());
                                request.setMemberId(member.getId());
                                request.setName(member.getName());
                                request.setSurname(member.getSurname());
                                request.setSubject(challenge.getSubject().toString());
                                request.setType(Constant.REQUEST_TYPE.ACCEPT);
                                challengeRequests.add(request);
                            });
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
        activityService.createActivity(Mappers.prepareActivity(textComment.getId(), textComment.getChallengeId(), textComment.getMemberId()
                , StringUtils.hasText(textComment.getCommentedMemberId()) ? textComment.getCommentedMemberId() : challenge.getChallengerId(), Constant.ACTIVITY.COMMENT));
    }

    @Override
    public Iterable<TextComment> getComments(String challengeId, int page) {
        Iterable<TextComment> textComments = getCommentsAsPageable(challengeId, page);
        for (TextComment comment : textComments) {
            Member member = memberService.getMemberInfo(comment.getMemberId());
            comment.setName(member.getName() + Constant.SPACE + member.getSurname());
            comment.setFbID(member.getFacebookID());
        }
        return textComments;
    }

    private List<TextComment> getCommentsAsPageable(String challengeId, int page) {
        Page<TextComment> nextPage;
        List<TextComment> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(Sort.Direction.DESC, "date");
            nextPage = commentRepository.findByChallengeId(challengeId, Util.getPageable(page, sort, Constant.DEFAULT_PAGEABLE_SIZE));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<TextComment>();
    }

    @Override
    public String getChallengeSizeOfMember(String memberId) {
        List<Integer> visibilities = fullVisibility();
        Boolean actives[] = {true, false};
        List<Challenge> challenges = chlRepo.findChallengeSizeByMemberId(memberId, visibilities, Arrays.asList(actives));
        return Integer.valueOf(challenges.size()).toString();
    }
}
