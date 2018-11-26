package org.chl.service;

import org.chl.intf.IActivityService;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.util.Constant;
import org.chl.util.DateUtil;
import org.chl.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ibrahim on 06/28/2018.
 */
@Service
public class ActivityService implements IActivityService {
    @Autowired
    ActivityCountRepository activityCountRepository;
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProofRepository proofRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private SupportRepository supportRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NotificationService notificationService;

    @Override
    public void createActivity(Activity activity) {
        if (activity.getToMemberId().equals(activity.getFromMemberId()) &&
                !(activity.getType().equals(Constant.ACTIVITY.UPCOMING_WARMING) || activity.getType().equals(Constant.ACTIVITY.TIMES_UP)))
            return;
        activity.setInsertDate(DateUtil.getCurrentDatePlusThreeHour());
        if (Util.in(activity.getType().toString(), Constant.ACTIVITY.PROOF.toString(), Constant.ACTIVITY.UPCOMING_WARMING.toString(),
                Constant.ACTIVITY.TIMES_UP.toString(), Constant.ACTIVITY.SUPPORT.toString())) {
            Activity exist = activityRepo.findExistActivity(activity.getChallengeId(), activity.getToMemberId(), activity.getFromMemberId(),
                    !activity.getType().equals(Constant.ACTIVITY.PROOF) ? activity.getType() : Constant.ACTIVITY.JOIN);
            if (exist != null) {
                if (Util.in(activity.getType().toString(), Constant.ACTIVITY.UPCOMING_WARMING.toString(), Constant.ACTIVITY.TIMES_UP.toString())) {
                    return;
                } else if (activity.getType().equals(Constant.ACTIVITY.SUPPORT)) {
                    activityRepo.delete(exist);
                } else if (activity.getType().equals(Constant.ACTIVITY.PROOF)) {
                    exist.setType(Constant.ACTIVITY.PROOF);
                    exist.setInsertDate(DateUtil.getCurrentDatePlusThreeHour());
                    increaseActivityCount(activity.getToMemberId());
                    activityRepo.save(exist);
                    createNotification(exist.getType(), exist.getActivityTableId(), exist.getFromMemberId(), exist.getToMemberId(), exist.getChallengeId());
                    return;
                }
            }
        }
        increaseCountOfActivity(activity.getToMemberId());
        activityRepo.save(activity);
        createNotification(activity.getType(), activity.getActivityTableId(), activity.getFromMemberId(), activity.getToMemberId(), activity.getChallengeId());
    }

    @Override
    public void increaseActivityCount(String memberId) {
        increaseCountOfActivity(memberId);
    }

    private void increaseCountOfActivity(String memberId) {
        ActivityCount exist = activityCountRepository.findByMemberId(memberId);
        if (exist != null) {
            String countStr = exist.getCount();
            Integer countInt = Integer.valueOf(countStr);
            exist.setCount((++countInt).toString());
            activityCountRepository.save(exist);
        } else {
            ActivityCount activityCount = new ActivityCount();
            activityCount.setMemberId(memberId);
            activityCount.setCount(Constant.ONE);
            activityCountRepository.save(activityCount);
        }
    }

    private void createNotification(Constant.ACTIVITY type, String activityTableId, String fromMemberId, String toMemberId, String challengeId) {
        Member member = memberRepository.findById(fromMemberId).get();
        Member toMember = memberRepository.findById(toMemberId).get();
        String nameSurname = member.getName().trim() + Constant.SPACE + member.getSurname().trim() + Constant.SPACE;
        String title = null;
        String content = null;
        String messageContent = null;
        switch (type) {
            case COMMENT:
                title = Constant.PUSH_NOTIFICATION.COMMENT.getMessageTitle();
                messageContent = getCommentMessageContent(activityTableId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case PROOF:
                title = Constant.PUSH_NOTIFICATION.PROOF.getMessageTitle();
                messageContent = getProofMessageContent(challengeId, toMemberId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case SUPPORT:
                title = Constant.PUSH_NOTIFICATION.SUPPORT.getMessageTitle();
                messageContent = getSupportMessageContent(activityTableId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case ACCEPT:
                title = Constant.PUSH_NOTIFICATION.ACCEPT.getMessageTitle();
                messageContent = getAcceptMessageContent(challengeId, toMemberId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case JOIN:
                title = Constant.PUSH_NOTIFICATION.JOIN.getMessageTitle();
                messageContent = getJoinMessageContent(challengeId, toMemberId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case FOLLOWER:
                title = Constant.PUSH_NOTIFICATION.FOLLOWER.getMessageTitle();
                messageContent = getFollowerMessageContent(activityTableId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case FOLLOWING:
                title = Constant.PUSH_NOTIFICATION.FOLLOWING.getMessageTitle();
                nameSurname = toMember.getName() + Constant.SPACE + toMember.getSurname() + Constant.SPACE;
                messageContent = getFollowingMessageContent(member.getName(), member.getSurname());
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case FRIEND_REQUEST:
                title = Constant.PUSH_NOTIFICATION.FRIEND_REQUEST.getMessageTitle();
                messageContent = getFriendshipRequestMessageContent(activityTableId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case UPCOMING_WARMING:
                title = Constant.PUSH_NOTIFICATION.UPCOMING_WARMING.getMessageTitle();
                content = getUpcomingChallengeContent(challengeId);
                break;
            case TIMES_UP:
                title = Constant.PUSH_NOTIFICATION.TIMES_UP.getMessageTitle();
                content = getTimesUpContent(challengeId);
                break;
            case CHALLENGE_APPROVE:
                title = Constant.PUSH_NOTIFICATION.CHALLENGE_APPROVE.getMessageTitle();
                messageContent = getChallengeApproveMessageContent(challengeId);
                content = StringUtils.hasText(messageContent) ? nameSurname + messageContent : null;
                break;
            case CHALLENGE_REJECT:
                title = Constant.PUSH_NOTIFICATION.CHALLENGE_REJECT.getMessageTitle();
                content = getChallengeRejectMessageContent(challengeId);
                break;
            case CHALLENGE_APPROVED:
                title = Constant.PUSH_NOTIFICATION.CHALLENGE_APPROVED.getMessageTitle();
                content = getChallengeApprovedMessageContent(challengeId, nameSurname);
                break;
            default:
        }
        if (StringUtils.hasText(content)) {
            sendNotification(challengeId, toMemberId, title, content, toMember.getDeviceNotifyToken());
        }
    }

    private void sendNotification(String challengeId, String memberId, String title, String content, String deviceToken) {
        PushNotification notification = new PushNotification();
        notification.setChallengeId(challengeId);
        notification.setMemberId(memberId);
        notification.setUntilDate(DateUtil.getCurrentDatePlusThreeHour());
        notification.setMessageTitle(title);
        notification.setMessage(content);
        notification.setDeviceToken(deviceToken);
        notificationService.send(notification);
    }

    @Override
    public List<Activity> getActivities(String toMemberId, int page) {
        List<Activity> activities = getActivitiesAsPageable(toMemberId, page);
        activities.forEach(activity -> {
            Member member = memberRepository.findById(activity.getFromMemberId()).get();
            Member toMember = memberRepository.findById(activity.getToMemberId()).get();
            activity.setFacebookID(member.getFacebookID());
            activity.setName(member.getName().trim() + Constant.SPACE + member.getSurname().trim());
            switch (activity.getType()) {
                case COMMENT:
                    activity.setContent(getCommentMessageContent(activity.getActivityTableId()));
                    break;
                case PROOF:
                    Proof proof = proofRepository.findByChallengeIdAndMemberId(activity.getChallengeId(), activity.getFromMemberId());
                    activity.setMediaObjectId(proof.getProofObjectId());
                    Challenge chl = challengeRepository.findById(proof.getChallengeId()).get();
                    JoinAttendance proofOfChallenger = Optional.ofNullable(chl.getJoinAttendanceList())
                            .orElseGet(Collections::emptyList).stream().filter(join -> join.getMemberId().equals(chl.getChallengerId())).findFirst().orElse(null);
                    activity.setProvedWithImage(proofOfChallenger != null ? nvl(proofOfChallenger.getProvedWithImage(), true) : true);
                    activity.setContent(getProofMessageContent(activity.getChallengeId(), activity.getToMemberId()));
                    break;
                case SUPPORT:
                    activity.setContent(getSupportMessageContent(activity.getActivityTableId()));
                    break;
                case ACCEPT:
                    activity.setContent(getAcceptMessageContent(activity.getChallengeId(), activity.getToMemberId()));
                    break;
                case JOIN:
                    activity.setContent(getJoinMessageContent(activity.getChallengeId(), activity.getToMemberId()));
                    break;
                case FOLLOWER:
                    activity.setContent(getFollowerMessageContent(activity.getActivityTableId()));
                    break;
                case FOLLOWING:
                    activity.setFacebookID(toMember.getFacebookID());
                    activity.setName(Constant.YOU);
                    activity.setContent(getFollowingMessageContent(member.getName(), member.getSurname()));
                    break;
                case ACCEPT_FRIEND_REQUEST:
                    activity.setContent(getAcceptFollowerRequestMessageContent(activity.getActivityTableId()));
                    break;
                case FRIEND_REQUEST:
                    activity.setContent(getFriendshipRequestMessageContent(activity.getActivityTableId()));
                    break;
                case CHALLENGE_APPROVE:
                    activity.setContent(getChallengeApproveMessageContent(activity.getChallengeId()));
                    break;
                case CHALLENGE_REJECT:
                    activity.setContent(getChallengeRejectMessageContent(activity.getChallengeId()));
                    break;
                case CHALLENGE_APPROVED:
                    activity.setContent(getChallengeApprovedMessageContent(activity.getChallengeId(), activity.getName()));
                    break;
                case UPCOMING_WARMING:
                    activity.setContent(getUpcomingChallengeContent(activity.getChallengeId()));
                    break;
                case TIMES_UP:
                    activity.setContent(Constant.SPACE + getTimesUpContent(activity.getChallengeId()));
                    break;
                default:

            }
        });
        return activities.stream().filter(act -> StringUtils.hasText(act.getContent())).collect(Collectors.toList());
    }

    private List<Activity> getActivitiesAsPageable(String toMemberId, int page) {
        Page<Activity> nextPage;
        List<Activity> dbRecords;
        boolean stop = false;
        do {
            Sort sort = new Sort(Sort.Direction.DESC, "insertDate");
            nextPage = activityRepo.findActivityByToMemberId(toMemberId, Util.getPageable(page, sort, 20));
            dbRecords = nextPage.getContent();
            if (dbRecords.size() > 0) {
                return dbRecords;
            } else {
                stop = true;
            }
        } while (nextPage.getSize() > 0 && !stop);
        return new ArrayList<Activity>();
    }

    private String getFollowingMessageContent(String name, String surname) {
        return String.format(Constant.START_TO_FOLLOWING, name + Constant.SPACE + surname);
    }

    private String getFollowerMessageContent(String activityTableId) {
        return Constant.START_TO_FOLLOW_YOU;
    }

    private String getAcceptFollowerRequestMessageContent(String activityTableId) {
        return Constant.ACCEPT_FOLLOWER_REQUEST;
    }

    private String getFriendshipRequestMessageContent(String activityTableId) {
        return Constant.FRIENDSHIP_FOLLOWER_REQUEST;
    }

    private String getChallengeApproveMessageContent(String challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        return String.format(Constant.CHALLENGE_APPROVE_CONTENT, challenge.getSubject().toString().replace(" CHALLENGE", ""));
    }

    private String getChallengeRejectMessageContent(String challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        if (challenge.getWaitForApprove()) {
            return String.format(Constant.CHALLENGE_REJECT_CONTENT, challenge.getSubject().toString().replace(" CHALLENGE", ""), challenge.getScoreRejectName());
        }
        return null;
    }

    private String getChallengeApprovedMessageContent(String challengeId, String name) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        if (challenge.getWaitForApprove()) {
            return String.format(Constant.CHALLENGE_APPROVED_CONTENT, challenge.getSubject().toString().replace(" CHALLENGE", ""), name);
        }
        return null;
    }

    private String getJoinMessageContent(String challengeId, String toMemberId) {
        Challenge challengeOfJoin = challengeRepository.findById(challengeId).get();
        if (challengeOfJoin.getJoinAttendanceList().stream().anyMatch(join -> !join.getChallenger()
                && join.getMemberId().equals(toMemberId) && !join.getReject() && !join.getJoin() && !join.getProof()))
            return String.format(Constant.JOIN_REQUEST_CONTENT, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        else if (challengeOfJoin.getJoinAttendanceList().stream().anyMatch(join -> !join.getChallenger() &&
                !join.getMemberId().equals(toMemberId) && join.getReject()))
            return String.format(Constant.DONT_JOINED_TO_CHALLENGE, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        else if (challengeOfJoin.getJoinAttendanceList().stream().anyMatch(join -> !join.getChallenger() &&
                !join.getMemberId().equals(toMemberId) && !join.getReject() && join.getJoin() && !join.getProof()))
            return String.format(Constant.JOINED_TO_CHALLENGE, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        return null;
    }

    private String getUpcomingChallengeContent(String challengeId) {
        Challenge challengeOfJoin = challengeRepository.findById(challengeId).get();
        if (!challengeOfJoin.getDone())
            return String.format(Constant.UPCOMING_CHALLENGE, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        else return null;
    }

    private String getTimesUpContent(String challengeId) {
        Challenge challengeOfJoin = challengeRepository.findById(challengeId).get();
        return String.format(Constant.TIMES_UP_CHALLENGE, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
    }

    private String getAcceptMessageContent(String challengeId, String toMemberId) {
        Challenge challengeOfJoin = challengeRepository.findById(challengeId).get();
        if (challengeOfJoin.getVersusAttendanceList().stream()
                .anyMatch(versus -> versus.getMemberId().equals(toMemberId) && versus.getAccept() == null)) {
            return String.format(Constant.ACCEPT_REQUEST, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        } else if (challengeOfJoin.getVersusAttendanceList().stream()
                .anyMatch(versus -> toMemberId.equals(challengeOfJoin.getChallengerId()) &&
                        !versus.getMemberId().equals(toMemberId) && (versus.getAccept() == null || (versus.getAccept() != null && versus.getAccept())) && !versus.getReject())) {
            return String.format(Constant.ACCEPT, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        } else if (challengeOfJoin.getVersusAttendanceList().stream()
                .anyMatch(versus -> toMemberId.equals(challengeOfJoin.getChallengerId()) &&
                        !versus.getMemberId().equals(toMemberId) && versus.getAccept() != null && !versus.getAccept() && versus.getReject())) {
            return String.format(Constant.REJECT, challengeOfJoin.getSubject().toString().replace(" CHALLENGE", ""));
        }
        return null;
    }

    private String getSupportMessageContent(String activityTableId) {
        Optional<Support> support = supportRepository.findById(activityTableId);
        Challenge challenge = challengeRepository.findById(support.get().getChallengeId()).get();
        return support.get().getSupportFirstTeam() ?
                (challenge.getType().compareTo(Constant.TYPE.PRIVATE) == 0 ?
                        String.format(Constant.YOUR_TEAM, challenge.getSubject().replace(" CHALLENGE", "")) :
                        String.format(Constant.SUPPORT_YOU, challenge.getSubject().replace(" CHALLENGE", "")))
                : support.get().getSupportSecondTeam() ? String.format(Constant.YOUR_OPPONENT_TEAM, challenge.getSubject().replace(" CHALLENGE", "")) : null;
    }

    private String getProofMessageContent(String challengeId, String toMemberId) {
        Challenge challengeOfProof = challengeRepository.findById(challengeId).get();
        if (challengeOfProof.getJoinAttendanceList().stream()
                .anyMatch(join -> join.getMemberId().equals(challengeOfProof.getChallengerId()) &&
                        join.getMemberId().equals(toMemberId) && join.getProof())) {
            return String.format(Constant.PROOFED_CHALLENGE, challengeOfProof.getSubject().toString().replace(" CHALLENGE", ""));
        }
        return null;
    }

    private String getCommentMessageContent(String activityTableId) {
        TextComment textComment = commentRepository.findById(activityTableId).get();
        Challenge challenge = challengeRepository.findById(textComment.getChallengeId()).get();
        return String.format(Constant.COMMENTED, textComment.getComment(), challenge.getSubject().replace(" CHALLENGE", ""));
    }

    private static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
