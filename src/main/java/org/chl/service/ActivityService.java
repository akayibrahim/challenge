package org.chl.service;

import org.chl.intf.IActivityService;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    @Override
    public void createActivity(Activity activity) {
        activity.setInsertDate(new Date());
        if (activity.getType().equals(Constant.ACTIVITY.PROOF)) {
            Activity exist = activityRepo.findExistActivity(activity.getChallengeId(), activity.getToMemberId(), activity.getFromMemberId(), activity.getType());
            if (exist != null) {
                exist.setType(Constant.ACTIVITY.PROOF);
                activityRepo.save(exist);
                return;
            }
        }
        activityRepo.save(activity);
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
                    Optional<TextComment> textComment = commentRepository.findById(activity.getActivityTableId());
                    activity.setContent(Constant.COMMENTED + textComment.get().getComment());
                    break;
                case PROOF:
                    JoinAttendance proofAttendance = joinAndProofAttendanceRepository.findById(activity.getActivityTableId()).get();
                    Proof proof = proofRepository.findByChallengeIdAndMemberId(proofAttendance.getChallengeId(), proofAttendance.getMemberId());
                    Optional<Challenge> challengeOfProof = challengeRepository.findById(proofAttendance.getChallengeId());
                    activity.setContent(String.format(Constant.PROOFED_CHALLENGE, challengeOfProof.get().getSubject().toString()));
                    activity.setMediaObjectId(proof.getProofObjectId());
                    break;
                case SUPPORT:
                    Optional<Support> support = supportRepository.findById(activity.getActivityTableId());
                    activity.setContent(support.get().getSupportFirstTeam() ? Constant.YOUR_TEAM : Constant.YOUR_OPPONENT_TEAM);
                    break;
                case ACCEPT:
                    Optional<VersusAttendance> versusAttendance = versusAttendanceRepository.findById(activity.getActivityTableId());
                    Optional<Challenge> challengeOfVersus = challengeRepository.findById(versusAttendance.get().getChallengeId());
                    if (versusAttendance.get().getAccept() != null)
                        activity.setContent((versusAttendance.get().getAccept() ? Constant.ACCEPT : Constant.REJECT) + Constant.SPACE + challengeOfVersus.get().getSubject().toString()); // TODO
                    else
                        activity.setContent(String.format(Constant.ACCEPT_REQUEST, challengeOfVersus.get().getSubject().toString()));
                    break;
                case JOIN:
                    Optional<JoinAttendance> joinAttendance = joinAndProofAttendanceRepository.findById(activity.getActivityTableId());
                    Challenge challengeOfJoin = challengeRepository.findById(joinAttendance.get().getChallengeId()).get();
                    if (joinAttendance.get().getJoin())
                        activity.setContent(String.format(Constant.JOINED_TO_CHALLENGE, challengeOfJoin.getSubject().toString()));
                    else
                        activity.setContent(String.format(Constant.JOIN_REQUEST_CONTENT, challengeOfJoin.getSubject().toString()));
                    break;
                case FOLLOWER:
                    activity.setContent(Constant.START_TO_FOLLOW_YOU);
                    break;
                case FOLLOWING:
                    activity.setFacebookID(toMember.getFacebookID());
                    activity.setName(toMember.getName() + Constant.SPACE + member.getSurname());
                    activity.setContent(String.format(Constant.START_TO_FOLLOWING, member.getName() + Constant.SPACE + member.getSurname()));
                    break;
                default:

            }
        });
        return activities;
    }
}
