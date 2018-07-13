package org.chl.service;

import org.chl.intf.IActivityService;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by ibrahim on 06/28/2018.
 */
@Service
public class ActivityService implements IActivityService {
    @Autowired
    private ActivityRepository activityRepo;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private JoinAndProofAttendanceRepository joinAndProofAttendanceRepository;
    @Autowired
    private ProofRepository proofRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private VersusAttendanceRepository versusAttendanceRepository;
    @Autowired
    private SupportRepository supportRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    ActivityCountRepository activityCountRepository;

    @Override
    public void createActivity(Activity activity) {
        activity.setInsertDate(new Date());
        if (activity.getType().equals(Constant.ACTIVITY.PROOF)) {
            Activity exist = activityRepo.findExistActivity(activity.getChallengeId(), activity.getToMemberId(), activity.getFromMemberId(), activity.getType());
            if (exist != null) {
                exist.setType(Constant.ACTIVITY.PROOF);
                increaseActivityCount(activity.getToMemberId());
                activityRepo.save(exist);
                createNotification(exist.getType(), exist.getActivityTableId(), exist.getFromMemberId(), exist.getToMemberId(), exist.getChallengeId());
                return;
            }
        }
        increaseActivityCount(activity.getToMemberId());
        activityRepo.save(activity);
        createNotification(activity.getType(), activity.getActivityTableId(), activity.getFromMemberId(), activity.getToMemberId(), activity.getChallengeId());
    }

    private void increaseActivityCount(String memberId) {
        ActivityCount exist = activityCountRepository.findByMemberId(memberId);
        if (exist != null) {
            String countStr = exist.getCount();
            Integer countInt = Integer.valueOf(countStr);
            Integer count = countInt++;
            exist.setCount(count.toString());
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
        String nameSurname = member.getName() + Constant.SPACE + member.getSurname() + Constant.SPACE;
        String title = null;
        String content = null;
        switch (type) {
            case COMMENT:
                title = Constant.PUSH_NOTIFICATION.COMMENT.getMessageTitle();
                content = nameSurname + getCommentMessageContent(activityTableId);
                break;
            case PROOF:
                title = Constant.PUSH_NOTIFICATION.PROOF.getMessageTitle();
                content = nameSurname + getProofMessageContent(activityTableId);
                break;
            case SUPPORT:
                title = Constant.PUSH_NOTIFICATION.SUPPORT.getMessageTitle();
                content = nameSurname + getSupportMessageContent(activityTableId);
                break;
            case ACCEPT:
                title = Constant.PUSH_NOTIFICATION.ACCEPT.getMessageTitle();
                content = nameSurname + getAcceptMessageContent(activityTableId);
                break;
            case JOIN:
                title = Constant.PUSH_NOTIFICATION.JOIN.getMessageTitle();
                content = nameSurname + getJoinMessageContent(activityTableId);
                break;
            case FOLLOWER:
                title = Constant.PUSH_NOTIFICATION.FOLLOWER.getMessageTitle();
                content = nameSurname + getFollowerMessageContent(activityTableId);
                break;
            case FOLLOWING:
                title = Constant.PUSH_NOTIFICATION.FOLLOWING.getMessageTitle();
                nameSurname = toMember.getName() + Constant.SPACE + toMember.getSurname() + Constant.SPACE;
                content = nameSurname + getFollowingMessageContent(member.getName(), member.getSurname());
                break;
            default:
        }
        sendNotification(challengeId, fromMemberId, title, content);
    }

    private void sendNotification(String challengeId, String memberId, String title, String content) {
        PushNotification notification = new PushNotification();
        notification.setChallengeId(challengeId);
        notification.setMemberId(memberId);
        notification.setUntilDate(new Date());
        notification.setMessageTitle(title);
        notification.setMessage(content);
        notificationRepository.save(notification);
    }

    @Override
    public List<Activity> getActivities(String toMemberId) {
        List<Activity> activities = activityRepo.findActivityByToMemberId(toMemberId, new Sort(Sort.Direction.DESC, "insertDate"));
        activities.forEach(activity -> {
            Member member = memberRepository.findById(activity.getFromMemberId()).get();
            Member toMember = memberRepository.findById(toMemberId).get();
            activity.setFacebookID(member.getFacebookID());
            activity.setName(member.getName() + Constant.SPACE + member.getSurname());
            switch (activity.getType()) {
                case COMMENT:
                    activity.setContent(getCommentMessageContent(activity.getActivityTableId()));
                    break;
                case PROOF:
                    JoinAttendance proofAttendance = joinAndProofAttendanceRepository.findById(activity.getActivityTableId()).get();
                    Proof proof = proofRepository.findByChallengeIdAndMemberId(proofAttendance.getChallengeId(), proofAttendance.getMemberId());
                    activity.setMediaObjectId(proof.getProofObjectId());
                    activity.setContent(getProofMessageContent(activity.getActivityTableId()));
                    break;
                case SUPPORT:
                    activity.setContent(getSupportMessageContent(activity.getActivityTableId()));
                    break;
                case ACCEPT:
                    activity.setContent(getAcceptMessageContent(activity.getActivityTableId()));
                    break;
                case JOIN:
                    activity.setContent(getJoinMessageContent(activity.getActivityTableId()));
                    break;
                case FOLLOWER:
                    activity.setContent(getFollowerMessageContent(activity.getActivityTableId()));
                    break;
                case FOLLOWING:
                    activity.setFacebookID(toMember.getFacebookID());
                    activity.setName(toMember.getName() + Constant.SPACE + toMember.getSurname());
                    activity.setContent(getFollowingMessageContent(member.getName(), member.getSurname()));
                    break;
                default:

            }
        });
        return activities;
    }

    private String getFollowingMessageContent(String name, String surname) {
        return String.format(Constant.START_TO_FOLLOWING, name + Constant.SPACE + surname);
    }

    private String getFollowerMessageContent(String activityTableId) {
        return Constant.START_TO_FOLLOW_YOU;
    }

    private String getJoinMessageContent(String activityTableId) {
        Optional<JoinAttendance> joinAttendance = joinAndProofAttendanceRepository.findById(activityTableId);
        Challenge challengeOfJoin = challengeRepository.findById(joinAttendance.get().getChallengeId()).get();
        if (joinAttendance.get().getJoin())
            return String.format(Constant.JOINED_TO_CHALLENGE, challengeOfJoin.getSubject().toString());
        else
            return String.format(Constant.JOIN_REQUEST_CONTENT, challengeOfJoin.getSubject().toString());
    }

    private String getAcceptMessageContent(String activityTableId) {
        Optional<VersusAttendance> versusAttendance = versusAttendanceRepository.findById(activityTableId);
        Optional<Challenge> challengeOfVersus = challengeRepository.findById(versusAttendance.get().getChallengeId());
        if (versusAttendance.get().getAccept() != null)
            return (versusAttendance.get().getAccept() ? Constant.ACCEPT : Constant.REJECT) + Constant.SPACE + challengeOfVersus.get().getSubject().toString();
        else
            return String.format(Constant.ACCEPT_REQUEST, challengeOfVersus.get().getSubject().toString());
    }

    private String getSupportMessageContent(String activityTableId) {
        Optional<Support> support = supportRepository.findById(activityTableId);
        return support.get().getSupportFirstTeam() ? Constant.YOUR_TEAM : Constant.YOUR_OPPONENT_TEAM;
    }

    private String getProofMessageContent(String activityTableId) {
        JoinAttendance proofAttendance = joinAndProofAttendanceRepository.findById(activityTableId).get();
        Optional<Challenge> challengeOfProof = challengeRepository.findById(proofAttendance.getChallengeId());
        return String.format(Constant.PROOFED_CHALLENGE, challengeOfProof.get().getSubject().toString());
    }

    private String getCommentMessageContent(String activityTableId) {
        Optional<TextComment> textComment = commentRepository.findById(activityTableId);
        return Constant.COMMENTED + textComment.get().getComment();
    }
}
